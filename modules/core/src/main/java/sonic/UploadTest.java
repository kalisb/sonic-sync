package sonic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.TorrentBuilder;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.address;
import com.frostwire.jlibtorrent.swig.entry;
import com.frostwire.jlibtorrent.swig.error_code;
import com.frostwire.jlibtorrent.swig.libtorrent;
import com.frostwire.jlibtorrent.swig.session;
import com.frostwire.jlibtorrent.swig.string_int_pair;
import com.frostwire.jlibtorrent.swig.tcp_endpoint;
import com.frostwire.jlibtorrent.swig.torrent_handle;
import com.frostwire.jlibtorrent.swig.torrent_status;

public class UploadTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.setProperty("jlibtorrent.jni.path", "/home/kalisb/sonic-sync/modules/core/src/resources/libjlibtorrent.so");

		File torrentPath = new File("/home/kalisb/sonic-sync/modules/core/test.txt");
		//libtorrent.set_piece_hashes(torrent, ".", new error_code());
		TorrentBuilder.Result result = new TorrentBuilder()
				.path(torrentPath)
				.creator("libtorrent " + libtorrent.boost_version())
				.comment("Test")
				.generate();
		entry torrentEntry = result.entry().swig();
		File torrentFile = new File("test.torrent");
		try (OutputStream os = new FileOutputStream(torrentFile)) {
			os.write(Vectors.byte_vector2bytes(torrentEntry.bencode()));
		}
		
		
		session session = new session();
		AddTorrentParams torrentParams = new AddTorrentParams();
		TorrentInfo ti = new TorrentInfo(torrentFile);
		torrentParams.torrentInfo(ti);
		torrentParams.savePath(".");
		torrentParams.swig().setSeeding_time(-1);
		torrent_handle handle = session.add_torrent(torrentParams.swig(), new error_code());
		handle.connect_peer(new tcp_endpoint(new Address("127.0.0.1").swig(), 6881));
		handle.connect_peer(new tcp_endpoint(new Address("127.0.0.1").swig(), 6882));
		System.out.println("Total size: " + handle.status().getTotal_wanted());
		System.out.println("Name: " + ti.makeMagnetUri());
		while(true) {
			torrent_status status = handle.status();
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

