package sonic.sync.core.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sonic.sync.core.file.watcher.FileObserverListener;
import sonic.sync.core.logger.SSLogger;
import sonic.sync.core.logger.SSLoggerFactory;
import sonic.sync.core.security.EncryptionUtil;
import sonic.sync.core.util.Constants;

public class FileManager {
	enum Operations {
		ADD,
		DELETE,
		UPDATE, DOWNLOAD
	}

	private static final SSLogger logger = SSLoggerFactory.getLogger(FileManager.class);
	private final Path root;

	public static String getFileSep() {
		String fileSep = System.getProperty("file.separator");
		if (fileSep.equals("\\"))
			fileSep = "\\\\";
		return fileSep;
	}

	// holds persistent meta data
	private final Path h2hMetaFile;

	public FileManager(String rootDirectory) {
		this(Paths.get(rootDirectory));
	}

	public FileManager(Path rootDirectory) {
		root = rootDirectory;
		if (!root.toFile().exists()) {
			root.toFile().mkdirs();
		}

		h2hMetaFile = Paths.get(root.toString(), Constants.META_FILE_NAME);
	}

	/**
	 * Returns the root node
	 * 
	 * @return
	 */
	public Path getRoot() {
		return root;
	}

	/**
	 * Writes the meta data (used to synchronize) to the disk
	 */
	public void writePersistentMetaData() {
		// generate the new persistent meta data
		PersistentMetaData metaData = new PersistentMetaData();
		try {
			PersistenceFileVisitor visitor = new PersistenceFileVisitor(root);
			Files.walkFileTree(root, visitor);
			metaData.setFileTree(visitor.getFileTree());

			byte[] encoded = EncryptionUtil.serializeObject(metaData);
			FileUtils.writeByteArrayToFile(h2hMetaFile.toFile(), encoded);
		} catch (IOException e) {
			logger.error("Cannot write the meta data", e);
		}
	}


	/**
	 * Reads the meta data (used to synchronize) from the disk
	 * 
	 * @return the read meta data (never null)
	 */
	public PersistentMetaData getPersistentMetaData() {
		try {
			byte[] content = FileUtils.readFileToByteArray(h2hMetaFile.toFile());
			PersistentMetaData metaData = (PersistentMetaData) EncryptionUtil.deserializeObject(content);
			return metaData;
		} catch (IOException | ClassNotFoundException e) {
			logger.error("Cannot read the last meta data");
			return new PersistentMetaData();
		}
	}
/*
	public void createAddProcess(File file) throws NoSessionException,
	IllegalFileLocation {
		Session session = networkManager.getSession();
		if (file == null) {
			throw new IllegalArgumentException("File cannot be null.");
		} else if (!file.exists()) {
			throw new IllegalArgumentException("File does not exist.");
		} else if (session.getRootFile().equals(file)) {
			throw new IllegalArgumentException("Root cannot be added.");
		} else if (!FileUtil.isInSharedDirectory(session.getFileAgent(), file)) {
			throw new IllegalArgumentException("File is not within the root file tree.");
		}

		createAddFileProcess(file, networkManager, fileConfiguration);
	}

	private void createAddFileProcess(File file, NetworkManager networkManager, FileConfiguration fileConfiguration) throws NoSessionException, IllegalFileLocation {
		if (file == null) {
			throw new IllegalArgumentException("File can't be null.");
		}
		Session session = networkManager.getSession();
		DataManager dataManager = networkManager.getDataManager();
		AddFileProcessContext context = new AddFileProcessContext(file, session, fileConfiguration,
				networkManager.getEncryption());

		// process composition
		SyncProcess process = new SyncProcess();

		process.add(new ValidateFileStep(context));
		process.add(new CheckWriteAccessStep(context, session.getProfileManager()));
		process.add(new AddIndexToUserProfileStep(context, session.getProfileManager()));
		process.add(new PrepareMagnetCreationStep(context, networkManager));
		process.add(createNotificationProcess(context, Operations.ADD));
		process.execute();
	}

	private IStep createNotificationProcess(final IUpdateContext context, final Operations operation) {
		return new IStep() {

			@Override
			public void execute() {
				networkManager.publish(networkManager.getConfiguration().getNodeID());
				networkManager.publish(operation.name());
				networkManager.publish(context.consumeFile().getName());
				networkManager.publish(context.getMagnetLink());
			}
		};
	}

	public void createDeleteProcess(File file) throws NoSessionException,
	IllegalArgumentException {

		if (file == null) {
			throw new IllegalArgumentException("File cannot be null");
		} else if (!FileUtil.isInSharedDirectory(networkManager.getSession().getFileAgent(), file)) {
			throw new IllegalArgumentException("File is not in the Hive2Hive directory");
		} else if (file.isDirectory() && file.list().length > 0) {
			throw new IllegalArgumentException("Folder to delete is not empty");
		}

		createDeleteFileProcess(file, networkManager);
	}

	private void createDeleteFileProcess(File file, NetworkManager networkManager) throws NoSessionException, GetFailedException, SSException {
		Session session = networkManager.getSession();

		DeleteFileProcessContext context = new DeleteFileProcessContext(file, session, networkManager.getEncryption());

		// process composition
		SyncProcess process = new SyncProcess();

		//process.add(new DeleteFromUserProfileStep(context, networkManager));
		process.add(createNotificationProcess(context, Operations.DELETE));

		process.execute();
	}

	public void createUpdateProcess(File file) throws NoSessionException,
	IllegalArgumentException {

		if (file == null) {
			throw new IllegalArgumentException("File cannot be null");
		} else if (file.isDirectory()) {
			throw new IllegalArgumentException("A folder can have one version only");
		} else if (!file.exists()) {
			throw new IllegalArgumentException("File does not exist");
		} else if (!FileUtil.isInSharedDirectory(networkManager.getSession().getFileAgent(), file)) {
			throw new IllegalArgumentException("File is not in the Hive2Hive directory");
		}

		createUpdateFileProcess(file, networkManager, fileConfiguration);
	}

	private void createUpdateFileProcess(File file, NetworkManager networkManager,
			FileConfiguration fileConfiguration) throws NoSessionException, SSException {
		DataManager dataManager = networkManager.getDataManager();
		Session session = networkManager.getSession();
		AddFileProcessContext context = new AddFileProcessContext(file, session, fileConfiguration,
				networkManager.getEncryption());

		// process composition
		SyncProcess process = new SyncProcess();

		process.add(new ValidateFileStep(context));
		process.add(new CheckWriteAccessStep(context, session.getProfileManager()));
		process.add(new CreateNewVersionStep(context));
		process.add(new UpdateHashInUserProfileStep(context, session.getProfileManager()));
		process.add(new PrepareMagnetCreationStep(context, networkManager));
		process.add(createNotificationProcess(context, Operations.UPDATE));

		process.execute();
	}

	public void createDownloadProcess(File file, String magnetLink) throws NoPeerConnectionException, NoSessionException,
	IllegalArgumentException, SSException {
		createDownloadFileProcess(file, magnetLink, networkManager);
	}

	private void createDownloadFileProcess(File file, String magnetLink, NetworkManager networkManager) throws NoSessionException, SSException {
		Session session = networkManager.getSession();

		AddFileProcessContext context = new AddFileProcessContext(file, session, fileConfiguration, networkManager.getEncryption());

		// process composition
		SyncProcess process = new SyncProcess();
		context.setMagnetLink(magnetLink);

		process.add(new DownloadMagneetLinkStep(context, networkManager));
		process.add(createNotificationProcess(context, Operations.DOWNLOAD));

		process.execute();

	}

	public void createMoveProcess(File source, File destination) throws NoSessionException,
	NoPeerConnectionException, IllegalArgumentException {

		FileAgent fileAgent = networkManager.getSession().getFileAgent();

		if (source == null) {
			throw new IllegalArgumentException("Source cannot be null");
		} else if (destination == null) {
			throw new IllegalArgumentException("Destination cannot be null");
		} else if (!FileUtil.isInSharedDirectory(fileAgent, source)) {
			throw new IllegalArgumentException("Source file not in the Hive2Hive directory");
		} else if (!FileUtil.isInSharedDirectory(fileAgent, destination)) {
			throw new IllegalArgumentException("Destination file not in the Hive2Hive directory");
		}

		createMoveFileProcess(source, destination, networkManager);
	}

	private void createMoveFileProcess(File source, File destination, NetworkManager networkManager2) {
		// TODO Auto-generated method stub

	}

	public void createShareProcess(File folder, String userId, PermissionType permission)
			throws NoPeerConnectionException, NoSessionException, IllegalArgumentException {

		if (folder == null) {
			throw new IllegalArgumentException("Folder to share cannot be null");
		} else if (!folder.isDirectory()) {
			throw new IllegalArgumentException("File has to be a folder.");
		} else if (!folder.exists()) {
			throw new IllegalArgumentException("Folder does not exist.");
		}

		Session session = networkManager.getSession();

		// folder must be in the given root directory
		if (!FileUtil.isInSharedDirectory(session.getFileAgent(), folder)) {
			throw new IllegalArgumentException("Folder must be in root of the SS directory.");
		}

		// sharing root folder is not allowed
		if (folder.equals(session.getRootFile())) {
			throw new IllegalArgumentException("Root folder of the SS directory can't be shared.");
		}

		createShareProcess(folder, new UserPermission(userId, permission), networkManager);
	}

	private void createShareProcess(File folder, UserPermission userPermission, NetworkManager networkManager2) {
		// TODO Auto-generated method stub

	}

	public FileNode createFileListProcess() throws NoPeerConnectionException, NoSessionException {
		return createFileListProcess(networkManager);
	}

	private FileNode createFileListProcess(NetworkManager networkManager2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void subscribeFileEvents(FileAlterationListener listener, List<String> peers) {
		if (listener == null) {
			throw new IllegalArgumentException("The argument listener must not be null.");
		}

		networkManager.subscribe(peers);
	}
*/
	/**
	 * Returns the file on disk from a file node of the user profile
	 * 
	 * @param fileToFind
	 * @return the path to the file or null if the parameter is null
	 */
	public Path getPath(FileTreeNode fileToFind) {
		if (fileToFind == null)
			return null;
		return Paths.get(root.toString(), fileToFind.getFullPath().toString());
	}

	public void subscribeFileEvents(FileObserverListener fileObserverListener, List<String> peers) {
		// TODO Auto-generated method stub
		
	}


}
