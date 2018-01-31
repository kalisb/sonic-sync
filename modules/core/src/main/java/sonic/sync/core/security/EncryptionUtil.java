package sonic.sync.core.security;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import sonic.sync.core.network.EncryptedNetworkContent;

public class EncryptionUtil {

	private static final Logger logger = Logger.getLogger("EncryptionUtil.class");

	private static final BigInteger RSA_PUBLIC_EXP = new BigInteger("10001", 16);

	private static final int IV_LENGTH = 16;

	public enum RSA_KEYLENGTH {
		BIT_512(512),
		BIT_1024(1024),
		BIT_2048(2048),
		BIT_4096(4096);

		private final int bitLength;

		RSA_KEYLENGTH(int length) {
			bitLength = length;
		}

		public int value() {
			return bitLength;
		}
	}

	private EncryptionUtil() {
	}

	public static byte[] generateIV() {
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[IV_LENGTH];
		do {
			random.nextBytes(iv);
		} while (iv[0] == 0);
		return iv;
	}

	public static String byteToHex(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static EncryptedNetworkContent encryptAES(UserProfile userProfile, SecretKey encryptionKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public static KeyPair generateRSAKeyPair(String keylengthMetaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	public static KeyPair generateRSAKeyPair(RSA_KEYLENGTH keyLength, String securityProvider) {
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", securityProvider);
			RSAKeyGenParameterSpec params = new RSAKeyGenParameterSpec(keyLength.value(), RSA_PUBLIC_EXP);
			gen.initialize(params, new SecureRandom());
			return gen.generateKeyPair();
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
			logger.severe("Exception while generation of RSA key pair of length :" + keyLength);
			logger.severe(e.getMessage());
		}
		return null;
	}
}
