package sonic.sync.core.network.data;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Arrays;

import javax.crypto.SecretKey;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.Entry;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.SessionManager.MutableItem;
import com.frostwire.jlibtorrent.Sha1Hash;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

import sonic.sync.core.serializer.ISerialize;

public class ContentListener implements AlertListener {

	private NetworkContent content;
	private SessionManager sessionManager;
	private SecretKey locationKey;
	private String contentKey;
	private ISerialize serializer;

	public ContentListener(SecretKey keyPair, String contentKey, SessionManager sessionManager, ISerialize serializer) {
		this.locationKey = keyPair;
		this.contentKey = contentKey;
		this.sessionManager = sessionManager;
		this.serializer = serializer;
	}

	public NetworkContent getContent() {
		return content;
	}

	@Override
	public int[] types() {
		return new int[]{
				AlertType.DHT_PUT.swig()
		};
	}

	@Override
	public void alert(Alert<?> alert) {
		System.out.println(alert.message());
		if (locationKey != null) {
			byte[] salt = new byte[16];
			Arrays.fill(salt,(byte)0);
			MutableItem entry = sessionManager.dhtGetItem(locationKey.getEncoded(), salt, 1000);
			System.out.println("Dictionary " + entry.item.dictionary());
			Entry profile = entry.item.dictionary().get(contentKey);
			try {
				content = (NetworkContent) serializer.deserialize(profile.bencode());
				System.out.println(content);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setContent(NetworkContent deserialize) {
		this.content = deserialize;
	}
}
