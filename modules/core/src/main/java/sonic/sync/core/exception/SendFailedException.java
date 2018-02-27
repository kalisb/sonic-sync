package sonic.sync.core.exception;

public class SendFailedException extends SSException {
	private static final long serialVersionUID = 226923782989779245L;

	public SendFailedException() {
		this("The message could not be sent");
	}

	public SendFailedException(String message) {
		super(message);
	}
}
