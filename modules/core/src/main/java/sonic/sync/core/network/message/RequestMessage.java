package sonic.sync.core.network.message;

import java.io.IOException;
import java.io.Serializable;

public abstract class RequestMessage extends BaseMessage {

	private static final long serialVersionUID = -8363641962924723518L;

	private IResponseCallBackHandler handler;

	public RequestMessage(String address) {
		super(address);
	}

	public final IResponseCallBackHandler getCallBackHandler() {
		return handler;
	}

	public final void setCallBackHandler(IResponseCallBackHandler handler) {
		this.handler = handler;
	}

	public final ResponseMessage createResponse(Serializable content) {
		return new ResponseMessage(messageID, senderAddress, content);
	}

	public void sendDirectResponse(ResponseMessage response) throws IOException {
		networkManager.publish(response, getSenderPublicKey());
	}
}