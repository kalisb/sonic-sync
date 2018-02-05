package sonic.sync.core.network;

public class EncryptedNetworkContent extends BaseNetworkContent {

	private final byte[] cipherContent;
	private final byte[] initVector;
	
	public EncryptedNetworkContent(byte[]  cipherContent, byte[] initVector) {
		this.cipherContent = cipherContent;
		this.initVector = initVector;
	}

	public byte[] getCipherContent() {
		return cipherContent;
	}
	
	public byte[] getInitVector() {
		return initVector;
	}

}
