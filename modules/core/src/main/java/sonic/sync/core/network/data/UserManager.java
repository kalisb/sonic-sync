package sonic.sync.core.network.data;

import java.io.IOException;
import sonic.sync.core.configuration.ConsoleFileAgent;
import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.file.FileManager;
import sonic.sync.core.network.NetworkManager;
import sonic.sync.core.network.message.SyncProcess;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.security.SessionParameters;
import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.security.UserProfileManager;
import sonic.sync.core.step.ContactPeersStep;
import sonic.sync.core.step.GetLocationsStep;
import sonic.sync.core.step.GetUserProfileStep;
import sonic.sync.core.step.PutUserProfileStep;
import sonic.sync.core.step.SessionCreationStep;
import sonic.sync.core.step.VerifyUserProfileStep;
import sonic.sync.core.util.Constants;

public class UserManager {

	private final NetworkManager networkManager;

	public UserManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void executeLogin(UserCredentials credentials, ConsoleFileAgent fileAgent) {
		SessionParameters sessionParameters = new SessionParameters();
		sessionParameters.setProfileManager(new UserProfileManager(networkManager, credentials));
		sessionParameters.setFileManager(new FileManager(fileAgent.getRoot().getPath()));
		LoginProcess context = new LoginProcess(credentials, sessionParameters, networkManager);

		// process composition
		SyncProcess process = new SyncProcess();

		process.add(new GetUserProfileStep(credentials, context));
		process.add(new VerifyUserProfileStep(credentials.getUserId(), context));
		process.add(new SessionCreationStep(sessionParameters, context));
		process.add(new GetLocationsStep(context, credentials.getUserId() ));
		process.add(new ContactPeersStep(context));
		
		process.execute();

	}

	public void executeLogout() {
		// TODO Auto-generated method stub

	}

	public void executeRegisterProcess(UserCredentials credentials) {
		LoginProcess context = new LoginProcess(credentials, new SessionParameters(), networkManager);

		context.setNetworkManager(networkManager);
		UserProfile userProfile = new UserProfile(credentials.getUserId());
		// process composition
		SyncProcess process = new SyncProcess();
		process.add(new GetLocationsStep(context, credentials.getUserId()));
		process.add(new PutUserProfileStep(userProfile, credentials, context));
		process.execute();
	}

	public boolean isRegistered(String userId) throws ClassNotFoundException, IOException {
		return networkManager.getDataManager().get(
				new Parameters().setLocationKey(userId).setContentKey(Constants.USER_LOCATIONS)) != null;
	}

}
