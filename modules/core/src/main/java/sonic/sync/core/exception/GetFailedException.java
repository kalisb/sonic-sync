package sonic.sync.core.exception;

public class GetFailedException extends SSException {

	private static final long serialVersionUID = 1L;

	public GetFailedException() {
		this("Getting content from the DHT failed");
	}

	public GetFailedException(String message) {
		super(message);
	}

}
