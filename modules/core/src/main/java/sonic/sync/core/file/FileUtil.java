package sonic.sync.core.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;

public class FileUtil {

	private final static SSLogger logger = SSLoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {
	}

	public static long getFileSize(File file) {
		InputStream stream = null;
		try {
			URL url = file.toURI().toURL();
			stream = url.openStream();
			return stream.available();
		} catch (IOException e) {
			return file.length();
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public static void moveFile(String sourceName, String destName, FileTreeNode oldParent,
			FileTreeNode newParent, FileManager fileManager) throws IOException {
		// find the file of this user on the disc
		File oldParentFile = fileManager.getPath(oldParent).toFile();
		File toMoveSource = new File(oldParentFile, sourceName);

		if (!toMoveSource.exists()) {
			throw new FileNotFoundException("Cannot move file '" + toMoveSource.getAbsolutePath()
			+ "' because it's not at the source location anymore");
		}

		File newParentFile = fileManager.getPath(newParent).toFile();
		File toMoveDest = new File(newParentFile, destName);

		if (toMoveDest.exists()) {
			logger.warn("Overwriting '" + toMoveDest.getAbsolutePath()
			+ "' because file has been moved remotely");
		}

		// move the file
		Files.move(toMoveSource.toPath(), toMoveDest.toPath(), StandardCopyOption.ATOMIC_MOVE);
		logger.debug("Successfully moved the file from " + toMoveSource.getAbsolutePath() + " to "
				+ toMoveDest.getAbsolutePath());
	}
}
