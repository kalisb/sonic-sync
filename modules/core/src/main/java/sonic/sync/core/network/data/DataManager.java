package sonic.sync.core.network.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.zeromq.ZContext;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtPutAlert;
import com.frostwire.jlibtorrent.swig.entry.data_type;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.security.IEncryption;
import sonic.sync.core.serializer.ISerialize;
import sonic.sync.core.util.Constants;

public class DataManager {
	private final ISerialize serializer;
	private final IEncryption encryption;
	private final SessionManager sessionManager;
	private static List<Sha1Hash> userKey;

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

	public List<Sha1Hash> put(Parameters parameters) {
		return putUnblocked(parameters);
	}

	private List<Sha1Hash> putUnblocked(Parameters parameters) {
		List<Sha1Hash> shas = new ArrayList<>();
		try {
			byte[] data = serializer.serialize(parameters.getNetworkContent());
			String encodeToString = Base64.getEncoder().encodeToString(data);
			for (int j = 0; j < encodeToString.length(); j+= 500) {
				Entry entry = new Entry(encodeToString.substring(j, 
						j + 500 > encodeToString.length() ? encodeToString.length() : j + 500));
				Sha1Hash sha = sessionManager.dhtPutItem(entry);
				shas.add(sha);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final CountDownLatch signal = new CountDownLatch(1);
		try {
			signal.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return shas;
	}

	public NetworkContent get(Parameters parameters) throws ClassNotFoundException, IOException {
		Sha1Hash sha  = new Sha1Hash(parameters.getContentKey());
		Entry entry = sessionManager.dhtGetItem(sha, 150);
		StringBuilder sb = new StringBuilder();
		for (Entry encoded :  entry.dictionary().values()) {
			sb.append(encoded.string());
		}
		return (NetworkContent) serializer.deserialize(Base64.getDecoder().decode(
				sb.toString()
				));
	}


	public void setUserProfileKey(List<Sha1Hash> hash) {
		this.userKey = hash;
	}

	public List<Sha1Hash> getUserProfileKey() {
		return userKey;
	}

	public NetworkContent get(List<Sha1Hash> list) throws ClassNotFoundException, IOException {
		StringBuilder sb = new StringBuilder();
		for (Sha1Hash sha : list) {
			Entry entry = sessionManager.dhtGetItem(sha, 150);
			sb.append(entry.string());	
		}
		return (NetworkContent) serializer.deserialize(Base64.getDecoder().decode(sb.toString()));
	}

}
