package sonic.sync.core.step;

import java.security.PublicKey;
import java.util.Map;

import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.message.Session;
import sonic.sync.core.network.DownloadManager;
import sonic.sync.core.security.LoginProcessContext;
import sonic.sync.core.security.PersistentMetaData;
import sonic.sync.core.security.PublicKeyManager;
import sonic.sync.core.security.SessionParameters;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.security.UserProfileManager;
import sonic.sync.core.util.FileUtil;

public class SessionCreationStep implements IStep {

	private final LoginProcessContext context;
	private final NetworkManager networkManager;

	public SessionCreationStep(LoginProcessContext context, NetworkManager networkManager) {
		this.context = context;
		this.networkManager = networkManager;
	}

	@Override
	public void execute() {
		SessionParameters params = context.consumeSessionParameters();

		try {
			// create user profile manager
			UserProfileManager userProfileManager = new UserProfileManager(networkManager.getDataManager(),
					context.consumeUserCredentials());
			params.setUserProfileManager(userProfileManager);

			// load user profile
			//UserProfile userProfile = userProfileManager.readUserProfile();
			// get the persistently cached items
			PersistentMetaData metaData = FileUtil.readPersistentMetaData(params.getFileAgent(), networkManager
					.getDataManager().getSerializer());

			// create the key manager
			//PublicKeyManager keyManager = new PublicKeyManager(userProfile.getUserId(), userProfile.getEncryptionKeys(),
			//		userProfile.getProtectionKeys(), networkManager.getDataManager());

			// read eventually cached keys and add them to the key manager
			Map<String, PublicKey> publicKeyCache = metaData.getPublicKeyCache();
			//for (String userId : publicKeyCache.keySet()) {
			//	keyManager.putPublicKey(userId, publicKeyCache.get(userId));
			//}
			//params.setKeyManager(keyManager);

			// create the download manager
			DownloadManager downloadManager = networkManager.getDownloadManager();
			params.setDownloadManager(downloadManager);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// create session
		Session session = new Session(params);
		networkManager.setSession(session);
	}

}
