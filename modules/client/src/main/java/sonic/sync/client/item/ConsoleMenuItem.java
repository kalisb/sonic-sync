package sonic.sync.client.item;

import sonic.sync.client.menu.ConsoleMenu;

public abstract class ConsoleMenuItem {
	
	public String displayText;

	public ConsoleMenuItem(String displayText) {
		this.displayText = displayText;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void invoke() {
		boolean satisfied = false;
		try {
			satisfied = checkPreconditions();
		} catch (Exception e) {
			ConsoleMenu.printError("Exception during precondition check.");
			e.printStackTrace();
		}

		if (satisfied) {
			initialize();
			try {
				execute();
			} catch (Exception e) {
				ConsoleMenu.printError("Exception during menu item execution.");
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {
				end();
			}
}
	}

	public void end() {
		// TODO Auto-generated method stub
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	protected boolean checkPreconditions() throws Exception {
		return true;
	}

	public static void printPrecondition(String string) {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract void execute() throws Exception;

}
