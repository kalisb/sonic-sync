package sonic.sync.core.configuration;

import java.net.InetAddress;

import org.zeromq.ZContext;

import sonic.sync.core.util.Constants;

public class NetworkConfiguration {

	private static final int AUTO_PORT = -1;

	public NetworkConfiguration(String string, Object object) {
		// TODO Auto-generated constructor stub
	}

	public static NetworkConfiguration createInitial(String nodeID) {
		ctx = new ZContext();
		return new NetworkConfiguration(nodeID, null).setPort(AUTO_PORT);
	}

	private String nodeID;
	private int port;
	private int bootstrapPort;
	private InetAddress bootstrapAddress;
	private static ZContext ctx;

	private NetworkConfiguration setNodeId(String nodeID) {
		this.nodeID = nodeID;
		return this;
	}

	public ZContext getContext() {
		return ctx;
	}

	public NetworkConfiguration setBootstrap(InetAddress bootstrapAddress) {
		return setBootstrap(bootstrapAddress, Constants.PORT);
	}

	public NetworkConfiguration setBootstrap(InetAddress bootstrapAddress, int bootstrapPort) {
		this.bootstrapAddress = bootstrapAddress;
		this.bootstrapPort = bootstrapPort;
		return this;
	}

	public NetworkConfiguration setBootstrapPort(int bootstrapPort) {
		this.bootstrapPort = bootstrapPort;
		return this;
	}

	public static NetworkConfiguration create(String nodeID, InetAddress bootstrapAddress) {
		ctx = new ZContext();
		return new NetworkConfiguration(nodeID, null).setBootstrap(bootstrapAddress);
	}

	public NetworkConfiguration setPort(int port) {
		this.port = port;
		return this;
	}

	public String getNodeID() {
		return nodeID;
	}

	public int getPort() {
		return port;
	}
	
	public InetAddress getBootstrapAddress() {
		return bootstrapAddress;
	}
	
	public int getBootstrapPort() {
		return bootstrapPort;
	}

	public String resolve(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
