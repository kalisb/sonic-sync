package sonic.sync.core.exception;

import sonic.sync.core.step.ValidateFileStep;

public class ProcessExecutionException extends Exception {

	public ProcessExecutionException(ValidateFileStep validateFileStep, String format) {
		// TODO Auto-generated constructor stub
	}


	public ProcessExecutionException(ValidateFileStep validateFileStep,
			AbortModifyException abortModifyException) {
		// TODO Auto-generated constructor stub
	}

}
