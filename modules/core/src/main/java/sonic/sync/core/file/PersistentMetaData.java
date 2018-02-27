package sonic.sync.core.file;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PersistentMetaData implements Serializable {

	private static final long serialVersionUID = -1069468683019402537L;

	private Map<String, byte[]> fileTree;

	public PersistentMetaData() {
		fileTree = new HashMap<String, byte[]>();
	}

	public Map<String, byte[]> getFileTree() {
		return fileTree;
	}

	public void setFileTree(Map<String, byte[]> fileTree) {
		this.fileTree = fileTree;
}

}
