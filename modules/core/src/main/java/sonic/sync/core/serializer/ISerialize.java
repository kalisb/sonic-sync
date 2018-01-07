package sonic.sync.core.serializer;

import java.io.IOException;
import java.io.Serializable;

public interface ISerialize {

	byte[] serialize(Serializable object) throws IOException;
	
	Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException;
}
