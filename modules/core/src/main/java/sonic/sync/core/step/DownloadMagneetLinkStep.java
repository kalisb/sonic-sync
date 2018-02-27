package sonic.sync.core.step;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.SessionHandle;
import com.frostwire.jlibtorrent.TcpEndpoint;
import com.frostwire.jlibtorrent.TorrentHandle;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.swig.torrent_status;

import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.file.process.AddFileProcessContext;
import sonic.sync.core.network.NetworkManager;
import zmq.io.net.tcp.TcpAddress;

public class DownloadMagneetLinkStep implements IStep {

	private File file;
	private String magnetLink;
	private NetworkManager networkManager;

	public DownloadMagneetLinkStep(AddFileProcessContext context, NetworkManager networkManager) {
		this.file = context.consumeFile();
		this.magnetLink = context.getMagnetLink();
		this.networkManager = networkManager;
	}

	@Override
	public void execute() {
		byte[] data = networkManager.getSessionManager().fetchMagnet(magnetLink, 1000);
		try {
			File f = File.createTempFile("test", file.getName() + System.nanoTime() + ".torrent");
			FileUtils.writeByteArrayToFile(
					f, data);
			File savePath = networkManager.getSession().getRootFile();
			System.out.println("Downloading in: " + savePath.getAbsolutePath());
			//networkManager.getSessionManager().download(new TorrentInfo(f), 
			//		savePath);
			AddTorrentParams params = new AddTorrentParams();
			params.savePath(savePath.getAbsolutePath());
			params.torrentInfo(new TorrentInfo(f));
			List<TcpEndpoint> address = new ArrayList<>();
			for (String peer : networkManager.getConfiguration().getPeers()) {
				address.add(new TcpEndpoint(peer, 6881));
			}
			params.peers(address);
			SessionHandle handleSession = networkManager.getSessionHandler();
			TorrentHandle handle = handleSession.addTorrent(params, new ErrorCode());
			torrent_status status = handle.swig().status();
			while(!status.getIs_finished()) {
				status = handle.swig().status();
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
			
		} catch (IOException | NoSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
