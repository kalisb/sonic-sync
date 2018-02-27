package sonic.sync.core.step;

import java.io.IOException;
import java.security.KeyPair;

import javax.crypto.SecretKey;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.data.DataManager;
import sonic.sync.core.network.data.NetworkContent;
import sonic.sync.core.security.EncryptionUtil;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.Constants;
import sonic.sync.core.util.PasswordUtil;

public class GetUserProfileStep implements IStep {

	private final static SSLogger logger = SSLoggerFactory.getLogger(GetUserProfileStep.class);

	private final UserCredentials credentials;
	private final LoginProcess context;

	public GetUserProfileStep(UserCredentials credentials, LoginProcess context) {
		this.credentials = credentials;
		this.context = context;
	}

	@Override
	public void execute() {
		NetworkContent content = null;
		try {
			content = get(context.getNetworkManager().getDataManager().getUserProfileKey(), credentials.getUserId());
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (content == null) {
			// could have been intended...
			logger.warn("Did not find user profile.");
		} else {
			EncryptedNetworkContent encrypted = (EncryptedNetworkContent) content;
			logger.debug("Decrypting user profile with 256-bit AES key from password.");

			SecretKey encryptionKey = PasswordUtil.generateAESKeyFromPassword(credentials.getPassword(),
					credentials.getPin(), Constants.KEYLENGTH_USER_PROFILE);
			try {
				NetworkContent decrypted = EncryptionUtil.decryptAES(encrypted, encryptionKey);
				UserProfile userProfile = (UserProfile) decrypted;
				userProfile.setVersionKey(content.getVersionKey());
				userProfile.setBasedOnKey(content.getBasedOnKey());
				context.setUserProfile(userProfile);
			} catch (IOException | ClassNotFoundException | DataLengthException | IllegalStateException | InvalidCipherTextException e) {
				logger.error("Cannot decrypt the user profile.", e);
			}
		}
	}

	protected NetworkContent get(SecretKey secretKey, String contentKey) throws ClassNotFoundException, IOException {
		DataManager dataManager = context.getNetworkManager().getDataManager();
		return dataManager.get(secretKey, contentKey);
	}

}
