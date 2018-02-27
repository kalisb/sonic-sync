package sonic.sync.core.network.message;

import java.io.IOException;

import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.step.ContactPeersStep;

public class ContactPeerMessage extends RequestMessage {

	private static final long serialVersionUID = 4949538351342930783L;

	private static final SSLogger logger = SSLoggerFactory.getLogger(ContactPeerMessage.class);

	private String evidenceContent;

	public ContactPeerMessage(String address, String evidenceContent) {
		super(address);
		this.evidenceContent = evidenceContent;
	}

	@Override
	public void run() {
		logger.debug(String.format("Sending a contact peer response message. requesting address = '%s'",
				getSenderAddress()));
		// send a response with the evidentContent -> proves this peer could decrypt and read the message
		try {
			sendDirectResponse(createResponse(evidenceContent));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}