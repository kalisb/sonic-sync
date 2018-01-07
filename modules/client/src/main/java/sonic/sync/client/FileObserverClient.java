package sonic.sync.client;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import sonic.sync.core.file.FileObserver;

public class FileObserverClient {

	public static void main(String[] args) {

		FileObserver watcher = new FileObserver.FileWatcherBuilder(Paths.get(
				FileUtils.getUserDirectoryPath(), "Sync").toFile()).setInterval(1000).build();
		watcher.addFileListener(new FileAlterationListener() {
			public void onStart(FileAlterationObserver observer) {
			}

			public void onDirectoryCreate(File directory) {
				printFileDetails("Directory created", directory);
			}

			public void onDirectoryChange(File directory) {
				printFileDetails("Directory changed", directory);
			}

			public void onDirectoryDelete(File directory) {
				printFileDetails("Directory deleted", directory);
			}

			public void onFileCreate(File file) {
				printFileDetails("File created", file);
			}

			public void onFileChange(File file) {
				printFileDetails("File changed", file);
			}

			public void onFileDelete(File file) {
				printFileDetails("File deleted", file);
			}

			public void onStop(FileAlterationObserver observer) {
			}
		});
		try {
			watcher.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printFileDetails(String event, File file) {
		System.out.println(String.format("%s: %s\n", event, file.getAbsolutePath()));
	}
}

