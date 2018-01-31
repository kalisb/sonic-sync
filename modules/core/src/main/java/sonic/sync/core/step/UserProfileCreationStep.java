package sonic.sync.core.step;

import java.security.KeyPair;

import sonic.sync.core.file.RegisterProcessContext;
import sonic.sync.core.security.IEncryption;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.Constants;

public class UserProfileCreationStep implements IStep {

	private final RegisterProcessContext context;
	private final IEncryption encryption;

	public UserProfileCreationStep(RegisterProcessContext context, IEncryption encryption) {
		this.encryption = encryption;
		this.context = context;
	}

	@Override
	public void execute() {
		String userId = context.consumeUserId();
		System.err.println("Creating new user profile. user id ='" + userId + "'");

		// generate keys
		KeyPair encryptionKeys = encryption.generateRSAKeyPair(Constants.KEYLENGTH_USER_KEYS);
		KeyPair protectionKeys = encryption.generateRSAKeyPair(Constants.KEYLENGTH_PROTECTION);
		context.provideUserProfile(new UserProfile(userId, encryptionKeys, protectionKeys));
	}

}
