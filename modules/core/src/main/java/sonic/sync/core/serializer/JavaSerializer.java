package sonic.sync.core.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

public class JavaSerializer implements ISerialize {
	
	private static final Logger logger = Logger.getLogger("JavaSerializer.class");

	@Override
	public byte[] serialize(Serializable object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		byte[] result = null;

		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			result = baos.toByteArray();
		} catch (IOException e) {
			logger.severe("Exception while serializing object:" + e.getMessage());
			throw e;
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				logger.severe("Exception while closing serialization process." + e.getMessage());
			}
		}
		return result;
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
