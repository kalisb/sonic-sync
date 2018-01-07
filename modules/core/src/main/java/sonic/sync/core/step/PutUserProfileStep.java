package sonic.sync.core.step;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.PutFailedException;
import sonic.sync.core.file.RegisterProcessContext;
import sonic.sync.core.libtorrent.Sha1Hash;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.security.DataManager;
import sonic.sync.core.security.DefaultEncryption;
import sonic.sync.core.util.Constants;

public class PutUserProfileStep implements IStep {

	private RegisterProcessContext context;
	private DataManager dataManager;
	private Parameters parameters;

	public PutUserProfileStep(RegisterProcessContext context, DataManager dataManager) {
		this.context = context;
		this.dataManager = dataManager;
	}

	@Override
	public void execute() {
		try {
			System.err.println("Start encrypting the user profile of the new user " + context.consumeUserId());
			EncryptedNetworkContent encrypted = dataManager.getEncryption().encryptAES(context.consumeUserProfile(),
					context.consumeUserProfileEncryptionKeys());
			encrypted.generateVersionKey();
			System.err.println("User profile successfully encrypted. Start putting it...");
			put(context.consumeUserProflieLocationKey(), Constants.USER_PROFILE, encrypted,
					context.consumeUserProfileProtectionKeys());
			return;
		} catch (IllegalStateException ex) {
			System.err.println("Cannot encrypt the user profile of the new user " + context.consumeUserId());
			System.err.println("Cannot encrypt the user profile.");
			return;
		} catch (PutFailedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	protected void put(String locationKey, String contentKey, BaseNetworkContent content, KeyPair protectionKeys)
			throws PutFailedException {
		Parameters params = new Parameters().setLocationKey(locationKey).setContentKey(contentKey)
				.setNetworkContent(content).setProtectionKeys(protectionKeys);
		put(params);
	}
	protected Sha1Hash put(Parameters parameters) throws PutFailedException {
		// store for roll back
		this.parameters = parameters;

		return dataManager.put(parameters);
	}

}
