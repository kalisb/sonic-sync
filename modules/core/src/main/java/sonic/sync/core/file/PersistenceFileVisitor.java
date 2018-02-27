package sonic.sync.core.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import sonic.sync.core.security.EncryptionUtil;
import sonic.sync.core.util.Constants;

public class PersistenceFileVisitor extends SimpleFileVisitor<Path> {

	private final HashMap<String, byte[]> fileTree;
	private final Path root;
	private final Path configFilePath;

	public PersistenceFileVisitor(Path root) {
		this.root = root;
		fileTree = new HashMap<String, byte[]>();

		configFilePath = Paths.get(root.toString(), Constants.META_FILE_NAME);
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
		// ignore configFile
		if (path.equals(configFilePath)) {
			return FileVisitResult.CONTINUE;
		}

		addToMap(path);
		return super.visitFile(path, attrs);
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// ignore root directory
		if (dir.equals(root)) {
			return FileVisitResult.CONTINUE;
		}

		addToMap(dir);
		return super.preVisitDirectory(dir, attrs);
	}

	private void addToMap(Path path) throws IOException {
		// add to fileTree
		Path relativePath = root.relativize(path);

		byte[] md5;
		try {
			md5 = EncryptionUtil.generateMD5Hash(path.toFile());
			fileTree.put(relativePath.toString(), md5);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HashMap<String, byte[]> getFileTree() {
		return fileTree;
	}

}
