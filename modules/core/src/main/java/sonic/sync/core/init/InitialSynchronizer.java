package sonic.sync.core.init;

import java.io.File;
import java.util.Map;

public class InitialSynchronizer {

	private final File root;
	//private final UserProfile userProfile;
	//private final FolderIndex profileRootNode;

	// Map<file-path, file-hash>
	private final Map<String, byte[]> before;
	private final Map<String, byte[]> now;

	public InitialSynchronizer(File rootDirectory, Map<String, byte[]> before, Map<String, byte[]> now) {
		this.root = rootDirectory;
		this.before = before;
		this.now = now;
	}
}
