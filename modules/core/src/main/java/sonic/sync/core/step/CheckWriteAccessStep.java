package sonic.sync.core.step;

import java.io.File;

import sonic.sync.core.exception.GetFailedException;
import sonic.sync.core.file.AddFileProcessContext;
import sonic.sync.core.file.FolderIndex;
import sonic.sync.core.file.IUpdateContext;
import sonic.sync.core.security.UserProfile;
import sonic.sync.core.security.UserProfileManager;

public class CheckWriteAccessStep implements IStep {

	private final IUpdateContext context;
	private final UserProfileManager profileManager;

	public CheckWriteAccessStep(AddFileProcessContext context, UserProfileManager profileManager) {
		this.context = context;
		this.profileManager = profileManager;
	}

	@Override
	public void execute() {
		File file = context.consumeFile();
		File root = context.consumeRoot();

		System.err.println("Check write access in folder " + file.getParentFile().getName() + " for file " + file.getName());

		UserProfile userProfile = null;
		try {
			// fetch user profile (only read)
			userProfile = profileManager.readUserProfile();
		} catch (GetFailedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}

		// find the parent node using the relative path to navigate there
		FolderIndex parentNode = (FolderIndex) userProfile.getFileByPath(file.getParentFile(), root);

		if (parentNode == null) {
			System.err.println("parentNode == null");
			return;
		}
	
		// validate the write protection
		if (!parentNode.canWrite()) {
			System.err.println(String.format(
					"The directory '%s' is write protected (and we don't have the keys).", file.getParentFile().getName()));
		}

		System.err.println("Write access check for file " + parentNode.getFullPath() + file.getName() + " has been passed.");
		// provide the content protection keys, use same for chunks and meta file
		context.provideChunkProtectionKeys(parentNode.getProtectionKeys());
		context.provideMetaFileProtectionKeys(parentNode.getProtectionKeys());
		
	}

}
