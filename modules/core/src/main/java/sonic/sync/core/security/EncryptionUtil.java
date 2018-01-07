package sonic.sync.core.security;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import sonic.sync.core.network.EncryptedNetworkContent;

public class EncryptionUtil {
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

	public static String byteToHex(byte[] locationKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public static EncryptedNetworkContent encryptAES(UserProfile userProfile, SecretKey encryptionKey) {
		// TODO Auto-generated method stub
		return null;
	}

	public static KeyPair generateRSAKeyPair(String keylengthMetaDocument) {
		// TODO Auto-generated method stub
		return null;
	}
}
