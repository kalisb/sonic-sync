package sonic.sync.core.configuration;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.handler.LogicalMessageContext;

import com.frostwire.jlibtorrent.Entry;
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
		byte[] locationKeyBytes = Arrays.copyOf(locationKey.getBytes(), 20);
		this.lKey =  new Sha1Hash(locationKeyBytes);
		return this;
	}
	
	public String getLocationKey() {
		return locationKey;
	}

	public Parameters setContentKey(String contentKey) {
		this.contentKey = contentKey;
		byte[] contentKeyBytes = Arrays.copyOf(contentKey.getBytes(), 20);
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
