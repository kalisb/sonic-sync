package sonic.sync.core.file.process;

import java.io.File;
import java.security.KeyPair;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.network.message.Session;
import sonic.sync.core.security.IEncryption;

public class DeleteFileProcessContext implements IUpdateContext {
	private File file;
	private Session session;
	private IEncryption encryption;
	//private Index index;
	
	public DeleteFileProcessContext(File file, Session session, IEncryption encryption) {
		this.file = file;
		this.session = session;
		this.encryption = encryption;
	}

	@Override
	public File consumeFile() {
		return file;
	}

	@Override
	public FileConfiguration consumeFileConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLargeFile(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean allowLargeFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File consumeRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void provideMetaFileProtectionKeys(KeyPair metaFileProtectionKeys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void provideChunkProtectionKeys(KeyPair chunkProtectionKeys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMagnetLink() {
		return "";
	}
/*
	@Override
	public void provideIndex(Index index) {
		this.index = index;
	}
*/
}
