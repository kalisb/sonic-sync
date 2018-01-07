package sonic.sync.core.step;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import sonic.sync.core.configuration.Locations;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.message.MessageManager;
import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.message.PeerAddress;
import sonic.sync.core.security.LoginProcessContext;
import sonic.sync.core.security.PublicKeyManager;
import sonic.sync.core.util.Constants;

public class ContactOtherClientsStep implements IStep {

	private final LoginProcessContext context;
	private final MessageManager messageManager;
	private final NetworkManager networkManager;

	private final Map<PeerAddress, Boolean> responses = new ConcurrentHashMap<PeerAddress, Boolean>();
	private CountDownLatch waitForResponses;

	public ContactOtherClientsStep(LoginProcessContext context, NetworkManager networkManager) {
		this.context = context;
		this.networkManager = networkManager;
		this.messageManager = networkManager.getMessageManager();
	}

	@Override
	public void execute() {
		/*PublicKeyManager keyManager = null;
		try {
			keyManager = networkManager.getSession().getKeyManager();
		} catch (NoSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PublicKey ownPublicKey = keyManager.getOwnPublicKey();
		Locations locations = context.consumeLocations();

		sendBlocking(locations.getPeerAddresses(), ownPublicKey);

		locations.getPeerAddresses().clear();

		// add addresses that responded
		for (PeerAddress address : responses.keySet()) {
			if (responses.get(address)) {
				locations.addPeerAddress(address);
			}
		}*/
		// add self
		//PeerAddress ownAddress = networkManager.getConnection().getPeer().peerAddress();
		//locations.addPeerAddress(ownAddress);
	}

	private void sendBlocking(List<PeerAddress> peerAddresses, PublicKey ownPublicKey) {
		waitForResponses = new CountDownLatch(peerAddresses.size());
		boolean hasSlowPeers = false;
		for (final PeerAddress address : peerAddresses) {
			// contact all other clients (exclude self)
			/*if (!address.equals(networkManager.getConnection().getPeer().peerAddress())) {
				logger.debug("Sending contact message to check for aliveness to {}", address);
				String evidence = UUID.randomUUID().toString();
				evidences.put(address, evidence);
				hasSlowPeers = hasSlowPeers || address.isSlow();

				final ContactPeerMessage message = new ContactPeerMessage(address, evidence);
				message.setCallBackHandler(this);

				// asynchronously send all messages (parallel)
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (!messageManager.sendDirect(message, ownPublicKey)) {
							responses.put(address, false);
						}
					}
				}).start();
			}*/
		}

		// wait (blocking) until all responses are here or the time's up
		int waitTime = hasSlowPeers ? Constants.CONTACT_SLOW_PEERS_AWAIT_MS : Constants.CONTACT_PEERS_AWAIT_MS;
		try {
			waitForResponses.await(waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
	}

}
