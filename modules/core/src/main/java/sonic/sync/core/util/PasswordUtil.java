package sonic.sync.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

	public static final int HASH_BIT_SIZE = 192;
	public static final int SALT_BIT_SIZE = HASH_BIT_SIZE;
	
	private static final int PBKDF2_ITERATIONS = 65536;

	public static byte[] generateFixedSalt(byte[] input) {
		try {
			byte[] fixedSalt = new byte[SALT_BIT_SIZE / 8];

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] state = sha.digest(input);
			sha.update(state);

			int offset = 0;
			while (offset < fixedSalt.length) {
				state = sha.digest();

				if (fixedSalt.length - offset > state.length) {
					System.arraycopy(state, 0, fixedSalt, offset, state.length);
				} else {
					System.arraycopy(state, 0, fixedSalt, offset, fixedSalt.length - offset);
				}

				offset += state.length;

				sha.update(state);
			}

			return fixedSalt;

		} catch (NoSuchAlgorithmException e) {
			System.err.println("Exception while generating fixed salt:" + e.getMessage());
		}
		return null;
	}

	public static byte[] generateHash(char[] password, byte[] salt) {
		// hash the password
		return getPBKDF2Hash(password, salt, HASH_BIT_SIZE);
	}

	private static byte[] getPBKDF2Hash(char[] password, byte[] salt, int hashBitSize) {

		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

			KeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, hashBitSize);
			SecretKey secretKey = skf.generateSecret(spec);
			return secretKey.getEncoded();

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.err.println("Error while PBKDF2 key streching:" + e.getMessage());
		}
		return null;
	}

	public static SecretKey generateAESKeyFromPassword(String password, String pin, String keylengthUserProfile) {
		// TODO Auto-generated method stub
		return null;
	}
}
