package sonic.sync.core.network;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.util.Constants;

public class PutQueueEntry extends QueueEntry {

	private final String pid;
	private final AtomicBoolean readyToPut = new AtomicBoolean(false);
	private final AtomicBoolean abort = new AtomicBoolean(false);
	private final CountDownLatch putWaiter = new CountDownLatch(1);

	private PutFailedException putFailedException;

	public PutQueueEntry(String pid) {
		this.pid = pid;
	}

	public String getPid() {
		return pid;
	}

	public boolean isReadyToPut() {
		return readyToPut.get();
	}

	public void readyToPut() {
		readyToPut.set(true);
	}

	public boolean isAborted() {
		return abort.get();
	}

	public void abort() {
		abort.set(true);
	}

	public void notifyPut() {
		putWaiter.countDown();
	}

	public void waitForPut() throws PutFailedException {
		if (putFailedException != null) {
			throw putFailedException;
		}

		try {
			boolean success = putWaiter.await(Constants.AWAIT_NETWORK_OPERATION_MS * Constants.PUT_RETRIES,
					TimeUnit.MILLISECONDS);
			if (!success) {
				putFailedException = new PutFailedException("Timeout while putting occurred");
			}
		} catch (InterruptedException e) {
			putFailedException = new PutFailedException("Could not wait to put the user profile");
		}

		if (putFailedException != null) {
			throw putFailedException;
		}
	}

	public void setPutError(PutFailedException error) {
		this.putFailedException = error;
	}

	@Override
	public int hashCode() {
		return getPid().hashCode();
	}

	@Override
	public boolean equals(Object otherPid) {
		if (otherPid == null) {
			return false;
		} else if (otherPid instanceof String) {
			String pidString = (String) otherPid;
			return getPid().equals(pidString);
		} else if (otherPid instanceof PutQueueEntry) {
			PutQueueEntry otherEntry = (PutQueueEntry) otherPid;
			return getPid().equals(otherEntry.getPid());
		}
		return false;
	}
}