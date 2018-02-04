package sonic.sync.core.security;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.SecretKey;

import sonic.sync.core.event.IUserProfileModification;
import sonic.sync.core.exception.AbortModifyException;
import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.network.PutQueueEntry;
import sonic.sync.core.network.QueueEntry;
import sonic.sync.core.util.Constants;
import sonic.sync.core.util.PasswordUtil;

public class UserProfileManager {

	private static final long MAX_MODIFICATION_TIME = 1000;
	private static final long FAILOVER_TIMEOUT = 5 * 60 * 1000;
	private static final int FORK_LIMIT = 2;

	private final AESEncryptedVersionManager<UserProfile> versionManager;
	private final UserCredentials credentials;

	private final Object queueWaiter = new Object();
	private final Queue<QueueEntry> readOnlyQueue = new ConcurrentLinkedQueue<QueueEntry>();
	private final Queue<PutQueueEntry> modifyQueue = new ConcurrentLinkedQueue<PutQueueEntry>();
	private final AtomicBoolean running = new AtomicBoolean(false);

	private volatile PutQueueEntry modifying;

	private KeyPair protectionKeys = null;
	private Thread workerThread;

	public UserProfileManager(DataManager dataManager, UserCredentials credentials) {
		this.credentials = credentials;

		SecretKey passwordKey = PasswordUtil.generateAESKeyFromPassword(credentials.getPassword(), credentials.getPin(),
				Constants.KEYLENGTH_USER_PROFILE);
		this.versionManager = new AESEncryptedVersionManager<UserProfile>(dataManager, passwordKey,
				credentials.getProfileLocationKey(), Constants.USER_PROFILE);
		startQueueWorker();
	}

	public void stopQueueWorker() {
		if (!running.get()) {
			System.err.println("The user profile manager has already been shutdown");
			return;
		}

		running.set(false);

		try {
			// interrupt the thread such that blocking 'wait' calls throw an exception and the thread can
			// shutdown gracefully
			workerThread.checkAccess();
			workerThread.interrupt();
		} catch (SecurityException e) {
			System.err.println("Cannot stop the user profile thread " + e.getMessage());
		}
	}

	public void startQueueWorker() {
		if (running.get()) {
			System.err.println("Queue worker is already running");
		} else {
			running.set(true);
			workerThread = new Thread(new QueueWorker());
			workerThread.setName("UP queue");
			workerThread.start();
		}
	}

	public AESEncryptedVersionManager<UserProfile> getVersionManager() {
		return versionManager;
	}
	
	public UserCredentials getUserCredentials() {
		return credentials;
	}

	/**
	 * Gets the user profile (read-only). The call blocks until the most recent profile is here.
	 * 
	 * @return the user profile
	 * @throws GetFailedException if the profile cannot be fetched
	 */
	public UserProfile readUserProfile() throws GetFailedException {
		QueueEntry entry = new QueueEntry();
		readOnlyQueue.add(entry);

		synchronized (queueWaiter) {
			queueWaiter.notify();
		}

		UserProfile profile = entry.getUserProfile();
		if (profile == null) {
			throw new GetFailedException("User Profile not found");
		}
		return profile;
	}

	/**
	 * Gets the user profile and allows to modify it. The call blocks until
	 * {@link IUserProfileModification#modifyUserProfile(UserProfile)} is called or an exception is thrown.
	 * 
	 * @param pid the process identifier
	 * @param modifier the implementation where the modification is done
	 * @throws GetFailedException if the profile cannot be fetched
	 * @throws AbortModifyException if the modification was aborted
	 */
	public void modifyUserProfile(String pid, IUserProfileModification modifier) throws GetFailedException,
			PutFailedException {
		PutQueueEntry entry = new PutQueueEntry(pid);
		modifyQueue.add(entry);

		synchronized (queueWaiter) {
			queueWaiter.notify();
		}

		UserProfile profile;
		try {
			profile = entry.getUserProfile();
			if (profile == null) {
				throw new GetFailedException("User Profile not found");
			}
		} catch (GetFailedException e) {
			// just stop the modification if an error occurs.
			if (modifying != null && modifying.getPid().equals(pid)) {
				modifying.abort();
			}
			throw e;
		}

		boolean retryPut = true;
		int forkCounter = 0;
		int forkWaitTime = new Random().nextInt(1000) + 500;
		while (retryPut) {
			// user starts modifying it
			modifier.modifyUserProfile(profile);

			// put the updated user profile
			if (protectionKeys == null) {
				protectionKeys = profile.getProtectionKeys();
			}

			if (modifying != null && modifying.getPid().equals(pid)) {
				modifying.setUserProfile(profile);
				modifying.readyToPut();
				modifying.waitForPut();

				// successfully put the user profile
				retryPut = false;
			} else {
				throw new PutFailedException("Not allowed to put anymore");
			}
		}
	}

	private class QueueWorker implements Runnable {

		@Override
		public void run() {
			// run forever
			while (running.get()) {
				// modifying processes have advantage here because the read-only processes can profit
				if (modifyQueue.isEmpty() && readOnlyQueue.isEmpty()) {
					synchronized (queueWaiter) {
						try {
							// timeout to prevent queues to live forever because of invalid shutdown
							queueWaiter.wait(FAILOVER_TIMEOUT);
						} catch (InterruptedException e) {
							// interrupted, go to next iteration, probably the thread was stopped
							continue;
						}
					}
				} else if (modifyQueue.isEmpty()) {
					System.err.println(readOnlyQueue.size() + " process(es) are waiting for read-only access.");
					System.err.println("Loading latest version of user profile.");
					UserProfile userProfile = null;
					try {
						userProfile = (UserProfile) versionManager.get();
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.err.println("Notifying " + readOnlyQueue.size() + " processes that newest profile is ready.");
					while (!readOnlyQueue.isEmpty()) {
						QueueEntry readOnly = readOnlyQueue.poll();
						readOnly.setUserProfile(userProfile);
					}
				} else {
					// a process wants to modify
					modifying = modifyQueue.poll();

					System.err.println("Process " + modifying.getPid() + " is waiting to make profile modifications.");

					UserProfile userProfile = null;
					System.err.println("Loading latest version of user profile for process " + modifying.getPid() + " to modify.");
					try {
						userProfile = (UserProfile) versionManager.get();
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					modifying.setUserProfile(userProfile);

					int counter = 0;
					long sleepTime = MAX_MODIFICATION_TIME / 10;
					while (counter < 10 && !modifying.isReadyToPut() && !modifying.isAborted()) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// ignore
						} finally {
							counter++;
						}
					}

					if (modifying.isReadyToPut()) {
						System.err.println("Process " + modifying.getPid() + " made modifcations and uploads them now.");
						try {
							// put updated user profile version into network
							versionManager.put(userProfile, protectionKeys);
							modifying.notifyPut();

							// notify all read only processes with newest version
							while (!readOnlyQueue.isEmpty()) {
								QueueEntry readOnly = readOnlyQueue.poll();
								readOnly.setUserProfile(userProfile);
							}
						} catch (PutFailedException e) {
							modifying.setPutError(e);
							modifying.notifyPut();
						}
					} else if (!modifying.isAborted()) {
						System.err.println("Process " + modifying.getPid() + " never finished doing modifications. Abort the put request.");
						modifying.abort();
						modifying.setPutError(new PutFailedException(String.format(
								"Too long modification. Only %s ms are allowed.", MAX_MODIFICATION_TIME)));
						modifying.notifyPut();
					}
				}
			}

			System.err.println("Queue worker stopped. user id = '" + credentials.getUserId() + "'");
		}

	}
}