package sonic.sync.core.message;

import java.io.File;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationListener;

import sonic.sync.core.configuration.FileConfiguration;
import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.NoPeerConnectionException;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.exception.SSException;
import sonic.sync.core.file.AddFileProcessContext;
import sonic.sync.core.file.DeleteFileProcessContext;
import sonic.sync.core.file.FileAgent;
import sonic.sync.core.file.FileNode;
import sonic.sync.core.file.IUpdateContext;
import sonic.sync.core.file.UpdateFileProcessContext;
import sonic.sync.core.security.DataManager;
import sonic.sync.core.security.UserPermission;
import sonic.sync.core.security.UserPermission.PermissionType;
import sonic.sync.core.step.AddIndexToUserProfileStep;
import sonic.sync.core.step.CheckWriteAccessStep;
import sonic.sync.core.step.DeleteFromUserProfileStep;
import sonic.sync.core.step.DownloadMagneetLinkStep;
import sonic.sync.core.step.IStep;
import sonic.sync.core.step.PrepareMagnetCreationStep;
import sonic.sync.core.step.ValidateFileStep;
import sonic.sync.core.util.FileUtil;

public class FileManager {
	enum Operations {
		ADD,
		DELETE,
		UPDATE, DOWNLOAD
	}

	private final FileConfiguration fileConfiguration;
	private final NetworkManager networkManager;

	public FileManager(NetworkManager networkManager, FileConfiguration fileConfiguration) {
		this.networkManager = networkManager;
		this.fileConfiguration = fileConfiguration;
	}

	public void createAddProcess(File file) throws NoPeerConnectionException, NoSessionException,
	IllegalArgumentException, SSException {

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

	private void createAddFileProcess(File file, NetworkManager networkManager, FileConfiguration fileConfiguration) throws NoSessionException, SSException {
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

	public void createDeleteProcess(File file) throws NoPeerConnectionException, NoSessionException,
	IllegalArgumentException, GetFailedException, SSException {

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

		process.add(new DeleteFromUserProfileStep(context, networkManager));
		process.add(createNotificationProcess(context, Operations.DELETE));

		process.execute();
	}

	public void createUpdateProcess(File file) throws NoPeerConnectionException, NoSessionException,
	IllegalArgumentException, SSException {

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
		/*process.add(new CreateNewVersionStep(context));
		process.add(new UpdateHashInUserProfileStep(context, session.getProfileManager()));*/
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


}
