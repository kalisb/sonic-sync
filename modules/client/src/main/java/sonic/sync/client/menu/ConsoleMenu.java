package sonic.sync.client.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sonic.sync.client.item.ConsoleMenuItem;
import sonic.sync.core.configuration.Configuration;
import sonic.sync.core.configuration.ConfigurationFactory;

public abstract class ConsoleMenu {

	private final List<ConsoleMenuItem> items;
	protected final Configuration config;

	private static final String EXIT_TOKEN = "Q";
	private boolean exited;
	protected final MenuContainer menus;

	public ConsoleMenu(MenuContainer menus) {
		this.menus = menus;
		this.items = new ArrayList<>();
		this.config = ConfigurationFactory.load("configuration.yaml");
		createItems();
	}

	public abstract void createItems();	

	protected final void add(ConsoleMenuItem menuItem) {
		items.add(menuItem);
	}

	public static void print(String message) {
		System.out.println(message);
	}

	public void open() {
		items.clear();
		addMenuItems();

		this.exited = false;
		while (!exited) {
			show();
		}
		onMenuExit();
	}

	public abstract void onMenuExit();

	private void show() {
		print();
		print(getInstruction());
		print();

		// print normal items
		for (int i = 0; i < items.size(); ++i) {
			print(String.format("\t[%s]  %s", i + 1, items.get(i).getDisplayText()));
		}

		// print exit item
		print(String.format("\n\t[%s]  %s", EXIT_TOKEN, getExitItemText()));

		// evaluate input
		String input;
		boolean validInput = false;

		while (!validInput) {
			input = awaitStringParameter();
			if (input.equalsIgnoreCase(EXIT_TOKEN)) {
				validInput = true;
				exit();
			} else {
				try {
					int chosen = Integer.valueOf(input);
					if (chosen > items.size() || chosen < 1) {
						printError(String.format("Invalid option. Please select an option from 1 to %s.", items.size()));
						validInput = false;
					} else {
						items.get(chosen - 1).invoke();
						validInput = true;
					}
				} catch (NumberFormatException e) {
					printError(String
							.format("This was not a valid input. Please select an option from 1 to %s or press '%s' to exit this menu.",
									items.size(), EXIT_TOKEN));
					validInput = false;
				}
			}
		}

	}

	public static void printError(String errorMsg) {
		System.err.println(errorMsg);
	}

	String awaitStringParameter() {
		// do not close input
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);

		String parameter = null;
		boolean success = false;
		while (!success) {
			try {
				parameter = input.next();
				success = true;
			} catch (Exception e) {
				printError("Exception while parsing the parameter: " + e.getMessage());
			}
		}

		return parameter;
	}

	protected int awaitIntParameter() {
		boolean success = false;
		int number = 0;
		int tries = 0;
		while (!success && tries < 5) {
			try {
				number = Integer.parseInt(awaitStringParameter());
				success = true;
			} catch (NumberFormatException e) {
				tries++;
				printError("This was not a number... Try again! (" + (5 - tries) + " tries left)");
			}
		}
		return number;
	}
	
	private String getExitItemText() {
		return "Back";
	}

	public abstract String getInstruction();

	private void print() {
		System.out.println();
	}

	public abstract void addMenuItems();

	public abstract void shutdown();

	public void exit() {
		exited = true;
	}

}
