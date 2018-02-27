package sonic.sync.core.step;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import sonic.sync.core.configuration.Locations;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.message.ContactPeerMessage;
import sonic.sync.core.network.message.IResponseCallBackHandler;
import sonic.sync.core.network.message.PeerAddress;
import sonic.sync.core.network.message.ResponseMessage;
import sonic.sync.core.security.LoginProcess;
import sonic.sync.core.util.Constants;

public class ContactPeersStep implements IStep, IResponseCallBackHandler {

	private static final SSLogger logger = SSLoggerFactory.getLogger(ContactPeersStep.class);

	private LoginProcess context;

	private ConcurrentHashMap<String, Boolean> responses = new ConcurrentHashMap<>();
	protected ConcurrentHashMap<String, String> evidenceMap = new ConcurrentHashMap<>();

	/*
	 * A flag to indicate if the process step has already finished. It can happen that delayed response
	 * messages arrive but the process has already gone further.
	 */
	private boolean isExecuted = false;

	public ContactPeersStep(LoginProcess context) {
		this.context = context;
	}

	@Override
	public void execute() {
		// check if other client nodes has to be contacted
		if (!context.getNetworkManager().getConfiguration().getPeers().isEmpty()) {
			// set timer to guarantee that the process step goes further because not coming response messages
			// may block the step
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					updateLocationsMap();
				}
			}, Constants.CONTACT_PEERS_AWAIT_MS);

			// contact all peers
			for (String address : context.getNetworkManager().getConfiguration().getPeers()) {
				sendContactPeerMessage(address);
			}
		} else {
			updateLocationsMap();
		}
	}

	protected void sendContactPeerMessage(String address) {
		// generate random verification content
		String evidenceContent = UUID.randomUUID().toString();
		// create a liveness check message
		ContactPeerMessage contactMsg = new ContactPeerMessage(address, evidenceContent);
		evidenceMap.put(address, evidenceContent);
		// the process step is expecting a response
		contactMsg.setCallBackHandler(this);
		// send direct
		boolean success = context.getNetworkManager().sendDirect(contactMsg, context.getNetworkManager().getPublicKey());
		if (!success) {
			responses.put(address, false);
		}
	}

	private synchronized void updateLocationsMap() {
		// ensure this method is executed only once
		if (!isExecuted) {
			isExecuted = true;

			// replace the locations map
			Locations newLocations = new Locations(context.getLocations().getUserId());
			newLocations.setBasedOnKey(context.getLocations().getBasedOnKey());
			newLocations.setVersionKey(context.getLocations().getVersionKey());
			List<String> listToDetectMaster = new ArrayList<>();

			// add contacts that responded
			for (String peerAddress : responses.keySet()) {
				// check if response was ok or failed
				if (responses.get(peerAddress)) {
					newLocations.addPeerAddress(peerAddress);
					listToDetectMaster.add(peerAddress);
				} else {
					logger.warn(String
							.format("A dead client node detected. peer address = '%s'", peerAddress));
				}
			}

			context.setLocations(newLocations);
		}
	}

	@Override
	public void handleResponseMessage(ResponseMessage responseMessage) {
		if (isExecuted) {
			logger.warn(String
					.format("Received a delayed contact peer response message, which gets ignored. peer address = '%s'",
							responseMessage.getSenderAddress()));
			// TODO notify delayed response client nodes about removing him from location map
			return;
		}

		// verify reply
		if (evidenceMap.get(responseMessage.getSenderAddress()).equals((String) responseMessage.getContent())) {
			// add to map and label success
			responses.put(responseMessage.getSenderAddress(), true);
			// check if all peers responded
			if (responses.size() >= context.getLocations().getPeerAddresses().size()) {
				// all peers answered
				updateLocationsMap();
			}
		} else {
			logger.error(String
					.format("Received during liveness check of other clients a wrong evidence content. responding node = '%s'",
							responseMessage.getSenderAddress()));
		}
	}

}
