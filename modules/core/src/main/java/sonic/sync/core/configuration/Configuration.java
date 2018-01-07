package sonic.sync.core.configuration;

public class Configuration {
	private Serializer serializer;
	private String address;

	public Configuration() {
	}

	public Configuration(String string, Object object) {
		// TODO Auto-generated constructor stub
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String resolve (final String path, final String defaultValue) {
		Configuration item = locate(path);
		if (item != null)
			return item.value();
		else
			return defaultValue;
	}

	private String value() {
		// TODO Auto-generated method stub
		return null;
	}

	private Configuration locate(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void setPath(String path, String value) {
		// TODO Auto-generated method stub

	}

}
