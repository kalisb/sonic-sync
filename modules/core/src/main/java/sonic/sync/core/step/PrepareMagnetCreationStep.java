package sonic.sync.core.step;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.TorrentBuilder;
import com.frostwire.jlibtorrent.TorrentInfo;
import com.frostwire.jlibtorrent.Vectors;
import com.frostwire.jlibtorrent.swig.entry;

import sonic.sync.core.file.AddFileProcessContext;
import sonic.sync.core.message.NetworkManager;

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
			System.out.println(networkManager.getConfiguration().getBootstrapAddress().getHostAddress());
			String hostName = networkManager.getConfiguration().getNodeID();
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
	}

}
