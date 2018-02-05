package sonic.sync.core.step;

import java.security.KeyPair;
import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.file.RegisterProcessContext;
import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.security.DataManager;
import sonic.sync.core.util.Constants;

public class PutUserProfileStep implements IStep {

	private RegisterProcessContext context;
	private DataManager dataManager;
	private NetworkManager networkManager;
	private Parameters parameters;

	public PutUserProfileStep(RegisterProcessContext context, DataManager dataManager, NetworkManager networkManager) {
		this.context = context;
		this.dataManager = dataManager;
		this.networkManager = networkManager;
	}

	@Override
	public void execute() {
		try {
			System.err.println("Start encrypting the user profile of the new user " + context.consumeUserId());
			EncryptedNetworkContent encrypted = dataManager.getEncryption().encryptAES(context.consumeUserProfile(),
					context.consumeUserProfileEncryptionKeys());
			System.err.println("User profile successfully encrypted. Start putting it...");
			put(context.consumeUserProflieLocationKey(), Constants.USER_PROFILE, encrypted,
					context.consumeUserProfileProtectionKeys());
			return;
		} catch (IllegalStateException ex) {
			System.err.println("Cannot encrypt the user profile of the new user " + context.consumeUserId());
			System.err.println("Cannot encrypt the user profile.");
			return;
		}
	}

	private void put(String consumeUserProflieLocationKey, String userProfile, EncryptedNetworkContent encrypted,
			KeyPair consumeUserProfileProtectionKeys) {
		dataManager.put(new Parameters().setLocationKey(consumeUserProflieLocationKey).setContentKey(userProfile).setNetworkContent(encrypted));	
	}

}
