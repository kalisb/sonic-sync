package sonic.sync.core.network;

import sonic.sync.core.network.data.NetworkContent;

public class EncryptedNetworkContent extends NetworkContent {
	private static final long serialVersionUID = 1L;
	private final byte[] cipherContent;
	private final byte[] initVector;

	public EncryptedNetworkContent(byte[] cipherContent, byte[] initVector) {
		this.cipherContent = cipherContent;
		this.initVector = initVector;
	}

	public final byte[] getCipherContent() {
		return cipherContent;
	}

	public final byte[] getInitVector() {
		return initVector;
	}
}
