package sonic.sync.core.security;

import javax.crypto.SecretKey;

public interface IStrongAESEncryption {

	byte[] encryptStrongAES(byte[] data, SecretKey secretKey, byte[] initVector);

	byte[] decryptStrongAES(byte[] data, SecretKey secretKey, byte[] initVector);

}
