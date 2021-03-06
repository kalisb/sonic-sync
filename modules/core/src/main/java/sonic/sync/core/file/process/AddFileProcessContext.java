package sonic.sync.core.file.process;

import java.io.File;
import java.security.KeyPair;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.network.message.Session;
import sonic.sync.core.security.IEncryption;

public class AddFileProcessContext implements IUpdateContext {

	private File file;
	private String magnetLink;
	private File torrent;
	private Session session;
	private FileConfiguration fileConfiguration;
	private IEncryption encryption;
	private boolean largeFile;
	private KeyPair chunkProtectionKeys;
	private KeyPair metaFileProtectionKeys;
	//private Index index;

	public AddFileProcessContext(File file, Session session, FileConfiguration fileConfiguration,
			IEncryption encryption) {
		this.file = file;
		this.session = session;
		this.fileConfiguration = fileConfiguration;
		this.encryption = encryption;
	}

	public String getMagnetLink() {
		return magnetLink == null ? "" : magnetLink;
	}
	
	public void setMagnetLink(String magnetLink) {
		this.magnetLink = magnetLink;
	}
	
	@Override
	public File consumeFile() {
		return file;
	}

	@Override
	public FileConfiguration consumeFileConfiguration() {
		return fileConfiguration;
	}
	
	public File getTorrent() {
        return torrent;
    }
	
	public void setTorrent(File torrent) {
        this.torrent = torrent;
    }

	@Override
	public void setLargeFile(boolean largeFile) {
		this.largeFile = largeFile;
	}

	@Override
	public boolean allowLargeFile() {
		return true;
	}

	@Override
	public File consumeRoot() {
		return session.getRootFile();
	}

	@Override
	public void provideChunkProtectionKeys(KeyPair chunkProtectionKeys) {
		this.chunkProtectionKeys = chunkProtectionKeys;
	}

	@Override
	public void provideMetaFileProtectionKeys(KeyPair metaFileProtectionKeys) {
		this.metaFileProtectionKeys = metaFileProtectionKeys;
	}

}
