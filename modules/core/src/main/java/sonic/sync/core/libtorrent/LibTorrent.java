package sonic.sync.core.libtorrent;

import sonic.sync.core.libtorrent.swig.libtorrent;

public class LibTorrent {

	public static String version() {
		return libtorrent.version();
	}

}
