package sonic.sync.core.security;

import java.security.PublicKey;

import sonic.sync.core.configuration.Locations;
import sonic.sync.core.network.NetworkManager;
import sonic.sync.core.network.message.Session;

public class LoginProcess {

	private Locations locations;
	private UserProfile userProfile;
	private PublicKey publicKey;
	private NetworkManager networkManager;

	private Session session;

	public LoginProcess(UserCredentials credentials, SessionParameters sessionParameters,
			NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	public void setLocations(Locations locations) {
		this.locations = locations;
	}

	public Locations getLocations() {
		return locations;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

}
