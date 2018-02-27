package sonic.sync.core.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class CategoryFactory implements LoggerFactory {

	@Override
	public Logger makeNewLoggerInstance(String name) {
		return new SSLogger(name);
	}
}
