package sonic.sync.core.exception;

public class SSException extends Exception {
	private final ErrorCode error;

	public SSException() {
		this(null, "Something is wrong with a SonicSync operation.");
	}

	public SSException(String message) {
		this(null, message);
	}

	public SSException(ErrorCode error, String message) {
		super(message);
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
}
}
