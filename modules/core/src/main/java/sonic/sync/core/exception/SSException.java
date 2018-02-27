package sonic.sync.core.exception;

public class SSException extends Exception {
	
	private static final long serialVersionUID = -5099216171679230489L;

	public SSException() {
		super("Something is wrong with a SonicSync operation.");
	}

	public SSException(String message) {
		super(message);
	}
}
