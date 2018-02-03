package sonic.sync.client.menu;

import java.io.File;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.exception.NoPeerConnectionException;
import sonic.sync.core.exception.NoSessionException;

public class FileMenu extends ConsoleMenu {

	public FileMenu(MenuContainer menus) {
		super(menus);
	}

	@Override
	public void createItems() {

	}

	protected File askForFile(boolean expectExistence) {
		return askForFile("Specify the relative path to the root directory '%s'.", expectExistence);
	}

	private File askForFile(String msg, boolean expectExistence) {
		return askFor(msg, expectExistence, false);
	}

	private File askFor(String msg, boolean expectExistence, boolean requireDirectory) {

		File rootDirectory = menus.getUserMenu().getRootDirectory();
		File file = null;
		do {
			print(String.format(msg.concat(expectExistence ? String.format(" The %s at this path must exist.",
					requireDirectory ? "folder" : "file") : ""), rootDirectory.getAbsolutePath()));
			print("Or enter 'cancel' in order to go back.");

			String input = awaitStringParameter();

			if ("cancel".equalsIgnoreCase(input)) {
				return null;
			}

			file = new File(rootDirectory, input);
			if (expectExistence && !file.exists()) {
				printError(String.format("The specified %s '%s' does not exist. Try again.", requireDirectory ? "folder"
						: "file", file.getAbsolutePath()));
				continue;
			}
			if (expectExistence && requireDirectory && !file.isDirectory()) {
				printError(String.format("The specified file '%s' is not a folder. Try again.", file.getAbsolutePath()));
			}
		} while (expectExistence && (file == null || !file.exists() || (requireDirectory && !file.isDirectory())));
		return file;
	}


	@Override
	public void onMenuExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInstruction() {
		return "Select a file operation:";
	}

	@Override
	public void addMenuItems() {
		add(new ConsoleMenuItem("Add File") {
			protected boolean checkPreconditions() {
				return menus.getUserMenu().createRootDirectory();
			}

			protected void execute() {

				File file = askForFile(true);
				if (file == null) {
					return;
				}

				try {
					menus.getNodeMenu().getNode().getFileManager()
					.createAddProcess(file);
				} catch (NoSessionException | IllegalArgumentException | NoPeerConnectionException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}

			}
		});

		add(new ConsoleMenuItem("File Observer") {
			protected boolean checkPreconditions() {
				return menus.getUserMenu().createRootDirectory();
			}

			protected void execute() {
				menus.getFileObserverMenu().open();
			}
		});
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void reset() {
		// TODO Auto-generated method stub

	}

}
