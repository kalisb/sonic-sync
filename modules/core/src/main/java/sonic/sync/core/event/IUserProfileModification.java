package sonic.sync.core.event;

import sonic.sync.core.exception.VersionForkAfterPutException;
import sonic.sync.core.security.UserProfile;

public interface IUserProfileModification {

	void modifyUserProfile(UserProfile profile);

}
