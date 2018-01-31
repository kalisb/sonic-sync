package sonic.sync.core.configuration;

import java.security.KeyPair;
import java.util.Arrays;

import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.network.BaseNetworkContent;

public class Parameters {

	private BaseNetworkContent networkContent;
	private KeyPair protectionKeys;
	private String contentKey;
	private Sha1Hash cKey;
	private String locationKey;
	private Sha1Hash lKey;

	public Parameters setLocationKey(String locationKey) {
		this.locationKey = locationKey;
		byte[] locationKeyBytes = new byte[20];
		Arrays.fill(locationKeyBytes, (byte) 0 );
		byte[] bytes = locationKey.getBytes();
		int size = locationKey.getBytes().length > 20 ? 20 : locationKey.getBytes().length;
		for (int i = 0; i < size; i++) {
			locationKeyBytes[i] = bytes[i];
		}
		this.lKey =  new Sha1Hash(locationKeyBytes);
		return this;
	}
	
	public String getLocationKey() {
		return locationKey;
	}

	public Parameters setContentKey(String contentKey) {
		this.contentKey = contentKey;
		byte[] contentKeyBytes = new byte[20];
		Arrays.fill(contentKeyBytes, (byte) 0 );
		byte[] bytes = locationKey.getBytes();
		int size = bytes.length > 20 ? 20 : bytes.length;
		for (int i = 0; i < size; i++) {
			contentKeyBytes[i] = bytes[i];
		}
		this.cKey = new Sha1Hash(contentKeyBytes);
		return this;
	}
	
	public String getContentKey() {
		return contentKey;
	}

	public Parameters setNetworkContent(BaseNetworkContent networkContent) {
		this.networkContent = networkContent;
		return this;
	}
	
	public BaseNetworkContent getNetworkContent() {
		return networkContent;
	}

	public Parameters setProtectionKeys(KeyPair protectionKeys) {
		this.protectionKeys = protectionKeys;
		return this;
	}
	
	public KeyPair getProtectionKeys() {
		return protectionKeys;
	}

}
