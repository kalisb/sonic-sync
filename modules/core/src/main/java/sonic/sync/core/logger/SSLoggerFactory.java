package sonic.sync.core.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.RootLogger;

public class SSLoggerFactory {

	private static final SSLoggerFactory factory = new SSLoggerFactory();
	private Hierarchy log4jHierarchy = new LogHierarchy(new RootLogger(Level.DEBUG));
	private boolean isConfigured;

	private SSLoggerFactory() {
	}

	public static void initFactory() throws IOException {
		if (!factory.isConfigured) {
			factory.configureLogging();
		}
	}

	public static SSLogger getLogger(Class<?> aClass) {
		return (SSLogger) factory.log4jHierarchy.getLogger(aClass.getName());
	}

	private void configureLogging() throws IOException {

		InputStream propertiesInputStream = this.getClass().getResourceAsStream("config/log4j.properties");

		if (propertiesInputStream != null) {
			Properties props = new Properties();
			props.load(propertiesInputStream);
			new PropertyConfigurator().doConfigure(props, log4jHierarchy);

			propertiesInputStream.close();
		}

		factory.isConfigured = true;
	}

}
