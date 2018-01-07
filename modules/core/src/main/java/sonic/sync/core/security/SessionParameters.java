package sonic.sync.core.security;

import sonic.sync.core.configuration.ConsoleFileAgent;
import sonic.sync.core.file.FileAgent;
import sonic.sync.core.network.DownloadManager;

public class SessionParameters {
	
	private UserProfileManager profileManager;
	private ConsoleFileAgent fileAgent;

	public SessionParameters(ConsoleFileAgent fileAgent) {
		this.fileAgent = fileAgent;
	}

	public void setUserProfileManager(UserProfileManager userProfileManager) {
		this.profileManager = userProfileManager;
	}

	public void setLocationsManager(LocationsManager locationsManager) {
		// TODO Auto-generated method stub
		
	}

	public void setKeyManager(PublicKeyManager keyManager) {
		// TODO Auto-generated method stub
		
	}

	public ConsoleFileAgent getFileAgent() {
		return fileAgent;
	}

	public void setDownloadManager(DownloadManager downloadManager) {
		// TODO Auto-generated method stub
		
	}

	public UserProfileManager getProfileManager() {
		return profileManager;
	}

	public LocationsManager getLocationsManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public PublicKeyManager getKeyManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public DownloadManager getDownloadManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
