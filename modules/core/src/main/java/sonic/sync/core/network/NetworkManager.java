package sonic.sync.core.network;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.Pair;
import com.frostwire.jlibtorrent.SessionHandle;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.alerts.Alert;
import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.configuration.NetworkConfiguration;
import sonic.sync.core.configuration.Serializer;
import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.exception.SSException;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.DownloadManager;
import sonic.sync.core.network.data.DataManager;
import sonic.sync.core.network.message.ContactPeerMessage;
import sonic.sync.core.network.message.IResponseCallBackHandler;
import sonic.sync.core.network.message.MessageManager;
import sonic.sync.core.network.message.PeerAddress;
import sonic.sync.core.network.message.ResponseMessage;
import sonic.sync.core.network.message.Session;
import sonic.sync.core.security.IEncryption;
import sonic.sync.core.serializer.ISerialize;

public class NetworkManager {

	private static final SSLogger logger = SSLoggerFactory.getLogger(NetworkManager.class);

	private NetworkConfiguration config;
	private IEncryption encryption;
	private ISerialize serializer;

	private String nodeID;
	private ZContext conection;
	private static DataManager dataManager;
	private MessageManager messageManager;
	private Socket publishChannel;

	private SessionManager sessionManager;
	private SessionHandle sessionHandle;
	private Session session;


	public NetworkManager(IEncryption encryption, ISerialize serializer, FileConfiguration fileConfiguration) {
		this.encryption = encryption;
		this.serializer = serializer;
		System.setProperty("jlibtorrent.jni.path", "/home/kalisb/sonic-sync/modules/client/libjlibtorrent.so");
        System.out.println("Using libtorrent version: " + LibTorrent.version());

  		this.sessionManager = new SessionManager();
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public boolean connect(NetworkConfiguration networkConfig) {
		sessionManager.start();
		sessionManager.startDht();
		sessionHandle = new SessionHandle(sessionManager.swig());
		for (String peer : networkConfig.getPeers()) {
			sessionHandle.addDHTNode(new Pair<String, Integer>(peer, 6881));
		}
		final CountDownLatch signal = new CountDownLatch(1);

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long nodes = sessionManager.stats().dhtNodes();
				// wait for at least 10 nodes in the DHT.
				if (nodes >= 1) {
					System.out.println("DHT contains " + nodes + " nodes");
					signal.countDown();
					timer.cancel();
				}
			}
		}, 0, 1000);

		System.out.println("Waiting for nodes in DHT (10 seconds)...");
		boolean r;
		try {
			r = signal.await(80, TimeUnit.SECONDS);
			if (!r) {
				System.out.println("DHT bootstrap timeout");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config = networkConfig;
		//dataManager = new DataManager(networkConfig.getContext(), serializer, encryption, sessionManager);
		nodeID = networkConfig.getNodeID();
		//  Bind state backend to endpoint
		publishChannel = networkConfig.getContext().createSocket(ZMQ.PUB);
		conection = networkConfig.getContext();
		this.dataManager = new DataManager(conection, serializer, encryption, sessionManager);
		return publishChannel.bind("tcp://*:5563");
	}

	public NetworkConfiguration getConfiguration() {
		return config;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public DownloadManager getDownloadManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MessageManager getMessageManager() {
		// TODO Auto-generated method stub
		return null;
	}

	public Session getSession() throws NoSessionException {
		if (session == null) {
			throw new NoSessionException();
		}
		return session;
	}

	public ZContext getConnection() {
		return conection;
	}

	public IEncryption getEncryption() {
		return encryption;
	}

	public String getUserId() {
		return nodeID;
	}

	public SessionHandle getSessionHandler() {
		return sessionHandle;
	}

	public PeerAddress getPeerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public PublicKey getPublicKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean sendDirect(ContactPeerMessage contactMsg, PublicKey publicKey) {
		// TODO Auto-generated method stub
		return false;
	}

	public void publish(ResponseMessage response, PublicKey senderPublicKey) throws IOException {
		publishChannel.send(serializer.serialize(response));
	}

	public String getNodeId() {
		return nodeID;
	}

	public ISerialize getSerializer() {
		return serializer;
	}

}
