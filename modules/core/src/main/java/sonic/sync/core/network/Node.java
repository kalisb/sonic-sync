package sonic.sync.core.network;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.configuration.NetworkConfiguration;
import sonic.sync.core.message.FileManager;
import sonic.sync.core.message.NetworkManager;
import sonic.sync.core.security.IEncryption;
import sonic.sync.core.security.UserManager;
import sonic.sync.core.serializer.ISerialize;

public class Node {

	private final FileConfiguration fileConfiguration;
	private final NetworkManager networkManager;
	private FileManager fileManager;
	private UserManager userManager;
	
	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	private Node(FileConfiguration fileConfiguration, IEncryption encryption, ISerialize serializer) {
		this.fileConfiguration = fileConfiguration;
		this.networkManager = new NetworkManager(encryption, serializer, fileConfiguration);
	}

	public static Node createNode(FileConfiguration fileConfiguration, IEncryption encryption,
			ISerialize serializer) {
		return new Node(fileConfiguration, encryption, serializer);
	}

	public boolean connect(NetworkConfiguration networkConfig) {
		return networkManager.connect(networkConfig);
	}

	public FileManager getFileManager() {
		if (fileManager == null) {
			this.fileManager = new FileManager(networkManager, fileConfiguration);
		}
		return fileManager;
	}

	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager(networkManager);
		}
		return userManager;
	}


}
