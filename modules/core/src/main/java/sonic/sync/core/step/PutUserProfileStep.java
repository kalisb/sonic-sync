package sonic.sync.core.step;

import java.io.IOException;
import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.data.DataManager;
import sonic.sync.core.security.EncryptionUtil;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.PasswordUtil;
import sonic.sync.core.util.Constants;

public class PutUserProfileStep implements IStep {

	private final static SSLogger logger = SSLoggerFactory.getLogger(PutUserProfileStep.class);

	protected UserProfile userProfile;
	private final UserCredentials credentials;
	private final LoginProcess context;

	public PutUserProfileStep(UserProfile profile, UserCredentials credentials, LoginProcess nextStep) {
		this.userProfile = profile;
		this.credentials = credentials;
		this.context = nextStep;
	}

	@Override
	public void execute() {
		logger.debug("Encrypting UserProfile with 256bit AES key from password");
		try {
			SecretKey encryptionKey = PasswordUtil.generateAESKeyFromPassword(credentials.getPassword(),
					credentials.getPin(), Constants.KEYLENGTH_USER_PROFILE);
			EncryptedNetworkContent encryptedUserProfile = EncryptionUtil.encryptAES(userProfile,
					encryptionKey);
			logger.debug("Putting UserProfile into the DHT");
			encryptedUserProfile.generateVersionKey(context.getNetworkManager());
			//userProfile.setProtectionKeys(encryptionKey);
			put(credentials.getProfileLocationKey(), Constants.USER_PROFILE, encryptedUserProfile,
					encryptionKey);
		} catch (IOException | DataLengthException | IllegalStateException e) {
			logger.error("Cannot encrypt the user profile.");
		} catch (PutFailedException e) {
		} catch (InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void put(String locationKey, String contentKey, EncryptedNetworkContent content,
			SecretKey encryptionKey) throws PutFailedException {

		DataManager dataManager = context.getNetworkManager().getDataManager();
		if (dataManager == null) {
			throw new PutFailedException("Node is not connected");
		}
		try {
			System.out.println("Size: " + encryptionKey.getEncoded().length);
			dataManager.put(contentKey, content, encryptionKey);
			context.getNetworkManager().getDataManager().setUserProfileKey(encryptionKey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
