package sonic.sync.core.configuration;

import java.util.HashSet;
import java.util.Set;

import sonic.sync.core.network.data.NetworkContent;
import sonic.sync.core.network.message.PeerAddress;
public class Locations extends NetworkContent {
	private static final long serialVersionUID = 1L;

	private final String userId;
	private final Set<String> addresses;

	public Locations(String userId) {
		this.userId = userId;
		this.addresses = new HashSet<>();
	}

	public String getUserId() {
		return userId;
	}

	public void addPeerAddress(String peerAddress) {
		addresses.add(peerAddress);
	}

	public void removePeerAddress(PeerAddress toRemove) {
		addresses.remove(toRemove);
	}

	public Set<String> getPeerAddresses() {
		return addresses;
	}

}
