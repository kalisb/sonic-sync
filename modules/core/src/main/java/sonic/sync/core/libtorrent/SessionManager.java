package sonic.sync.core.libtorrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import sonic.sync.core.libtorrent.swig.*;

public class SessionManager {

	private volatile session session;

	public Entry dhtGetItem(Sha1Hash sha1, int timeout) {
		if (session == null) {
			return null;
		}

		final sha1_hash target = sha1.swig();
		final Entry[] result = {null};
		final CountDownLatch signal = new CountDownLatch(1);


		try {

			session.dht_get_item(target);

			signal.await(timeout, TimeUnit.SECONDS);

		} catch (Throwable e) {
			System.err.println("Error getting immutable item");
			e.printStackTrace();
		} finally {
		}

		return result[0];
	}

	public void startDht() {
		// TODO Auto-generated method stub

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public Sha1Hash dhtPutItem(Entry dataToSend) {
		// TODO Auto-generated method stub
		return null;
	}

}
