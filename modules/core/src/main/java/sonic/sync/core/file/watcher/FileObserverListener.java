package sonic.sync.core.file.watcher;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import sonic.sync.core.exception.IllegalFileLocation;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.file.FileManager;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.Node;

public class FileObserverListener implements FileAlterationListener {
	
	private static final SSLogger logger = SSLoggerFactory.getLogger(FileObserverListener.class);

	private final Node node;

	public FileObserverListener(Node node) {
		this.node = node;
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
	}

	@Override
	public void onDirectoryCreate(File directory) {
		printFileDetails("created", directory);
		addFile(directory);
	}

	@Override
	public void onDirectoryChange(File directory) {
		printFileDetails("changed", directory);
		// TODO implement onDirectoryChange
	}

	@Override
	public void onDirectoryDelete(File directory) {
		printFileDetails("deleted", directory);
		removeFile(directory);
	}

	@Override
	public void onFileCreate(File file) {
		printFileDetails("created", file);
		addFile(file);
	}

	@Override
	public void onFileChange(File file) {
		printFileDetails("updated", file);
		updateFile(file);

	}

	@Override
	public void onFileDelete(File file) {
		printFileDetails("deleted", file);
		removeFile(file);
	}
	
	@Override
	public void onStop(FileAlterationObserver observer) {
	}

	private void addFile(File file) {
		/*try {
			fileManager.createAddProcess(file);
		} catch (IllegalFileLocation | NoSessionException e) {
			logger.error(e.getMessage());
		}*/
	}

	private void removeFile(File file) {
		/*try {
			fileManager.createDeleteProcess(file);
		} catch (IllegalArgumentException | NoSessionException e) {
			logger.error(e.getMessage());
		}*/
	}
	
	private void updateFile(File file) {
		/*try {
			fileManager.createUpdateProcess(file);
		} catch (IllegalArgumentException | NoSessionException e) {
			logger.error(e.getMessage());
		}*/
	}
	
	private void printFileDetails(String action, File file) {
		logger.debug(String.format("%s %s: %s\n", file.isDirectory() ? "Directory" : "File", action,
				file.getAbsolutePath()));
	}

}
