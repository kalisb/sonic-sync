package sonic.sync.core.logger;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

public class LogHierarchy extends Hierarchy {

	private static final LoggerFactory defaultFactory = new CategoryFactory();

	public LogHierarchy(Logger root) {
		super(root);
	}

	@Override
	public SSLogger getLogger(String name) {
		return (SSLogger)super.getLogger(name, defaultFactory);
	}

}
