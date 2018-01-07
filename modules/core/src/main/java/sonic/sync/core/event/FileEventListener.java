package sonic.sync.core.event;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import sonic.sync.core.message.FileManager;

public class FileEventListener implements FileAlterationListener {

	public FileEventListener(FileManager fileManager) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryCreate(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryChange(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryDelete(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileCreate(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileChange(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileDelete(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

}
