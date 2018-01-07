package sonic.sync.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.yaml.snakeyaml.Yaml;

public class ConfigurationFactory {

	public static Configuration load(String configFile) {
		Yaml yaml = new Yaml();  
		Configuration config = null;
        try( InputStream in = Files.newInputStream(Paths.get(configFile))) {
            config = yaml.loadAs( in, Configuration.class );
        } catch (IOException e) {
			e.printStackTrace();
		}
        return config;
	}

}
