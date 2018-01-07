package sonic.sync.core.file;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.Constants;
import sonic.sync.core.util.PasswordUtil;

public final class RegisterProcessContext {

	private final UserCredentials userCredentials;

	private UserProfile profile;

	public RegisterProcessContext(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	public String consumeUserId() {
		return userCredentials.getUserId();
	}

	public String consumeUserProflieLocationKey() {
		return userCredentials.getProfileLocationKey();
	}

	public void provideUserProfile(UserProfile profile) {
		this.profile = profile;
	}

	public UserProfile consumeUserProfile() {
		return profile;
	}

	public KeyPair consumeUserLocationsProtectionKeys() {
		return profile.getProtectionKeys();
	}

	public KeyPair consumeUserProfileProtectionKeys() {
		return profile.getProtectionKeys();
	}

	public KeyPair consumeUserPublicKeyProtectionKeys() {
		return profile.getProtectionKeys();
	}

	public SecretKey consumeUserProfileEncryptionKeys() {
		return PasswordUtil.generateAESKeyFromPassword(userCredentials.getPassword(), userCredentials.getPin(),
				Constants.KEYLENGTH_USER_PROFILE);
	}
}
