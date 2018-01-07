package sonic.sync.core.file;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import sonic.sync.core.exception.IllegalFileLocation;
import sonic.sync.core.exception.NoPeerConnectionException;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.message.FileManager;
import sonic.sync.core.network.Node;

public class FileObserverListener implements FileAlterationListener {
	
	private final FileManager fileManager;
	
	public FileObserverListener(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryCreate(File directory) {
		printFileDetails("created", directory);
		addFile(directory);
	}

	@Override
	public void onDirectoryChange(File directory) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileDelete(File file) {
		printFileDetails("deleted", file);
		removeFile(file);
	}

	private void printFileDetails(String action, File file) {
		System.out.println(file.getName() + " has been " + action);
		
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}
	
	private void addFile(File file) {
		try {
			fileManager.createAddProcess(file);
		} catch (NoSessionException | IllegalArgumentException | NoPeerConnectionException e) {
			System.err.println(e.getMessage());
		}
	}

	private void removeFile(File file) {
	/*	try {
			node.getFileManager().delete(file);
		} catch (IllegalArgumentException | NoSessionException e) {
			System.err.println(e.getMessage());
		}*/
	}

}
