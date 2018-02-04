package sonic.sync.core.security;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.zeromq.ZContext;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.serializer.ISerialize;

public class DataManager {
	private final ISerialize serializer;
	private final IEncryption encryption;
	private final SessionManager sessionManager;

	public DataManager(ZContext context, ISerialize serializer, IEncryption encryption, SessionManager sessionManager) {
		this.serializer = serializer;
		this.encryption = encryption;
		this.sessionManager = sessionManager;
	}

	public ISerialize getSerializer() {
		return serializer;
	}

	public IEncryption getEncryption() {
		return encryption;
	}

	public BaseNetworkContent get(String profileLocationKey) throws ClassNotFoundException, IOException {
		Entry entry = sessionManager.dhtGetItem(new Sha1Hash(profileLocationKey), 600);
		return (BaseNetworkContent) serializer.deserialize(entry.bencode());
	}

	public Sha1Hash put(Parameters parameters) {
		return putUnblocked(parameters);
	}

	private Sha1Hash putUnblocked(Parameters parameters) {
		// serialize with custom serializer (TomP2P would use Java serializer)
		Entry entry = new Entry(0);
		Entry dataToSend = null;
		try {
			byte[] data = serializer.serialize(parameters.getNetworkContent());
			dataToSend = entry.bdecode(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Sha1Hash sha = sessionManager.dhtPutItem(dataToSend);
	    final CountDownLatch signal = new CountDownLatch(1);
		try {
			signal.await(50, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sha;
	}

}
