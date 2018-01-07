package sonic.sync.core.security;

import java.io.IOException;

import org.zeromq.ZContext;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.libtorrent.Entry;
import sonic.sync.core.libtorrent.SessionManager;
import sonic.sync.core.libtorrent.Sha1Hash;
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

	public BaseNetworkContent get(Parameters parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	public IEncryption getEncryption() {
		return encryption;
	}

	public BaseNetworkContent get(String profileLocationKey, String userProfile) throws ClassNotFoundException, IOException {
		Entry entry = sessionManager.dhtGetItem(new Sha1Hash(profileLocationKey), 600);
		return (BaseNetworkContent) serializer.deserialize(entry.bencode());
	}

	public boolean put(Object profileLocationKey, String userProfile, EncryptedNetworkContent encryptedUserProfile,
			Object protectionKeys) {
		// TODO Auto-generated method stub
		return false;
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

		return sessionManager.dhtPutItem(dataToSend);
	}

}
