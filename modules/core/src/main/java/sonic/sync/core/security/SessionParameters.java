package sonic.sync.core.security;

import java.security.KeyPair;

import sonic.sync.core.file.FileManager;

public class SessionParameters {
	private KeyPair keyPair;
	private UserProfileManager profileManager;
	private FileManager fileManager;

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public UserProfileManager getProfileManager() {
		return profileManager;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void setProfileManager(UserProfileManager profileManager) {
		this.profileManager = profileManager;
	}

}
