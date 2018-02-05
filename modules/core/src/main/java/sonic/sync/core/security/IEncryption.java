package sonic.sync.core.security;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.HybridEncryptedContent;
import sonic.sync.core.security.EncryptionUtil.RSA_KEYLENGTH;

public interface IEncryption {
	String getSecurityProvider();
	EncryptedNetworkContent encryptAES(BaseNetworkContent content, SecretKey aesKey);
	BaseNetworkContent decryptAES(EncryptedNetworkContent content, SecretKey aesKey) throws ClassNotFoundException, IOException;
	HybridEncryptedContent encryptHybrid(BaseNetworkContent content, PublicKey publicKey);
	HybridEncryptedContent encryptHybrid(byte[] content, PublicKey publicKey);
	BaseNetworkContent decryptHybrid(HybridEncryptedContent content, PrivateKey privateKey);
	byte[] decryptHybridRaw(HybridEncryptedContent content, PrivateKey privateKey);
	KeyPair generateRSAKeyPair(RSA_KEYLENGTH length);
}
