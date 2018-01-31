package sonic.sync.core.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.SecretKey;

import sonic.sync.core.network.BaseNetworkContent;
import sonic.sync.core.network.EncryptedNetworkContent;
import sonic.sync.core.network.HybridEncryptedContent;
import sonic.sync.core.security.EncryptionUtil.RSA_KEYLENGTH;
import sonic.sync.core.serializer.ISerialize;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DefaultEncryption implements IEncryption {

	private final ISerialize serializer;
	private final String securityProvider;
	private final IStrongAESEncryption strongAES;

	public DefaultEncryption(ISerialize serializer) {
		this(serializer, BouncyCastleProvider.PROVIDER_NAME, new BCStrongAESEncryption());

		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public DefaultEncryption(ISerialize serializer, String securityProvider, IStrongAESEncryption strongAES) {
		this.serializer = serializer;
		this.securityProvider = securityProvider;
		this.strongAES = strongAES;
	}

	@Override
	public String getSecurityProvider() {
		return securityProvider;
	}

	@Override
	public EncryptedNetworkContent encryptAES(BaseNetworkContent content, SecretKey aesKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseNetworkContent decryptAES(EncryptedNetworkContent content, SecretKey aesKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HybridEncryptedContent encryptHybrid(BaseNetworkContent content, PublicKey publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HybridEncryptedContent encryptHybrid(byte[] content, PublicKey publicKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseNetworkContent decryptHybrid(HybridEncryptedContent content, PrivateKey privateKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] decryptHybridRaw(HybridEncryptedContent content, PrivateKey privateKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyPair generateRSAKeyPair(RSA_KEYLENGTH length) {
		return EncryptionUtil.generateRSAKeyPair(length, securityProvider);
	}

	public static PublicKey key2String(PublicKey locationKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
