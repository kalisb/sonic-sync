package sonic.sync.core.exception;

public class AbortModifyException extends SSException {

	public AbortModifyException() {
		this("Modification failed");
	}

	public AbortModifyException(String message) {
		super(AbortModificationCode.UNSPECIFIED, message);
	}
	public AbortModifyException(ErrorCode error, String message) {
		super(error, message);
	}

}
