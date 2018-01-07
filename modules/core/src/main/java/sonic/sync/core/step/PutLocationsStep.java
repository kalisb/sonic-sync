package sonic.sync.core.step;

import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.security.LocationsManager;
import sonic.sync.core.security.LoginProcessContext;

public class PutLocationsStep implements IStep {

	private final NetworkManager networkManager;
	private final LoginProcessContext context;

	public PutLocationsStep(LoginProcessContext context, NetworkManager networkManager) {
		this.networkManager = networkManager;
		this.context = context;
	}

	@Override
	public void execute() {
	/*	LocationsManager locationsManager = null;
		try {
			locationsManager = networkManager.getSession().getLocationsManager();
		} catch (NoSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		locationsManager.put(context.consumeLocations());*/
	}

}
