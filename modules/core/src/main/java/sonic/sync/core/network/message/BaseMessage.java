package sonic.sync.core.network.message;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.SecureRandom;

import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.NetworkManager;

public abstract class BaseMessage implements Runnable, Serializable {

	public BaseMessage(String address) {
		this(createMessageID(), null, targetAddress, false);
	}

	public BaseMessage(String messageID, String targetKey, String senderAddress,
			boolean needsRedirectedSend) {
		this.messageID = messageID;
		this.targetKey = targetKey;
		this.targetAddress = senderAddress;
		this.needsRedirectedSend = needsRedirectedSend;
	}

	protected static String createMessageID() {
		return new BigInteger(56, new SecureRandom()).toString(32);
	}

	protected final String messageID;
	protected final String targetKey;

	protected String senderAddress;

	protected NetworkManager networkManager;

	private static final SSLogger logger = SSLoggerFactory.getLogger(BaseMessage.class);

	private static final long serialVersionUID = 5080812282190501445L;

	protected static String targetAddress;
	private final boolean needsRedirectedSend;

	private int directSendingCounter = 0;
	
	protected PublicKey senderPublicKey;

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}
	
	public String getSenderAddress() {
		return senderAddress;
	}
	
	public String getMessageID() {
		return messageID;
	}
}
