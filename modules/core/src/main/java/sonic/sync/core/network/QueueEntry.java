package sonic.sync.core.network;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.Constants;

public class QueueEntry {

	private final CountDownLatch getWaiter = new CountDownLatch(1);

	// got from DHT
	private UserProfile userProfile;
	private GetFailedException getFailedException;

	public void setGetError(GetFailedException error) {
		this.getFailedException = error;
		getWaiter.countDown();
	}

	/**
	 * Returns the user profile (blocking) as soon as it's ready
	 * 
	 * @return the user profile
	 */
	public UserProfile getUserProfile() throws GetFailedException {
		
		if (getFailedException != null) {
			// exception already here, don't even wait
			throw getFailedException;
		}

		try {
			boolean success = getWaiter.await(Constants.AWAIT_NETWORK_OPERATION_MS, TimeUnit.MINUTES);
			if (!success) {
				throw new GetFailedException("Could not wait for getting the user profile");
			}
		} catch (InterruptedException e) {
			throw new GetFailedException("Could not wait for getting the user profile.");
		}

		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
		getWaiter.countDown();
	}
}