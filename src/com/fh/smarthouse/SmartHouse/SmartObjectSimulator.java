package com.fh.smarthouse.SmartHouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.fh.smarthouse.energy.CityPower;
import com.fh.smarthouse.energy.DieselGenerator;
import com.fh.smarthouse.energy.EnergySource;
import com.fh.smarthouse.energy.SolarPanel;
import com.fh.smarthouse.management.EnergyManager;
import com.fh.smarthouse.objects.SmartObject;

public class SmartObjectSimulator {

	private static final Logger logger = Logger.getLogger(SmartObjectSimulator.class.getName());

	public static void main(String[] args) {

		try {
			// Load logging configuration from file
			LogManager.getLogManager()
					.readConfiguration(SmartObjectSimulator.class.getResourceAsStream("/logging.properties"));

			logger.info("Logging setup completed!");

			// Your existing application logic
		} catch (IOException e) {
			System.err.println("Failed to load logging configuration: " + e.getMessage());
		}

		// Initialize energy sources

		List<EnergySource> energySources = Arrays.asList(new SolarPanel(500), new CityPower(1000),
				new DieselGenerator(800));

		// Initialize smart objects
		List<SmartObject> smartObjects = Arrays.asList(new SmartObject("Lamp", 100), new SmartObject("AC", 300),
				new SmartObject("TV", 200), new SmartObject("Fridge", 300));

		// Initialize energy manager
		EnergyManager manager = new EnergyManager(new ArrayList<>(smartObjects), new ArrayList<>(energySources));

		// User interaction
		try (Scanner scanner = new Scanner(System.in)) {
			boolean exit = false;

			while (!exit) {
				System.out.println("\n<<< Welcome to Smart House Management >>>");
				System.out.println("1. View Status");
				System.out.println("2. Manage Smart Objects");
				System.out.println("3. Set Active Energy Source");
				System.out.println("4. Balance Load Across Sources");
				System.out.println("0. Exit");
				System.out.print("Choose an option: ");

				try {
					int choice = scanner.nextInt();
					switch (choice) {
					case 1:
						viewStatus(manager, scanner);
						break;
					case 2:
						manageSmartObjects(manager, scanner);
						break;
					case 3:
						setActiveEnergySource(manager, scanner);
						break;
					case 4:
						manager.balanceLoadAcrossSources();
					case 0: {
						exit = true;
						System.out.println("Exiting...");
					}
					default:
						System.out.println("Invalid option. Please try again.");
						logger.warning("Invalid input. Please enter a valid number.");

					}
				} catch (InputMismatchException e) {
					logger.warning("Invalid input. Please enter a valid number.");
					scanner.nextLine(); // Clear invalid input
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
		}

	}

	private static void viewStatus(EnergyManager manager, Scanner scanner) {
		try {
			boolean exit = false;

			while (!exit) {
				System.out.println("\n<<< Smart Objects >>>");
				manager.getSmartObjects().forEach(object -> System.out.println(object.getName() + " - "
						+ (object.isOn() ? "On" : "Off") + " - Consumption: " + object.getConsumption() + " W"));

				System.out.println("\n<<< Energy Sources >>>");
				EnergySource activeSource = manager.getActiveSource();
				double totalConsumption = manager.calculateTotalConsumption();

				System.out.println(
						"Active Source: " + (activeSource != null ? activeSource.getClass().getSimpleName() : "None"));
				System.out.println("Total Consumption: " + totalConsumption + " W");

				if (activeSource != null && totalConsumption > activeSource.getCapacity()) {
					logger.warning("Warning! Energy consumption exceeds source capacity.");
				}

				// Option to go back
				System.out.print("\nEnter (0) to go back: ");
				String userInput = scanner.next();

				if ("0".equals(userInput)) {
					exit = true;
					System.out.println("Returning to the previous menu...");
				} else {
					System.out.println("Invalid input. Please enter 0 to go back.");
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while viewing status: " + e.getMessage(), e);
		}
	}

	private static void manageSmartObjects(EnergyManager manager, Scanner scanner) {
		try {

			boolean exit = false;
			while (!exit) {
				System.out.println("\n<<< Manage Smart Objects >>>");
				System.out.println("1. Add Smart Object");
				System.out.println("2. Remove Smart Object");
				System.out.println("3. Toggle Smart Object");
				System.out.println("4. List Smart Objects");
				System.out.println("0. Go Back");
				System.out.print("Choose an option: ");

				int choice = scanner.nextInt();

				switch (choice) {
				case 1:
					manager.addSmartObject(scanner); // Add a new smart object
				case 2: {
					System.out.print("Enter the name of the object to remove: ");
					String name = scanner.next();
					manager.deleteSmartObject(name); // Remove the specified smart object
				}
				case 3:
					toggleSmartObject(manager, scanner); // Toggle the state of a smart object
				case 4:
					manager.listSmartObject(); // List all smart objects
				case 0:
					exit = true; // Exit the menu
				default:
					System.out.println("Invalid option. Please try again."); // Handle invalid input
				}
			}
			System.out.println("Returning to the previous menu...");
		} catch (InputMismatchException e) {
			logger.warning("Invalid input. Please enter a valid number.");
			scanner.nextLine(); // Clear invalid input
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while managing smart objects: " + e.getMessage(), e);
		}
	}

	private static void toggleSmartObject(EnergyManager manager, Scanner scanner) {
		try {

			List<SmartObject> smartObjects = manager.getSmartObjects();
			boolean exit = false;

			while (!exit) {
				System.out.println("\n<<< Toggle Smart Object >>>");
				for (int i = 0; i < smartObjects.size(); i++) {
					SmartObject object = smartObjects.get(i);
					System.out.println((i + 1) + ". " + object.getName() + " - " + (object.isOn() ? "ON" : "OFF"));
				}
				System.out.print("Select a smart object to toggle or enter 0 to go back: ");

				int index = scanner.nextInt() - 1;

				if (index == -1) {
					exit = true; // User entered 0 to exit the menu
				} else if (index >= 0 && index < smartObjects.size()) {
					manager.toggleSmartObject(index); // Toggle the selected object
				} else {
					System.out.println("Invalid selection. Please try again.");
				}
			}
			System.out.println("Returning to the previous menu...");
		} catch (InputMismatchException e) {
			logger.warning("Invalid input. Please enter a valid number.");
			scanner.nextLine(); // Clear invalid input
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while toggling smart object: " + e.getMessage(), e);
		}
	}

	private static void setActiveEnergySource(EnergyManager manager, Scanner scanner) {
		try {

			boolean exit = false;

			while (!exit) {
				System.out.println("\n<<< Set Active Energy Source >>>");
				List<EnergySource> energySources = manager.getEnergySources();

				for (int i = 0; i < energySources.size(); i++) {
					EnergySource source = energySources.get(i);
					System.out.println((i + 1) + ". " + source.getClass().getSimpleName() + " - Remaining Capacity: "
							+ source.getCapacity() + " W");
				}
				System.out.println("0. Go Back");
				System.out.print("Choose an energy source: ");

				int index = scanner.nextInt() - 1;

				if (index == -1) {
					exit = true; // Exit the menu when the user enters 0
				} else if (index >= 0 && index < energySources.size()) {
					manager.setActiveSource(energySources.get(index)); // Set the selected energy source
				} else {
					System.out.println("Invalid selection. Please try again.");
				}
			}
			System.out.println("Returning to the previous menu...");
		} catch (InputMismatchException e) {
			logger.warning("Invalid input. Please enter a valid number.");
			scanner.nextLine(); // Clear invalid input
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while setting active energy source: " + e.getMessage(), e);
		}
	}

}
