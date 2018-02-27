package sonic.sync.core.step;

import java.io.IOException;
import java.security.KeyPair;

import javax.crypto.SecretKey;

import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.configuration.Locations;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.data.DataManager;
import sonic.sync.core.network.data.NetworkContent;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.util.Constants;

public class GetLocationsStep implements IStep {

	private final static SSLogger logger = SSLoggerFactory.getLogger(GetLocationsStep.class);

	private final String userId;
	private final LoginProcess context;

	public GetLocationsStep(LoginProcess context, String userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void execute() {
		logger.debug("Get the locations for user '" + userId + "'.");
		NetworkContent content = null;
		try {
			SecretKey userProfileKey = context.getNetworkManager().getDataManager().getUserProfileKey();
			if (userProfileKey != null) {
				content = get(userProfileKey, userId);
			}
		
			if (content == null) {
				logger.warn("Did not find the locations.");
				context.setLocations(null);
			} else {
				context.setLocations((Locations) content);
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected NetworkContent get(SecretKey userProfileKey, String contentKey) throws ClassNotFoundException, IOException {
		DataManager dataManager = context.getNetworkManager().getDataManager();
		return dataManager.get(userProfileKey, contentKey);
	}

}
