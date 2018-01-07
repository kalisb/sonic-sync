package sonic.sync.core.security;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;

public class AESEncryptedVersionManager<T extends BaseNetworkContent> {

	private final IEncryption encryption;
	private final SecretKey encryptionKey;
	private DataManager dataManager;
	private String locationKey;
	private String contentKey;
	private Cache<EncryptedNetworkContent> encryptedContentCache = new Cache<EncryptedNetworkContent>();

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

	public UserProfile get() {
		// TODO Auto-generated method stub
		return null;
	}

	public void put(T networkContent, KeyPair protectionKeys) throws PutFailedException {
		//try {
			EncryptedNetworkContent encrypted = encryption.encryptAES(networkContent, encryptionKey);
		/*	encrypted.setBasedOnKey(networkContent.getBasedOnKey());
			encrypted.setVersionKey(networkContent.getVersionKey());
			encrypted.generateVersionKey();

			Parameters parameters = new Parameters().setLocationKey(this.parameters.getLocationKey())
					.setContentKey(this.parameters.getContentKey()).setVersionKey(encrypted.getVersionKey())
					.setBasedOnKey(encrypted.getBasedOnKey()).setNetworkContent(encrypted).setProtectionKeys(protectionKeys)
					.setTTL(networkContent.getTimeToLive()).setPrepareFlag(true);

			PutStatus status = dataManager.put(parameters);
			if (status.equals(PutStatus.FAILED)) {
				throw new PutFailedException("Put failed.");
			} else if (status.equals(PutStatus.VERSION_FORK)) {
				if (!dataManager.remove(parameters)) {
				}
				throw new VersionForkAfterPutException();
			} else {
				networkContent.setVersionKey(encrypted.getVersionKey());
				networkContent.setBasedOnKey(encrypted.getBasedOnKey());
				// cache digest
				digestCache.put(parameters.getVersionKey(), new HashSet<Number160>(parameters.getData().basedOnSet()));
				// cache network content
				contentCache.put(parameters.getVersionKey(), networkContent);
				// cache encrypted network content
				encryptedContentCache.put(parameters.getVersionKey(), encrypted);
			}
		} catch (GeneralSecurityException | IOException e) {
			throw new PutFailedException(String.format("Cannot encrypt the user profile. reason = '%s'", e.getMessage()));
		}*/
	}

}
