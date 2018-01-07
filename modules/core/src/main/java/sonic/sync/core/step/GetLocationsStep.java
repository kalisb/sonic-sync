package sonic.sync.core.step;

import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.security.LoginProcessContext;

public class GetLocationsStep implements IStep {

	private final LoginProcessContext context;
	private final NetworkManager networkManager;

	public GetLocationsStep(LoginProcessContext context, NetworkManager networkManager) {
		this.context = context;
		this.networkManager = networkManager;
	}

	@Override
	public void execute() {
	/*	LocationsManager locationsManager = null;
		try {
			locationsManager = networkManager.getSession().getLocationsManager();
		} catch (NoSessionException e) {
			System.err.println(e.getMessage());
		}
		context.provideLocations(locationsManager.get());*/
	}

}
