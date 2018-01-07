package sonic.sync.core.security;

import sonic.sync.core.configuration.Locations;

public class LoginProcessContext {

	private UserCredentials credentials;
	private SessionParameters params;
	
	private Locations locations;

	public LoginProcessContext(UserCredentials credentials, SessionParameters params) {
		this.credentials = credentials;
		this.params = params;
	}

	public SessionParameters consumeSessionParameters() {
		return params;
	}

	public UserCredentials consumeUserCredentials() {
		return credentials;
	}

	public String consumeUserId() {
		return credentials.getUserId();
	}

	public Locations consumeLocations() {
		return locations;
	}

	public void provideLocations(Locations locations) {
		this.locations = locations;
	}

}
