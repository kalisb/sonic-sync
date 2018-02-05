package sonic.sync.core.step;

import java.io.IOException;
import java.security.PublicKey;

import sonic.sync.core.configuration.Parameters;
import sonic.sync.core.exception.InvalidProcessStateException;
import sonic.sync.core.file.RegisterProcessContext;
import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.security.DataManager;
import sonic.sync.core.security.DefaultEncryption;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.util.Constants;

public class CheckIsUserRegisteredStep implements IStep {

	private RegisterProcessContext context;
	private DataManager dataManager;

	public CheckIsUserRegisteredStep(RegisterProcessContext context, DataManager dataManager) {
		this.context = context;
		this.dataManager = dataManager;
	}

	@Override
	public void execute() {
		String userId = context.consumeUserId();
		System.err.println("Checking if user is already registerd. user id ='" + userId + "'");
		try {
			if (get(context.consumeUserId(), Constants.USER_LOCATIONS) != null) {
				System.err.println("The user '" + userId + "' is already registered and cannot be registered again.");
			}
		} catch (InvalidProcessStateException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected BaseNetworkContent get(PublicKey locationKey, String contentKey) throws InvalidProcessStateException {
		return get(DefaultEncryption.key2String(locationKey), contentKey);
	}

	protected BaseNetworkContent get(String locationKey, String contentKey) throws InvalidProcessStateException, ClassNotFoundException, IOException {
		return dataManager.get(new Parameters().setContentKey(contentKey).setLocationKey(locationKey));
	}

}
