package sonic.sync.client;

import java.io.IOException;

import sonic.sync.core.logger.SSLoggerFactory;

public class LoggerInit {
	public static void initLogger() {
		try {
			SSLoggerFactory.initFactory();
		} catch (IOException e) {
			System.err.println("SSLoggerFactory could not be initialized.");
		}
	}
}
