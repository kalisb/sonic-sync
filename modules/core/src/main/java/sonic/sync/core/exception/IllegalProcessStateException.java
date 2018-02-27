package sonic.sync.core.exception;

public class IllegalProcessStateException extends SSException {

	private static final long serialVersionUID = 7828742398966563635L;

	public IllegalProcessStateException() {
		super("The operation cannot be called because the process is in another state");
	}

	public IllegalProcessStateException(String message) {
		super(message);
	}
}
