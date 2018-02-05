package sonic.sync.core.util;

import java.math.BigInteger;

import sonic.sync.core.security.EncryptionUtil.AES_KEYLENGTH;
import sonic.sync.core.security.EncryptionUtil.RSA_KEYLENGTH;

public interface Constants {
	static final BigInteger MEGABYTES = BigInteger.valueOf(1024 * 1024);
	public static final BigInteger DEFAULT_MAX_FILE_SIZE = BigInteger.valueOf(250).multiply(MEGABYTES);// 250 MB
	public static final int DEFAULT_CHUNK_SIZE = MEGABYTES.intValue(); // 1 MB
	static final int PORT = 6881;
	public static final String USER_LOCATIONS = "USER_LOCATIONS";
	// maximum wait time until any network operation should be answered by the other peer (for each retry).
	// This just serves as a fallback against infinite blocking when all other mechanisms fail.
	public static final int AWAIT_NETWORK_OPERATION_MS = 2;
	// maximum delay to wait until peers have time to answer until they get removed from the locations
	public static final int CONTACT_PEERS_AWAIT_MS = 10000;
	// Slow peers need to have more time since they may be dependent on buffered relaying
	public static final int CONTACT_SLOW_PEERS_AWAIT_MS = 30000;
	static final AES_KEYLENGTH KEYLENGTH_USER_PROFILE = AES_KEYLENGTH.BIT_256;
	static final int PUT_RETRIES = 2;
	static final String USER_PROFILE = "USER_PROFILE";
	static final String KEYLENGTH_META_DOCUMENT = null;
	static final String META_DOCUMENT = null;
	static final String LARGE_FILE_UPDATE = null;
	static final RSA_KEYLENGTH KEYLENGTH_USER_KEYS = RSA_KEYLENGTH.BIT_2048;
	static final RSA_KEYLENGTH KEYLENGTH_PROTECTION = RSA_KEYLENGTH.BIT_1024;
	static final String META_FILE_NAME = "ss.conf";
}
