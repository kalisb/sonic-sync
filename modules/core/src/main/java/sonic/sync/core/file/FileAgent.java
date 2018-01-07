package sonic.sync.core.file;

import java.io.File;

public class FileAgent {
	File root;

	public FileAgent(File root) {
		this.root = root;
	}

	public File getRoot() {
		return root;
	}

}
