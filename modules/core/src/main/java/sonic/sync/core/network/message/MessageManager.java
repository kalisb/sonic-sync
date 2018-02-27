package sonic.sync.core.network.message;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.SSException;
import sonic.sync.core.file.FileManager;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.network.NetworkManager;

public class MessageManager {
	
	private static final SSLogger logger = SSLoggerFactory.getLogger(MessageManager.class);

	private final NetworkManager networkManager;
	private final HashMap<String, IResponseCallBackHandler> callBackHandlers;

	public MessageManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
		this.callBackHandlers = new HashMap<String, IResponseCallBackHandler>();
	}

	public IResponseCallBackHandler getCallBackHandler(String messageID) {
		return callBackHandlers.remove(messageID);
	}

	public void init() {
		ZContext connection = networkManager.getConnection();
		final Socket statefe = connection.createSocket(ZMQ.SUB);
		statefe.subscribe("".getBytes());
		for (String peer : networkManager.getConfiguration().getPeers()) {
			System.out.printf("I: connecting to state backend at '%s'\n", peer);
			statefe.connect(String.format("tcp://%s:5563", peer));
		}
		final Poller poller = connection.createPoller(1);
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
						try {
							IResponseCallBackHandler handler = (IResponseCallBackHandler) networkManager.getSerializer().deserialize(statefe.recv(0));
						} catch (ClassNotFoundException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		});
		pollThread.start();
		
	}
}
