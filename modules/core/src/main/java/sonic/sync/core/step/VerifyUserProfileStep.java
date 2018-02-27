package sonic.sync.core.step;

import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.security.UserProfile;

public class VerifyUserProfileStep implements IStep {

	private String userId;
	private LoginProcess context;

	public VerifyUserProfileStep(String userId, LoginProcess context) {
		this.userId = userId;
		this.context = context;
	}

	@Override
	public void execute() {
		// get the loaded profile from the process context
		UserProfile loadedProfile = context.getUserProfile();
	}

}
