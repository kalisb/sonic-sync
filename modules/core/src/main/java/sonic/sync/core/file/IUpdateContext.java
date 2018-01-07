package sonic.sync.core.file;

import java.io.File;
import java.security.KeyPair;

import sonic.sync.core.configuration.FileConfiguration;

public interface IUpdateContext {

	File consumeFile();

	FileConfiguration consumeFileConfiguration();

	void setLargeFile(boolean b);

	boolean allowLargeFile();

	File consumeRoot();

	void provideMetaFileProtectionKeys(KeyPair metaFileProtectionKeys);

	void provideChunkProtectionKeys(KeyPair chunkProtectionKeys);

}
