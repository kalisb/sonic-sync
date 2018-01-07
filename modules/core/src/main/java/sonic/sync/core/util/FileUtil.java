package sonic.sync.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import sonic.sync.core.file.FileAgent;
import sonic.sync.core.security.PersistentMetaData;
import sonic.sync.core.serializer.ISerialize;

public class FileUtil {

	public static PersistentMetaData readPersistentMetaData(Object fileAgent, ISerialize serializer) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean isInSharedDirectory(FileAgent fileAgent, File file) {
		return fileAgent == null ? false : isInSharedDirectory(file, fileAgent.getRoot());
	}
	
	public static boolean isInSharedDirectory(File file, File root) {
		if (root == null || file == null) {
			return false;
		}

		return file.getAbsolutePath().toString().startsWith(root.getAbsolutePath());
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

	public static CharSequence getFileSep() {
		// TODO Auto-generated method stub
		return null;
	}
}
