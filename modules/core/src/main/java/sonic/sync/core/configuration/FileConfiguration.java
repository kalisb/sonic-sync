package sonic.sync.core.configuration;

import java.math.BigInteger;

public class FileConfiguration {

	private final BigInteger maxFileSize;
	private final int chunkSize;

	public FileConfiguration(BigInteger maxFileSize, int chunkSize) {
		this.maxFileSize = maxFileSize;
		this.chunkSize = chunkSize;
	}

	public static FileConfiguration createCustom(BigInteger maxFileSize, int chunkSize) {
		return new FileConfiguration(maxFileSize, chunkSize);
	}
	
	public int getChunkSize() {
		return chunkSize;
	}
	
	public BigInteger getMaxFileSize() {
		return maxFileSize;
	}

}
