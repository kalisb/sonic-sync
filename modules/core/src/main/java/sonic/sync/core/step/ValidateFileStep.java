package sonic.sync.core.step;

import java.io.File;
import java.math.BigInteger;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.file.FileUtil;
import sonic.sync.core.file.process.IUpdateContext;

public class ValidateFileStep implements IStep {

	private final IUpdateContext context;

	public ValidateFileStep(IUpdateContext context) {
		this.context = context;
	}

	@Override
	public void execute() {
		File file = context.consumeFile();

		if (file.isDirectory()) {
			System.err.println("File " + file.getName() + "is a directory.");
			return;
		}

		// validate the file size
		FileConfiguration config = context.consumeFileConfiguration();
		if (BigInteger.valueOf(FileUtil.getFileSize(file)).compareTo(config.getMaxFileSize()) == 1) {
			System.err.println("File " + file.getName() + " is a 'large file'.");
			context.setLargeFile(true);
		} else {
			System.err.println("File " + file.getName() + " is a 'small file'.");
			context.setLargeFile(false);
		}
	}

}
