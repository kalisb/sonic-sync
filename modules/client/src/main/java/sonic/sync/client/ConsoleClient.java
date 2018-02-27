package sonic.sync.client;

import org.fusesource.jansi.AnsiConsole;

import sonic.sync.client.menu.ConsoleMenu;
import sonic.sync.client.menu.MenuContainer;
import sonic.sync.client.menu.StartMenu;

public class ConsoleClient {

	public static void main(String[] args) {
		new ConsoleClient().start();
		printFooter();
		Formatter.reset();
		AnsiConsole.systemUninstall();
		System.exit(0);
	}

	public void start() {
		AnsiConsole.systemInstall();
		Formatter.setDefaultForeground();

		LoggerInit.initLogger();
		printHeader();
		printInstructions();

		StartMenu menu = new StartMenu(new MenuContainer());
		menu.open();
		menu.shutdown();
	}

	private static void printHeader() {
		ConsoleMenu.print("\n****** Welcome to the Sonic Sync console client! ******\n");
	}

	private static void printFooter() {
		ConsoleMenu.print("\n****** Goodbye! ******\n");
	}

	private static void printInstructions() {
		ConsoleMenu.print("Configure and operate on your Sonic Sync network by following the guides.\n");
		ConsoleMenu.print("Navigate through the menus by entering the numbers next to the items of your choice.");
	}

}
