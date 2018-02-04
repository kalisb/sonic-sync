package sonic.sync.core.step;

import sonic.sync.core.exception.SSException;

public interface IStep {

	void execute() throws SSException;

}
