package sonic.sync.core.exception;

public class IllegalFileLocation extends SSException {

	private static final long serialVersionUID = 417527294506983792L;

	public IllegalFileLocation() {
		this("File location is not valid");
	}

	public IllegalFileLocation(String message) {
		super(message);
	}

}
