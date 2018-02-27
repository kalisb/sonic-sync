package sonic.sync.core.exception;

public class RemoveFailedException extends SSException {

	private static final long serialVersionUID = 6814330775686840290L;

	public RemoveFailedException() {
		this("Removing content to the DHT failed");
	}

	public RemoveFailedException(String message) {
		super(message);
	}
}
