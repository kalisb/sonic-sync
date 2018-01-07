package sonic.sync.client.menu;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.file.FileObserver;
import sonic.sync.core.file.FileObserverListener;

public class FileObserverMenu extends ConsoleMenu {

	private FileObserver fileObserver;
	private long interval = 1000;

	public FileObserverMenu(MenuContainer menus) {
		super(menus);
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
	public String getInstruction() {
		return String.format("Start/stop the file observer:");
	}

	@Override
	public void addMenuItems() {

		add(new ConsoleMenuItem("Start File Observer") {
			protected boolean checkPreconditions() {
				if (menus.getNodeMenu().createNetwork()) {
					return menus.getUserMenu().createRootDirectory();
				} else {
					return false;
				}
			}

			protected void execute() throws Exception {
				fileObserver = new FileObserver(menus.getUserMenu().getRootDirectory(), interval);
				FileObserverListener listener = new FileObserverListener(menus.getNodeMenu().getNode().getFileManager());
				fileObserver.addFileListener(listener);

				fileObserver.start();
				exit();
			}
		});

		add(new ConsoleMenuItem("Stop File Observer") {
			protected void execute() throws Exception {
				if (fileObserver != null) {
					fileObserver.stop();
				}
				exit();
			}
		});
	}
	
	public FileObserver getFileObserver() {
		return fileObserver;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
