package sonic.sync.core.step;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import com.frostwire.jlibtorrent.AddTorrentParams;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.TorrentBuilder;
import com.frostwire.jlibtorrent.TorrentFlags;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.entry;

import sonic.sync.core.file.process.AddFileProcessContext;
import sonic.sync.core.network.NetworkManager;

public class PrepareMagnetCreationStep implements IStep {
	
	private AddFileProcessContext context;
	private NetworkManager networkManager;

	public PrepareMagnetCreationStep(AddFileProcessContext context, NetworkManager networkManager) {
		this.context = context;
		this.networkManager = networkManager;
	}

	@Override
	public void execute() {
		File file = context.consumeFile();
		TorrentBuilder.Result result = null;
		try {
			String hostName = networkManager.getConfiguration().getBootstrapAddress().getHostAddress();
			result = new TorrentBuilder().path(file)
					.creator(hostName)
					.setPrivate(false)
					.addNode(new Pair<String, Integer>(hostName, 6881))
					.generate();
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
		File torrentFile = null;
		if (result != null) {
			entry torrentEntry = result.entry().swig();
			torrentFile = new File(FileUtils.getTempDirectory() + File.separator + file.getName() + ".torrent");
			context.setTorrent(torrentFile);
			try (OutputStream os = new FileOutputStream(torrentFile)) {
				os.write(Vectors.byte_vector2bytes(torrentEntry.bencode()));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} 

		}
		if (torrentFile == null) {
			System.out.println("Could not generate metalink.");
		}
		TorrentInfo torrentInfo = new TorrentInfo(torrentFile);
		String makeMagnetUri = torrentInfo.makeMagnetUri();
		System.out.println("Set metalink to " + makeMagnetUri);
		context.setMagnetLink(makeMagnetUri);
		seedTorrent(torrentFile, context.consumeRoot());
	}
	
	private void seedTorrent(File torrentFile, File consumeRoot) {
		AddTorrentParams torrentParams = new AddTorrentParams();
		TorrentInfo ti = new TorrentInfo(torrentFile);
		torrentParams.torrentInfo(ti);
		torrentParams.savePath(consumeRoot.getAbsolutePath());
		torrentParams.flags(TorrentFlags.SEED_MODE);
		networkManager.getSessionHandler().addTorrent(torrentParams, new ErrorCode()).swig();

	}

}
