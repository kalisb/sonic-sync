package sonic.sync.core.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class ConsoleFileAgent {

	private final File cache;
	private File root;

	public ConsoleFileAgent(File root) {
		this.root = root;
		this.cache = new File(FileUtils.getTempDirectory(), "Sync-Cache");
	}

	public void writeCache(String key, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File(cache, key), data);
	}
	
	public File getRoot() {
		return root;
	}

}
