package sonic.sync.core.network.message;

import java.io.Serializable;

import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;

public class ResponseMessage extends BaseMessage {

	private static final SSLogger logger = SSLoggerFactory.getLogger(ResponseMessage.class);

	private static final long serialVersionUID = -4182581031050888858L;

	private final Serializable content;

	public ResponseMessage(String messageID, String senderAddress, Serializable content) {
		super(messageID, null, senderAddress, false);
		this.content = content;
	}
	
	@Override
	public void run() {
		IResponseCallBackHandler handler = networkManager.getMessageManager().getCallBackHandler(
				getMessageID());
		if (handler != null) {
			handler.handleResponseMessage(this);
		} else {
			logger.warn(String.format(
					"No call back handler for this message! currentNodeID='%s', AsyncReturnMessage='%s'",
					networkManager.getNodeId(), this));
		}
}

	public Object getContent() {
		return content;
	}

}
