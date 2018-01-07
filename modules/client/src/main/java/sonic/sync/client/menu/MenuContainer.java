package sonic.sync.client.menu;

public class MenuContainer {

	private final NodeMenu nodeMenu = new NodeMenu(this);
	private final UserMenu userMenu = new UserMenu(this);
	private final FileMenu fileMenu = new FileMenu(this);
	private final FileObserverMenu fileObserverMenu = new FileObserverMenu(this);

	public NodeMenu getNodeMenu() {
		return nodeMenu;
	}

	public UserMenu getUserMenu() {
		return userMenu;
	}

	public FileMenu getFileMenu() {
		return fileMenu;
	}

	public FileObserverMenu getFileObserverMenu() {
		return fileObserverMenu;
	}
}
