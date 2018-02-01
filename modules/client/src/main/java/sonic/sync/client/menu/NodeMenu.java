package sonic.sync.client.menu;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.configuration.NetworkConfiguration;
import sonic.sync.core.event.FileEventListener;
import sonic.sync.core.network.Node;
import sonic.sync.core.security.BCSecurityClassProvider;
import sonic.sync.core.security.DefaultEncryption;
import sonic.sync.core.serializer.FSTSerializer;
import sonic.sync.core.serializer.ISerialize;
import sonic.sync.core.serializer.JavaSerializer;
import sonic.sync.core.util.Constants;

public class NodeMenu extends ConsoleMenu {

	private ConsoleMenuItem createNetworkMenuItem;
	private ConsoleMenuItem connectToExistingNetworkItem;
	private Node node;

	private BigInteger maxFileSize = Constants.DEFAULT_MAX_FILE_SIZE;
	private int chunkSize = Constants.DEFAULT_CHUNK_SIZE;

	public NodeMenu(MenuContainer menus) {
		super(menus);
	}

	@Override
	public void createItems() {
		createNetworkMenuItem = new ConsoleMenuItem("Create New Network") {
			protected void execute() throws UnknownHostException {
				buildNode();
				connectNode(NetworkConfiguration.createInitial(askNodeID()));
			}
		};


		connectToExistingNetworkItem = new ConsoleMenuItem("Connect to Existing Network") {
			protected void execute() throws UnknownHostException {
				String nodeID = askNodeID();

				print("Specify Bootstrap Address:");
				InetAddress bootstrapAddress = InetAddress.getByName(awaitStringParameter());

				String bootstrapPort = "default";

				buildNode();
				NetworkConfiguration config = NetworkConfiguration.create(nodeID, bootstrapAddress);
				if (!"default".equalsIgnoreCase(bootstrapPort)) {
					config.setBootstrapPort(Integer.parseInt(bootstrapPort));
				}

				connectNode(config);
			}
		};

	}

	protected void connectNode(NetworkConfiguration networkConfig) {
		String bindPort = "auto";

		if (!"auto".equalsIgnoreCase(bindPort)) {
			networkConfig.setPort(Integer.parseInt(bindPort));
		}

		if (node.connect(networkConfig)) {

			print("Network connection successfully established.");
			// connect the event bus
			node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));

			String address = config.getAddress();
			if (!"auto".equalsIgnoreCase(address)) {
				try {
					InetAddress inetAddress = InetAddress.getByName(address);
					print("Binding to address " + inetAddress);
					// TO DO
					// connect to peers
				} catch (UnknownHostException e) {
					print("Cannot resolve address " + address);
				}
			}

			exit();
		} else {
			print("Network connection could not be established.");
		}

	}

	protected String askNodeID() {
		String nodeID = UUID.randomUUID().toString();
		return nodeID;
	}

	protected void buildNode() {
		FileConfiguration fileConfig = FileConfiguration.createCustom(maxFileSize, chunkSize);
		ISerialize serializer;
		if ("java".equalsIgnoreCase(config.getSerializer().getMode())) {
			serializer = new JavaSerializer();
		} else {
			serializer = new FSTSerializer(config.getSerializer().getFst().isUnsafe(), new BCSecurityClassProvider());
		}

		node = Node.createNode(fileConfig, new DefaultEncryption(serializer), serializer);

	}

	@Override
	public void onMenuExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInstruction() {
		return "Do you want to create a new network or connect to an existing one?";
	}

	@Override
	public void addMenuItems() {
		add(createNetworkMenuItem);
		add(connectToExistingNetworkItem);

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public boolean createNetwork() {
		if (getNode() == null) {
			ConsoleMenuItem.printPrecondition("You are not connected to a network. Connect to a network first.");
			open();
		}
		return getNode() != null;
	}

	public Node getNode() {
		return node;
	}

}
