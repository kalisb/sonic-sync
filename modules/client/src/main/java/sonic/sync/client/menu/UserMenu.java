package sonic.sync.client.menu;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;

import org.apache.commons.io.FileUtils;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.security.UserCredentials;

public class UserMenu extends ConsoleMenu {

	private ConsoleMenuItem createUserCredentials;
	private UserCredentials userCredentials;
	private ConsoleMenuItem createRootDirectory;
	private File rootDirectory;

	public UserMenu(MenuContainer menus) {
		super(menus);
	}

	public boolean createUserCredentials() {
		while (getUserCredentials() == null) {
			createUserCredentials.invoke();
		}
		// at this point, credentials have always been specified
		return true;
	}

	@Override
	public void createItems() {
		createUserCredentials = new ConsoleMenuItem("Create User Credentials") {
			protected void execute() throws Exception {
				userCredentials = new UserCredentials(askUsedId(), askPassword(), askPin());
				exit();
			}
		};

		createRootDirectory = new ConsoleMenuItem("Create Root Directory") {
			protected void execute() throws Exception {
				String directory = askDirectory();
				rootDirectory = new File(FileUtils.getUserDirectory(), directory);

				if (!Files.exists(rootDirectory.toPath(), LinkOption.NOFOLLOW_LINKS)) {
					try {
						FileUtils.forceMkdir(rootDirectory);
						print(String.format("Root directory '%s' created.", rootDirectory));
					} catch (Exception e) {
						printError(String
								.format("Exception on creating the root directory %s: " + e, rootDirectory.toPath()));
					}
				} else {
					print(String.format("Existing root directory '%s' will be used.", rootDirectory));
				}
			}
		};
	}
	
	private String askDirectory() {
		print("Specify the user root directory:");
		return awaitStringParameter().trim();
	}

	protected String askPin() {
		print("Specify the user PIN:");
		return awaitStringParameter().trim();
	}

	protected String askPassword() {
		print("Specify the user password:");
		return awaitStringParameter().trim();
	}

	protected String askUsedId() {
		print("Specify the user ID:");
		return awaitStringParameter().trim();
	}

	@Override
	public void onMenuExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInstruction() {
		return "Please select a user configuration option:";
	}

	@Override
	public void addMenuItems() {
		add(createUserCredentials);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public boolean createRootDirectory() {
		while (getRootDirectory() == null) {
			createRootDirectory.invoke();
		}
		// at this point, a root directory has always been specified
		return true;
	}

	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void clearCredentials() {
		// TODO Auto-generated method stub
		
	}

}
