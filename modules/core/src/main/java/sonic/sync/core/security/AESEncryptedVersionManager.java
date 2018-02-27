package sonic.sync.core.security;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.data.DataManager;
import sonic.sync.core.network.data.NetworkContent;

public class AESEncryptedVersionManager<T extends BaseNetworkContent> {

	private final IEncryption encryption;
	private final SecretKey encryptionKey;
	private DataManager dataManager;
	private String locationKey;
	private String contentKey;
	private Cache<EncryptedNetworkContent> encryptedContentCache = new Cache<EncryptedNetworkContent>();
	private static List<String> keys = new ArrayList<>();

	public AESEncryptedVersionManager(DataManager dataManager, SecretKey encryptionKey, String locationKey, String contentKey) {
		this(dataManager, dataManager.getEncryption(), encryptionKey, locationKey, contentKey);
	}

	public AESEncryptedVersionManager(DataManager dataManager, IEncryption encryption, SecretKey encryptionKey,
			String locationKey, String contentKey) {
		this.dataManager = dataManager;
		this.locationKey = locationKey;
		this.contentKey = contentKey;
		this.encryption = encryption;
		this.encryptionKey = encryptionKey;
	}

	public BaseNetworkContent get() throws ClassNotFoundException, IOException {
		BaseNetworkContent entry = dataManager.get(new Parameters().setContentKey(contentKey).setLocationKey(locationKey));
		return entry;
	}

	public void put(NetworkContent networkContent, KeyPair protectionKeys) throws PutFailedException {
		EncryptedNetworkContent encrypted = encryption.encryptAES(networkContent, encryptionKey);
		/*	encrypted.setBasedOnKey(networkContent.getBasedOnKey());
		encrypted.setVersionKey(networkContent.getVersionKey());
		encrypted.generateVersionKey();*/

		Parameters parameters = new Parameters().setLocationKey(locationKey)
				.setContentKey(contentKey).setNetworkContent(encrypted);

		Sha1Hash code = dataManager.put(parameters);
		System.out.println("key:" + code);
		keys.add(code.toString());
	}

}
