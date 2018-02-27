package sonic.sync.core.exception;

public class NoSessionException extends SSException {
	private static final long serialVersionUID = 4263677549436609207L;

	public NoSessionException() {
		super("You are not logged in.");
	}
}
