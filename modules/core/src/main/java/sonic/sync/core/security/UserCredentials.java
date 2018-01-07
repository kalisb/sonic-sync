package sonic.sync.core.security;

import sonic.sync.core.util.PasswordUtil;

public class UserCredentials {

	private final String userId;
	private final String password;
	private final String pin;

	private final String locationCache;

	public UserCredentials(String userId, String password, String pin) {
		this.userId = userId;
		this.password = password;
		this.pin = pin;
		this.locationCache = calculateLocationCache();
	}

	private String calculateLocationCache() {
		// concatenate PIN + PW + UserId
		String appendage = new StringBuilder().append(pin).append(password).append(userId).toString();

		// create fixed salt based on location
		byte[] fixedSalt = PasswordUtil.generateFixedSalt(appendage.getBytes());

		// hash the location
		byte[] locationKey = PasswordUtil.generateHash(appendage.toCharArray(), fixedSalt);

		// Note: Do this as hex to support all platforms
		return EncryptionUtil.byteToHex(locationKey);
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getPin() {
		return pin;
	}
	
	public String getLocationCache() {
		return locationCache;
	}

	public String getProfileLocationKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
