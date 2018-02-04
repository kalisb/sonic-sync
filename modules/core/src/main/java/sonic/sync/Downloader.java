package sonic.sync;
import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.SessionHandle;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.SessionParams;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;
import com.frostwire.jlibtorrent.alerts.DhtAnnounceAlert;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;
import com.frostwire.jlibtorrent.swig.torrent_status;

public class Downloader {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("jlibtorrent.jni.path", "/home/kalisb/sonic-sync/modules/core/src/resources/libjlibtorrent.so");

		final String uri = "magnet:?xt=urn:btih:0bc6c0de1ff1d26d84187a4d42fb9a2d0bc73a1d&dn=test.txt";

		final SessionManager sessionManager = new SessionManager();
		SettingsPack settingsPack = new SettingsPack();
		settingsPack.enableDht(true);
		sessionManager.startDht();
		sessionManager.start(new SessionParams(settingsPack));
		final SessionHandle handleSession = new SessionHandle(sessionManager.swig());
		handleSession.addDHTNode(new Pair<String, Integer>("127.0.0.1", 6882));

		sessionManager.addListener(new AlertListener() {

			@Override
			public int[] types() {
				return null;
			}

			@Override
			public void alert(Alert<?> alert) {
				System.out.println(alert.message());
				if (alert.type().equals(AlertType.DHT_ANNOUNCE)) {
					DhtAnnounceAlert a = (DhtAnnounceAlert) alert;
					if (a.infoHash().equals("1c07d9f72f994cf7b14e896f43af2fd7a6439f78")) {
						AddTorrentParams params = new AddTorrentParams();
						params.savePath(".");
						params.parseMagnetUri(uri);
					
						TorrentHandle handle = handleSession.addTorrent(params, new ErrorCode());
						handle.swig().connect_peer(new tcp_endpoint(new Address("127.0.0.1").swig(), 6882));
						handle.swig().connect_peer(new tcp_endpoint(new Address("127.0.0.1").swig(), 6881));
						System.out.println(handle.isValid());
						
						while(true) {
							torrent_status status = handle.swig().status();
							System.out.println(status.getProgress() * 100 
									+ "complete (down: "+ status.getDownload_rate()
									+ " kb/s up: " + status.getUpload_rate()
									+ " kB/s peers: " + status.getNum_peers()
									+ ") " + status.getState());
							try {
								Thread.currentThread().sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		AddTorrentParams params = new AddTorrentParams();
		params.savePath(".");
		params.parseMagnetUri(uri);
	
		sessionManager.fetchMagnet(uri, 1000);
		
		TorrentHandle handle = handleSession.addTorrent(params, new ErrorCode());
		//handle.swig().connect_peer(new tcp_endpoint(new Address("127.0.0.1").swig(), 6881));
		System.out.println(handle.isValid());
		
		while(true) {
			torrent_status status = handle.swig().status();
			System.out.println(status.getProgress() * 100 
					+ "complete (down: "+ status.getDownload_rate()
					+ " kb/s up: " + status.getUpload_rate()
					+ " kB/s peers: " + status.getNum_peers()
					+ ") " + status.getState());
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//final CountDownLatch signal = new CountDownLatch(1);
	}
}

