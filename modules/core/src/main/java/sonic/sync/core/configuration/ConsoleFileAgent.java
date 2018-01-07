package sonic.sync.core.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import sonic.sync.core.file.FileAgent;

public class ConsoleFileAgent extends FileAgent {

	private final File cache;

	public ConsoleFileAgent(File root) {
		super(root);
		this.cache = new File(FileUtils.getTempDirectory(), "Sync-Cache");
	}

	public void writeCache(String key, byte[] data) throws IOException {
		FileUtils.writeByteArrayToFile(new File(cache, key), data);
	}
	
	public byte[] readCache(String key) throws IOException {
		return FileUtils.readFileToByteArray(new File(cache, key));
	}

}
