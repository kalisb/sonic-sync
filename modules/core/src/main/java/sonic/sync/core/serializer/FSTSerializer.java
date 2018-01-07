package sonic.sync.core.serializer;

import java.io.IOException;
import java.io.Serializable;

import sonic.sync.core.security.BCSecurityClassProvider;

public class FSTSerializer  implements ISerialize {

	public FSTSerializer(boolean unsafe, BCSecurityClassProvider bcSecurityClassProvider) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] serialize(Serializable object) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
