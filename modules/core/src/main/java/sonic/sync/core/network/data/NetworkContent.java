package sonic.sync.core.network.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.Sha1Hash;

import sonic.sync.core.network.NetworkManager;
import sonic.sync.core.security.EncryptionUtil;

public abstract class NetworkContent implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Some data has a version key (used to differentiate versions). Default value.
	 */
	transient
	private Sha1Hash versionKey = Sha1Hash.min();

	/**
	 * Some data is based on other data. Default value.
	 */
	transient
	private Sha1Hash basedOnKey = Sha1Hash.min();

	public Sha1Hash getVersionKey() {
		return versionKey;
	}

	public void setVersionKey(Sha1Hash versionKey) {
		this.versionKey = versionKey;
	}

	public Sha1Hash getBasedOnKey() {
		return basedOnKey;
	}

	public void setBasedOnKey(Sha1Hash versionKey) {
		this.basedOnKey = versionKey;
	}

	public void generateVersionKey(NetworkManager networkManager) throws IOException {
		// get a MD5 hash of the object itself
		byte[] hash = EncryptionUtil.generateMD5Hash(EncryptionUtil.serializeObject(this));
		// use time stamp value and the first part of the MD5 hash as version key
		versionKey = networkManager.getSessionManager().dhtPutItem(Entry.bdecode(Arrays.copyOf(hash, 20)));
	}
}
