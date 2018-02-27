package sonic.sync.core.step;

import sonic.sync.core.network.NetworkManager;
import sonic.sync.core.network.message.Session;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.security.SessionParameters;

public class SessionCreationStep implements IStep {

	private final SessionParameters sessionParams;
	private final LoginProcess context;
	private final NetworkManager networkManager;

	public SessionCreationStep(SessionParameters params, LoginProcess loginProcess) {
		this.sessionParams = params;
		this.networkManager = loginProcess.getNetworkManager();
		this.context = loginProcess;
	}

	@Override
	public void execute() {

		// create session
		sessionParams.setKeyPair(context.getUserProfile().getEncryptionKeys());

		Session session = new Session(sessionParams);
		context.setSession(session);
		networkManager.setSession(session);
	}

}
