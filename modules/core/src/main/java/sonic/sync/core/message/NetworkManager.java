package sonic.sync.core.message;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.frostwire.jlibtorrent.AlertListener;
import com.frostwire.jlibtorrent.LibTorrent;
import com.frostwire.jlibtorrent.SessionHandle;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.alerts.Alert;
import com.frostwire.jlibtorrent.alerts.AlertType;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.configuration.NetworkConfiguration;
import sonic.sync.core.exception.NoSessionException;
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

	public NetworkManager(IEncryption encryption, ISerialize serializer, FileConfiguration fileConfiguration) {
		this.encryption = encryption;
		this.serializer = serializer;
		System.setProperty("jlibtorrent.jni.path", "C:\\Program Files\\Git\\sonic-sync\\modules\\client\\jlibtorrent.dll");
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

	public boolean connect(NetworkConfiguration networkConfig) {
		sessionManager.start();
		sessionManager.startDht();
	    sessionHandle = new SessionHandle(sessionManager.swig());
		config = networkConfig;
		dataManager = new DataManager(networkConfig.getContext(), serializer, encryption, sessionManager);
		nodeID = networkConfig.getNodeID();
		String self = networkConfig.getNodeID();
		//  Bind state backend to endpoint
		Socket statebe = networkConfig.getContext().createSocket(ZMQ.PUB);
		conection = networkConfig.getContext();
		return statebe.bind(String.format("ipc://%s-state.ipc", self));
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

}
