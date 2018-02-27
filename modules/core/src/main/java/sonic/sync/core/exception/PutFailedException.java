package sonic.sync.core.exception;

public class PutFailedException extends SSException {

	private static final long serialVersionUID = -676084733761214493L;

	public PutFailedException() {
		this("Putting content to the DHT failed");
	}

	public PutFailedException(String message) {
		super(message);
	}

}
