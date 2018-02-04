package sonic.sync.core.message;

import java.util.ArrayList;
import java.util.List;

import sonic.sync.core.exception.SSException;
import sonic.sync.core.step.IStep;

public class SyncProcess {

	private List<IStep> components = new ArrayList<>();
	private int executionIndex;

	private IStep next = null;

	public void add(IStep step) {
		components.add(step);	
	}

	public void execute() throws SSException {
		executionIndex = 0;
		while (executionIndex < components.size()) {
			next = components.get(executionIndex);
			next.execute();
			executionIndex++;
		}
	}

	public void addAll(List<IStep> processes) {
		components.addAll(processes);
	}

}
