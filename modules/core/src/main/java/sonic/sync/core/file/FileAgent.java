package sonic.sync.core.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileAgent {
	private final File root;
	private final File cache;

	public FileAgent(File root) {
		this.root = root;
		this.cache = new File(FileUtils.getTempDirectory(), "SS-Cache");
	}

	public File getRoot() {
		return root;
	}

	public byte[] readCache(String key) throws IOException {
		System.out.println(key);
		return FileUtils.readFileToByteArray(new File(cache, key));
	}

}
