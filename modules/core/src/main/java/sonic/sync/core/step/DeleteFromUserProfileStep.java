package sonic.sync.core.step;

import java.io.File;

import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.exception.NoSessionException;
import sonic.sync.core.file.FileTreeNode;
import sonic.sync.core.file.process.DeleteFileProcessContext;
import sonic.sync.core.file.process.IUpdateContext;
import sonic.sync.core.network.NetworkManager;
import sonic.sync.core.security.UserProfile;

public class DeleteFromUserProfileStep implements IStep {

	private IUpdateContext context;
	private NetworkManager networkManager;
	private UserProfile userProfile;

	public DeleteFromUserProfileStep(DeleteFileProcessContext context, NetworkManager networkManager) throws GetFailedException, NoSessionException {
		this.context = context;
		this.networkManager = networkManager;
		this.userProfile = networkManager.getSession().getProfileManager().readUserProfile();
		
	}

	@Override
	public void execute() {
		File file = context.consumeFile();
		File root = context.consumeRoot();
/*
		FileTreeNode fileIndex = userProfile.getFileByPath(file, root);

		// validate
		if (fileIndex == null) {
			throw new AbortModifyException(AbortModificationCode.FILE_INDEX_NOT_FOUND, "File index not found in user profile");
		} else if (!fileIndex.canWrite()) {
			throw new AbortModifyException(AbortModificationCode.NO_WRITE_PERM, "Not allowed to delete this file (read-only permissions)");
		}

		// check preconditions
		if (fileIndex.isFolder()) {
			FolderIndex folder = (FolderIndex) fileIndex;
			if (!folder.getChildren().isEmpty()) {
				throw new AbortModifyException(AbortModificationCode.NON_EMPTY_DIR, "Cannot delete a directory that is not empty.");
			}
		}

		// remove the node from the tree
		FolderIndex parentIndex = fileIndex.getParent();
		parentIndex.removeChild(fileIndex);

		// store for later
		context.provideIndex(fileIndex);*/
	}

}
