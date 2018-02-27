package sonic.sync.core.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;
import org.bouncycastle.util.encoders.Base64;

import sonic.sync.core.file.PersistentMetaData;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.HybridEncryptedContent;
import sonic.sync.core.network.data.NetworkContent;

public class EncryptionUtil {

	private static final SSLogger logger = SSLoggerFactory.getLogger(EncryptionUtil.class);

	public static final String SINGATURE_ALGORITHM = "SHA1withRSA";
	public static final int IV_LENGTH = 16;

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

	/**
	 * Randomly generates an initialization vector (IV) which can be used as parameter for symmetric
	 * encryption.
	 * 
	 * @return Returns a randomly generated IV.
	 */
	public static byte[] generateIV() {
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[IV_LENGTH];
		random.nextBytes(iv);
		return iv;
	}

	/**
	 * Generates a symmetric AES key of the specified key length.
	 * 
	 * @param keyLength The length the AES key should have.
	 * @return A symmetric AES key of the specified length.
	 */
	public static SecretKey generateAESKey(AES_KEYLENGTH keyLength) {

		installBCProvider();

		try {
			final KeyGenerator kg = KeyGenerator.getInstance("AES", "BC");
			kg.init(keyLength.value(), new SecureRandom());
			byte[] encoded = kg.generateKey().getEncoded();
			return new SecretKeySpec(encoded, "AES");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			logger.error("Exception while AES key generator instance creation:", e);
		}
		return null;

	}

	/**
	 * Generates an asymmetric RSA key pair of the specified key length.
	 * 
	 * @param keyLength The length the RSA keys should have.
	 * @return An asymmetric RSA key pair of the specified length.
	 */
	public static KeyPair generateRSAKeyPair(RSA_KEYLENGTH keyLength) {

		int strength = keyLength.value();
		BigInteger publicExp = new BigInteger("10001", 16); // Fermat F4, largest known fermat prime

		try {
			JDKKeyPairGenerator gen = new JDKKeyPairGenerator.RSA();
			RSAKeyGenParameterSpec params = new RSAKeyGenParameterSpec(strength, publicExp);
			gen.initialize(params, new SecureRandom());
			return gen.generateKeyPair();
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("Exception whil RSA key pair generation:", e);
		}
		return null;

		// RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
		// KeyGenerationParameters parameters = new RSAKeyGenerationParameters(publicExp, new SecureRandom(),
		// strength, certainty);
		// kpg.init(parameters);
		//
		// AsymmetricCipherKeyPair keyPair = kpg.generateKeyPair();
		// return keyPair;
	}

	public static KeyPair generateProtectionKey() {
		KeyPairGenerator gen = null;
		try {
			gen = KeyPairGenerator.getInstance("DSA");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return gen.generateKeyPair();
	}

	/**
	 * Symmetrically encrypts the provided data by means of the AES algorithm.
	 * 
	 * @param data The data to be encrypted.
	 * @param secretKey The symmetric key with which the data shall be encrypted.
	 * @param initVector The initialization vector (IV) with which the data shall be encrypted.
	 * @return Returns the encrypted data.
	 */
	public static byte[] encryptAES(byte[] data, SecretKey secretKey, byte[] initVector)
			throws DataLengthException, IllegalStateException, InvalidCipherTextException {

		return processAESCiphering(true, data, secretKey, initVector);
	}

	/**
	 * Symmetrically decrypts the provided data by means of the AES algorithm.
	 * 
	 * @param data The data to be decrypted.
	 * @param secretKey The symmetric key with which the data shall be decrypted.
	 * @param initVector The initialization vector (IV) with which the data shall be decrypted.
	 * @return Returns the decrypted data.
	 */
	public static byte[] decryptAES(byte[] data, SecretKey secretKey, byte[] initVector)
			throws DataLengthException, IllegalStateException, InvalidCipherTextException {

		return processAESCiphering(false, data, secretKey, initVector);
	}

	public static NetworkContent decryptAES(EncryptedNetworkContent content, SecretKey aesKey)
			throws DataLengthException, IllegalStateException, InvalidCipherTextException, ClassNotFoundException, IOException {
		byte[] decrypted = EncryptionUtil.decryptAES(content.getCipherContent(), aesKey,
				content.getInitVector());
		return (NetworkContent) EncryptionUtil.deserializeObject(decrypted);
	}

	/**
	 * Asymmetrically encrypts the provided data by means of the RSA algorithm. In order to encrypt the
	 * content, a public RSA key has to be provided.
	 * 
	 * @param data The data to be encrypted.
	 * @param publicKey The asymmetric public key with which the data shall be encrypted.
	 * @return Returns the encrypted data.
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] encryptRSA(byte[] data, PublicKey publicKey) throws InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException {

		installBCProvider();

		try {
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
			logger.error("Exception while RSA encryption:", e);
			e.printStackTrace();
		}
		return null;

		// return processRSACiphering(true, data, publicKey);
	}

	/**
	 * Asymmetrically decrypts the provided data by means of the RSA algorithm. In order to decrypt the
	 * content, a private RSA key has to be provided.
	 * 
	 * @param data The data to be decrypted.
	 * @param publicKey The asymmetric private key with which the data shall be decrypted.
	 * @return Returns the decrypted data.
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] decryptRSA(byte[] data, PrivateKey privateKey) throws InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException {

		installBCProvider();

		try {
			Cipher cipher = Cipher.getInstance("RSA", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
			logger.error("Exception while RSA encryption:", e);
			e.printStackTrace();
		}
		return null;

		// return processRSACiphering(false, data, privateKey);
	}

	/**
	 * Encrypts the provided data in a hybrid manner. First, the content is symmetrically encrypted with a
	 * randomly generated IV and AES key of the specified length. Then, these encryption parameters are
	 * asymmetrically encrypted with the provided RSA public key.</br>
	 * 
	 * @param data The data to be encrypted in a hybrid manner.
	 * @param publicKey The RSA public key with which the data shall be encrypted.
	 * @param aesKeyLength The key length of the inner AES encryption.
	 * @return Returns a {@link HybridEncryptedContent} object containing the RSA encrypted parameters and the
	 *         AES encrypted content.
	 * @throws DataLengthException
	 * @throws IllegalStateException
	 * @throws InvalidCipherTextException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static HybridEncryptedContent encryptHybrid(byte[] data, PublicKey publicKey,
			AES_KEYLENGTH aesKeyLength) throws DataLengthException, IllegalStateException,
	InvalidCipherTextException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		// generate AES key
		SecretKey aesKey = generateAESKey(aesKeyLength);
		byte[] encodedAesKey = aesKey.getEncoded();

		// generate IV
		byte[] initVector = generateIV();

		// concatenate symmetric encryption parameters -> max. 48 bytes (with AES 256) -> can be encrypted
		// with RSA 512 bit
		byte[] params = new byte[initVector.length + encodedAesKey.length];
		System.arraycopy(initVector, 0, params, 0, initVector.length);
		System.arraycopy(encodedAesKey, 0, params, initVector.length, encodedAesKey.length);

		// encrypt data symmetrically
		byte[] aesEncryptedData = encryptAES(data, aesKey, initVector);

		// encrypt parameters asymmetrically
		byte[] rsaEncryptedParams = encryptRSA(params, publicKey);

		return new HybridEncryptedContent(rsaEncryptedParams, aesEncryptedData);
	}

	/**
	 * Decrypts the provided data in a hybrid manner. First, the symmetric encryption parameters stored in the
	 * {@link HybridEncryptedContent} are asymmetrically decrypted with the specified RSA private key. Then,
	 * the symmetrically encrypted data is decrypted by means of these parameters and then returned.
	 * 
	 * @param data The {@link HybridEncryptedContent} to be decrypted in a hybrid manner.
	 * @param privateKey The RSA private key with which the data shall be decrypted.
	 * @return Returns the decrypted data.
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws DataLengthException
	 * @throws IllegalStateException
	 * @throws InvalidCipherTextException
	 */
	public static byte[] decryptHybrid(HybridEncryptedContent data, PrivateKey privateKey)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DataLengthException,
			IllegalStateException, InvalidCipherTextException {

		// decrypt parameters asymmetrically
		byte[] params = decryptRSA(data.getEncryptedParameters(), privateKey);

		// split symmetric encryption parameters
		byte[] initVector = Arrays.copyOfRange(params, 0, IV_LENGTH);
		byte[] encodedAesKey = Arrays.copyOfRange(params, IV_LENGTH, params.length);

		// decrypt data symmetrically
		SecretKey aesKey = new SecretKeySpec(encodedAesKey, 0, encodedAesKey.length, "AES");
		return decryptAES(data.getEncryptedData(), aesKey, initVector);
	}

	/**
	 * Signs the provided data with the specified private key and returns the signature.
	 * 
	 * @param data The content to be signed.
	 * @param privateKey The private key used to sign the content.
	 * @return The created signature of the data.
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static byte[] sign(byte[] data, PrivateKey privateKey) throws InvalidKeyException,
	SignatureException {

		installBCProvider();

		try {
			Signature signEngine = Signature.getInstance(SINGATURE_ALGORITHM, "BC");
			signEngine.initSign(privateKey);
			signEngine.update(data);
			return signEngine.sign();
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			logger.error("Exception while signing:", e);
		}

		return null;

		// return setupSigner(true, data, privateKey).generateSignature();
	}

	/**
	 * Verifies the provided signature of the provided data with the specified public key.
	 * 
	 * @param data The data to be verified.
	 * @param signature The signature with which the data should be verified.
	 * @param publicKey The public key used to verify the content.
	 * @return Returns true if the signature could be verified and false otherwise.
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verify(byte[] data, byte[] signature, PublicKey publicKey)
			throws InvalidKeyException, SignatureException {

		installBCProvider();

		try {
			Signature signEngine = Signature.getInstance(SINGATURE_ALGORITHM, "BC");
			signEngine.initVerify(publicKey);
			signEngine.update(data);
			return signEngine.verify(signature);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			logger.error("Exception while signing:", e);
		}

		return false;

		// return setupSigner(false, data, publicKey).verifySignature(signature);
	}

	/**
	 * Generates a MD5 hash of a given data
	 * 
	 * @param data to calculate the MD5 hash over it
	 * @return the md5 hash
	 */
	public static byte[] generateMD5Hash(byte[] data) {
		MD5Digest digest = new MD5Digest();
		digest.update(data, 0, data.length);
		byte[] md5 = new byte[digest.getDigestSize()];
		digest.doFinal(md5, 0);
		return md5;
	}

	/**
	 * Generates a MD5 hash of an input stream
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] generateMD5Hash(File file) throws IOException, NoSuchAlgorithmException {
		if (file.isDirectory()) {
			return null;
		}

		MessageDigest digest = MessageDigest.getInstance("MD5");

		byte[] buffer = new byte[1024];
		int numRead;
		FileInputStream fis = new FileInputStream(file);
		DigestInputStream dis = new DigestInputStream(fis, digest);
		do {
			numRead = dis.read(buffer);
			if (numRead > 0) {
				digest.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		dis.close();
		fis.close();

		byte[] md5 = new byte[digest.getDigestLength()];
		digest.update(md5, 0, digest.getDigestLength());

		return md5;
	}

	public static byte[] serializeObject(Serializable object) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] result = null;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			result = baos.toByteArray();
		} catch (IOException e) {
			logger.error("Exception while serializing object:", e);
			throw e;
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				logger.error("Exception while closing serialization process.");
				throw e;
			}
		}
		return result;
	}

	public static Object deserializeObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		Object result = null;

		try {
			ois = new ObjectInputStream(bais);
			result = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Exception while deserializing object.");
			throw e;
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (bais != null)
					bais.close();
			} catch (IOException e) {
				logger.error("Exception while closing deserialization process.");
				throw e;
			}
		}

		return result;
	}

	public static String toBase64(Serializable object) throws IOException {

		return new String(Base64.encode(serializeObject(object)));
	}

	public static Object toBytes(String string) throws IOException, ClassNotFoundException {

		return deserializeObject(Base64.decode(string));
	}

	/**
	 * Converts the content of a byte array into a human readable form.
	 * 
	 * @param data The byte array to be converted.
	 * @return The hex converted byte array.
	 */
	public static String toHex(byte[] data) {

		final String digits = "0123456789abcdef";
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i != data.length; i++) {
			int v = data[i] & 0xff;

			buf.append(digits.charAt(v >> 4));
			buf.append(digits.charAt(v & 0xf));
		}

		return buf.toString();
	}

	public static void installBCProvider() {
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	private static byte[] processAESCiphering(boolean forEncrypting, byte[] data, SecretKey key,
			byte[] initVector) throws DataLengthException, IllegalStateException, InvalidCipherTextException {

		// set up engine, block cipher mode and padding
		AESEngine aesEngine = new AESEngine();
		CBCBlockCipher cbc = new CBCBlockCipher(aesEngine);
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(cbc);

		// apply parameters
		CipherParameters parameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), initVector);
		cipher.init(forEncrypting, parameters);

		// process ciphering
		byte[] output = new byte[cipher.getOutputSize(data.length)];

		int bytesProcessed1 = cipher.processBytes(data, 0, data.length, output, 0);
		int bytesProcessed2 = cipher.doFinal(output, bytesProcessed1);

		byte[] result = new byte[bytesProcessed1 + bytesProcessed2];
		System.arraycopy(output, 0, result, 0, result.length);
		return result;
	}

	public static String byteToHex(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static boolean compareMD5(File file, byte[] expectedMD5) throws IOException, NoSuchAlgorithmException {
		if (!file.exists() && expectedMD5 == null) {
			// both do not exist
			return true;
		} else if (file.isDirectory()) {
			// directories always match
			return true;
		}

		byte[] md5Hash = EncryptionUtil.generateMD5Hash(file);
		return compareMD5(md5Hash, expectedMD5);
	}

	public static boolean compareMD5(byte[] md5, byte[] expectedMD5) {
		// both null values is ok
		if (md5 == null) {
			return expectedMD5 == null;
		} else if (expectedMD5 == null) {
			return md5 == null;
		}

		// calculate the MD5 hash and compare it
		return Arrays.equals(md5, expectedMD5);
	}

	public static byte[] decryptAES(byte[] data, SecretKey secretKey, byte[] initVector, String securityProvider,
			IStrongAESEncryption strongAES) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
		return processAESCiphering(false, data, secretKey, initVector);
	}

	public static EncryptedNetworkContent encryptAES(NetworkContent content, SecretKey encryptionKey) throws IOException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		byte[] serialized = EncryptionUtil.serializeObject(content);
		byte[] initVector = EncryptionUtil.generateIV();
		byte[] encryptedContent = EncryptionUtil.encryptAES(serialized, encryptionKey, initVector);

		EncryptedNetworkContent encryptedNetworkContent = new EncryptedNetworkContent(encryptedContent,
				initVector);
		return encryptedNetworkContent;
	}

	// private static byte[] processRSACiphering(boolean isEncrypting, byte[] data, CipherParameters key)
	// throws InvalidCipherTextException {
	//
	// // set up engine and padding
	// RSAEngine rsaEngine = new RSAEngine();
	// AsymmetricBlockCipher cipher = new PKCS1Encoding(rsaEngine);
	//
	// // apply parameters
	// cipher.init(isEncrypting, key);
	//
	// // process ciphering
	// int position = 0;
	// int inputBlockSize = cipher.getInputBlockSize();
	// byte[] result = new byte[0];
	// while (position < data.length) {
	// if (position + inputBlockSize > data.length)
	// inputBlockSize = data.length - position;
	//
	// byte[] hexEncodedCipher = cipher.processBlock(data, position, inputBlockSize);
	// result = combine(result, hexEncodedCipher);
	// position += cipher.getInputBlockSize();
	// }
	// return result;
	// }

	// private static RSADigestSigner setupSigner(boolean forSigning, byte[] data, CipherParameters key) {
	//
	// // set up digester / hash function (e.g. SHA-1)
	// SHA1Digest digester = new SHA1Digest();
	//
	// // set up signature mode (e.g. RSA)
	// RSADigestSigner signer = new RSADigestSigner(digester);
	//
	// // apply parameters
	// signer.init(forSigning, key);
	// signer.update(data, 0, data.length);
	//
	// return signer;
	// }

	// private static byte[] combine(byte[] one, byte[] two) {
	//
	// byte[] combined = new byte[one.length + two.length];
	//
	// System.arraycopy(one, 0, combined, 0, one.length);
	// System.arraycopy(two, 0, combined, one.length, two.length);
	//
	// return combined;
	// }
}