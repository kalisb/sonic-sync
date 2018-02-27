package sonic.sync.core.network.message;

import java.io.File;
import java.security.KeyPair;

import sonic.sync.core.network.DownloadManager;
import sonic.sync.core.network.data.PublicKeyManager;
import sonic.sync.core.security.LocationsManager;
import sonic.sync.core.security.SessionParameters;
import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserProfileManager;

public class Session {


	private final UserProfileManager profileManager;
	//private final LocationsManager locationsManager;
	//private final PublicKeyManager keyManager;
//	private final DownloadManager downloadManager;

	public Session(SessionParameters params) {
		this.profileManager = params.getProfileManager();
		//this.fileAgent = (FileAgent) params.getFileAgent();
	}

	public UserProfileManager getProfileManager() {
		return profileManager;
	}

	/*public LocationsManager getLocationsManager() {
		return locationsManager;
	}*/

	public UserCredentials getCredentials() {
		return profileManager.getUserCredentials();
	}

	/**
	 * Returns the own encryption key pair
	 */
	/*public KeyPair getKeyPair() {
		return keyManager.getOwnKeyPair();
	}*/

	public File getRootFile() {
		return null;//getFileAgent().getRoot();
	}

	public String getUserId() {
		return getCredentials().getUserId();
	}

	/**
	 * Get the public key manger to get public keys from other users. A get call may block (if public key not
	 * cached).
	 * 
	 * @return a public key manager
	 */
/*	public PublicKeyManager getKeyManager() {
		return keyManager;
	}*/

	/**
	 * Returns the download manager, responsible for downloading chunks
	 * 
	 * @return the download manager
	 */
	/*public DownloadManager getDownloadManager() {
		return downloadManager;
	}*/
}
