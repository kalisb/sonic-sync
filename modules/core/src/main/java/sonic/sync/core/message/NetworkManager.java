package sonic.sync.core.message;

import java.io.File;
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
import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.NoPeerConnectionException;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.exception.SSException;
import sonic.sync.core.network.DownloadManager;
import sonic.sync.core.security.DataManager;
import sonic.sync.core.security.IEncryption;
import sonic.sync.core.serializer.ISerialize;

public class NetworkManager {
	private NetworkConfiguration config;
	private IEncryption encryption;
	private ISerialize serializer;
	private String nodeID;
	private Session session;
	private  DataManager dataManager;
	private ZContext conection;
	private SessionManager sessionManager;
    private SessionHandle sessionHandle;
    private Socket publishChannel;
    

	public NetworkManager(IEncryption encryption, ISerialize serializer, FileConfiguration fileConfiguration) {
		this.encryption = encryption;
		this.serializer = serializer;
		System.setProperty("jlibtorrent.jni.path", "/home/kalisb/sonic-sync/modules/client/libjlibtorrent.so");
        System.out.println("Using libtorrent version: " + LibTorrent.version());

		this.sessionManager = new SessionManager();
		this.sessionManager.addListener(new AlertListener() {

            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                System.out.println(alert.message());
            }

        });
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
		dataManager = new DataManager(networkConfig.getContext(), serializer, encryption, sessionManager);
		nodeID = networkConfig.getNodeID();
		//  Bind state backend to endpoint
		publishChannel = networkConfig.getContext().createSocket(ZMQ.PUB);
		conection = networkConfig.getContext();
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

	public void publish(String data) {
		publishChannel.send(data);
	}
	
	public void subscribe(List<String> peers) {
		final NetworkManager reference = this;
		final Socket statefe = conection.createSocket(ZMQ.SUB);
		statefe.subscribe("".getBytes());
		for (String peer : peers) {
			System.out.printf("I: connecting to state backend at '%s'\n", peer);
			statefe.connect(String.format("tcp://%s:5563", peer));
		}
		final Poller poller = conection.createPoller(1);
		poller.register(statefe, Poller.POLLIN);

		Thread pollThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					//  Poll for activity, or 1 second timeout
					int rc = poller.poll(1000);
					if (rc == -1)
						break; //  Interrupted

					//  Handle incoming status messages
					if (poller.pollin(0)) {
						String peer_name = new String(statefe.recv(0), ZMQ.CHARSET);
						String command_name = new String(statefe.recv(0), ZMQ.CHARSET);
						String file = new String(statefe.recv(0), ZMQ.CHARSET);
						String magnetLink = new String(statefe.recv(0), ZMQ.CHARSET);
						System.out.printf("%s says %s %s\n", peer_name, command_name, file);
						FileManager fileManager = new FileManager(reference, null);
						switch (command_name) {
						case "ADD":
							try {
								System.out.println(magnetLink);
								fileManager.createDownloadProcess(new File(file), magnetLink);
							} catch (IllegalArgumentException | NoPeerConnectionException | SSException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
							
						case "DELETE":
							try {
								fileManager.createDeleteProcess(new File(file));
							} catch (IllegalArgumentException | NoPeerConnectionException | GetFailedException | SSException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
							
						case "UPDATE":
							try {
								fileManager.createDownloadProcess(new File(file), magnetLink);
							} catch (IllegalArgumentException | NoPeerConnectionException | SSException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						default:
							break;
						}
					}
				}
				
			}
		});
		pollThread.start();
	}

}
