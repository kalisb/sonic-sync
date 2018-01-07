package sonic.sync.client.menu;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.configuration.ConsoleFileAgent;
import sonic.sync.core.exception.InvalidProcessStateException;
import sonic.sync.core.exception.NoPeerConnectionException;
import sonic.sync.core.exception.ProcessExecutionException;
import sonic.sync.core.security.UserCredentials;
import sonic.sync.core.security.UserManager;

public class StartMenu extends ConsoleMenu {

	public StartMenu(MenuContainer menus) {
		super(menus);
	}

	@Override
	public void addMenuItems() {
		add(new ConsoleMenuItem("Connect") {
			protected void execute() {
				menus.getNodeMenu().open();
			}
		});

		add(new ConsoleMenuItem("Login") {
			protected boolean checkPreconditions() throws NoPeerConnectionException, InvalidProcessStateException,
			InterruptedException {
				if (!menus.getNodeMenu().createNetwork()) {
					printAbortion(displayText, "Node not connected.");
					return false;
				}
				if (!menus.getUserMenu().createUserCredentials()) {
					printAbortion(displayText, "User credentials not specified.");
					return false;
				}
				if (!menus.getUserMenu().createRootDirectory()) {
					printAbortion(displayText, "Root directory not specified.");
					return false;
				}
				if (!register()) {
					printAbortion(displayText, "Registering failed.");
					return false;
				}
				return true;
			}

			protected void execute() throws NoPeerConnectionException, InterruptedException, InvalidProcessStateException {
				ConsoleFileAgent fileAgent = new ConsoleFileAgent(menus.getUserMenu().getRootDirectory());
				menus.getNodeMenu().getNode().getUserManager()
				.executeLogin(menus.getUserMenu().getUserCredentials(), fileAgent);
			}
		});

		add(new ConsoleMenuItem("Logout") {
			protected boolean checkPreconditions() throws Exception {
				return checkLogin();
			}

			protected void execute() throws Exception {
				menus.getNodeMenu().getNode().getUserManager().executeLogout();
				menus.getUserMenu().clearCredentials();
			}
		});

		add(new ConsoleMenuItem("File Menu") {
			@Override
			protected boolean checkPreconditions() throws Exception {
				return checkLogin();
			}

			protected void execute() throws Exception {
				menus.getFileMenu().open();
			}
		});
	}

	protected void printAbortion(String menuName, String message) {
		ConsoleMenu.print(String.format("'%s' aborted: %s", menuName, message));
	}

	@Override
	public String getInstruction() {
		return "Please select an option:";
	}

	private boolean register() {
		UserManager userManager = menus.getNodeMenu().getNode().getUserManager();
		UserCredentials userCredentials = menus.getUserMenu().getUserCredentials();

		if (userManager.isRegistered(userCredentials.getUserId())) {
			return true;
		} else {
			ConsoleMenuItem
			.printPrecondition("You are not registered to the network. This will now happen automatically.");
			try {
				userManager.executeRegisterProcess(userCredentials);
				return true;
			} catch (ProcessExecutionException e) {
				return false;
			}
		}
	}

	private boolean checkLogin() {

		if (menus.getNodeMenu().getNode() == null) {
			ConsoleMenuItem.printPrecondition("You are not logged in. Node is not connected to a network.");
			return false;
		}
		if (menus.getUserMenu().getUserCredentials() == null) {
			ConsoleMenuItem.printPrecondition("You are not logged in. No user credentials specified.");
			return false;
		}
		return true;
	}

	@Override
	public void createItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMenuExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
