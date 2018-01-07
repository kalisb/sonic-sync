package sonic.sync.core.security;

import sonic.sync.core.configuration.ConsoleFileAgent;
import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.ProcessExecutionException;
import sonic.sync.core.file.RegisterProcessContext;
import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.message.SyncProcess;
import sonic.sync.core.step.CheckIsUserRegisteredStep;
import sonic.sync.core.step.ContactOtherClientsStep;
import sonic.sync.core.step.GetLocationsStep;
import sonic.sync.core.step.PutLocationsStep;
import sonic.sync.core.step.PutUserProfileStep;
import sonic.sync.core.step.SessionCreationStep;
import sonic.sync.core.step.UserProfileCreationStep;
import sonic.sync.core.util.Constants;

public class UserManager {

	private final NetworkManager networkManager;

	public UserManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void executeLogin(UserCredentials credentials, ConsoleFileAgent fileAgent) {
		SessionParameters params = new SessionParameters(fileAgent);
		LoginProcessContext context = new LoginProcessContext(credentials, params);

		// process composition
		SyncProcess process = new SyncProcess();

		process.add(new SessionCreationStep(context, networkManager));
		process.add(new GetLocationsStep(context, networkManager));
		process.add(new ContactOtherClientsStep(context, networkManager));
		process.add(new PutLocationsStep(context, networkManager));

		process.execute();

	}

	public void executeLogout() {
		// TODO Auto-generated method stub

	}

	public boolean isRegistered(String userId) {
		return networkManager.getDataManager().get(
				new Parameters().setLocationKey(userId).setContentKey(Constants.USER_LOCATIONS)) != null;
	}

	public void executeRegisterProcess(UserCredentials credentials) throws ProcessExecutionException {
		DataManager dataManager = networkManager.getDataManager();
		RegisterProcessContext context = new RegisterProcessContext(credentials);

		// process composition
		SyncProcess process = new SyncProcess();

		process.add(new CheckIsUserRegisteredStep(context, dataManager));
		process.add(new UserProfileCreationStep(context, networkManager.getEncryption()));
		process.add(new PutUserProfileStep(context, dataManager));
		//process.add(new AsyncComponent<>(new org.hive2hive.core.processes.register.PutLocationsStep(context, dataManager)));
		//process.add(new AsyncComponent<>(new PutPublicKeyStep(context, dataManager)));
		process.execute();
	}

}