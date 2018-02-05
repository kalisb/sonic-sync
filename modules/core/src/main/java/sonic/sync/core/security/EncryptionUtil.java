package sonic.sync.core.security;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sonic.sync.core.network.EncryptedNetworkContent;

public class EncryptionUtil {

	private static final Logger logger = Logger.getLogger("EncryptionUtil.class");

	private static final BigInteger RSA_PUBLIC_EXP = new BigInteger("10001", 16);

	private static final int IV_LENGTH = 16;

	public enum AES_KEYLENGTH {
		BIT_128(128),
		BIT_192(192),
		BIT_256(256);

		private final int bitLength;

		AES_KEYLENGTH(int length) {
			bitLength = length;
		}

		public int value() {
			return bitLength;
		}
	}

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

	public static byte[] encryptAES(byte[] data, SecretKey secretKey, byte[] initVector, String securityProvider,
			IStrongAESEncryption strongAES) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		int keySize = secretKey.getEncoded().length * 8;
		if (Cipher.getMaxAllowedKeyLength("AES") >= keySize) {
			return processAESCiphering(true, data, secretKey, initVector, securityProvider);
		} else {
			return strongAES.encryptStrongAES(data, secretKey, initVector);
		}
	}

	public static byte[] decryptAES(byte[] data, SecretKey secretKey, byte[] initVector, String securityProvider,
			IStrongAESEncryption strongAES) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		int keySize = secretKey.getEncoded().length * 8;
		if (Cipher.getMaxAllowedKeyLength("AES") >= keySize) {
			return processAESCiphering(false, data, secretKey, initVector, securityProvider);
		} else {
			return strongAES.decryptStrongAES(data, secretKey, initVector);
		}
	}

	private static byte[] processAESCiphering(boolean forEncrypting, byte[] data, SecretKey key, byte[] initVector,
			String securityProvider) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		IvParameterSpec ivSpec = new IvParameterSpec(initVector);
		SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", securityProvider);
		int encryptMode = forEncrypting ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
		cipher.init(encryptMode, keySpec, ivSpec);

		// process ciphering
		byte[] output = new byte[cipher.getOutputSize(data.length)];

		int bytesProcessed1 = cipher.update(data, 0, data.length, output, 0);
		int bytesProcessed2 = cipher.doFinal(output, bytesProcessed1);

		byte[] result = new byte[bytesProcessed1 + bytesProcessed2];
		System.arraycopy(output, 0, result, 0, result.length);
		return result;
	}
}
