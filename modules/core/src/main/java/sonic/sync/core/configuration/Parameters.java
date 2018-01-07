package sonic.sync.core.configuration;

import java.security.KeyPair;

import sonic.sync.core.libtorrent.Sha1Hash;
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
		this.lKey =  new Sha1Hash(locationKey);
		return this;
	}
	
	public String getLocationKey() {
		return locationKey;
	}

	public Parameters setContentKey(String contentKey) {
		this.contentKey = contentKey;
		this.cKey = new Sha1Hash(contentKey);
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
