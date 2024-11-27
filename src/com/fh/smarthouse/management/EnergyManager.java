package com.fh.smarthouse.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.fh.smarthouse.energy.EnergySource;
import com.fh.smarthouse.objects.SmartObject;

public class EnergyManager {

	private final List<SmartObject> smartObjects;
	private final List<EnergySource> energySources;
	private EnergySource activeSource;
	private static final Logger logger = Logger.getLogger(EnergyManager.class.getName());

	public EnergyManager(List<SmartObject> smartObjects, List<EnergySource> energySources) {
		this.smartObjects = smartObjects;
		this.energySources = energySources;
		this.activeSource = energySources.isEmpty() ? null : energySources.get(0);
	}

	public List<SmartObject> getSmartObjects() {
		return smartObjects;
	}

	public List<EnergySource> getEnergySources() {
		return energySources;
	}

	public EnergySource getActiveSource() {
		return activeSource;
	}

	public void setActiveSource(EnergySource activeSource) {
		this.activeSource = activeSource;
		logger.info("Active energy source set to " + activeSource.getClass().getSimpleName());
	}

	public void addSmartObject(Scanner scanner) {
		System.out.print("\nEnter smart object name: ");
		String name = scanner.next();
		System.out.print("Enter Smart Object Power Rate (e.g., 200): ");
		int powerRate = scanner.nextInt();
		smartObjects.add(new SmartObject(name, powerRate));
		System.out.println("\nSmart object '" + name + "' added.");
	}

	public void listSmartObject() {
		boolean exit = false;

		while (!exit) {
			System.out.println("\n<<< List of Smart Objects >>>");

			// Display the list of smart objects
			for (int i = 0; i < smartObjects.size(); i++) {
				SmartObject object = smartObjects.get(i);
				System.out.println((i + 1) + ". " + object.getName() + " - " + (object.isOn() ? "On" : "Off")
						+ " - Power Rate: " + object.getWatt() + " W");
			}
			// Option to exit the menu
			System.out.print("\nEnter (0) to go back: ");
			Scanner scanner = new Scanner(System.in);
			String userInput = scanner.next();

			if ("0".equals(userInput)) {
				exit = true;
				scanner.close();
				System.out.println("Returning to the previous menu...");
			} else {
				System.out.println("Invalid input. Please enter 0 to go back.");
			}
		}
	}

	public void deleteSmartObject(String name) {
		boolean removed = smartObjects.removeIf(obj -> obj.getName().equals(name));
		if (removed) {
			System.out.println("\nSmart object '" + name + "' deleted.");
		} else {
			System.out.println("\nSmart object '" + name + "' not found.");
		}
	}

	public void toggleSmartObject(int index) {
		if (index < 0 || index >= smartObjects.size()) {
			System.out.println("\nInvalid object index.");
			return;
		}

		SmartObject object = smartObjects.get(index);
		if (object.isOn()) {
			object.turnOff();
			System.out.println(object.getName() + " turned off.");
		} else {
			object.turnOn();
			System.out.println(object.getName() + " turned on.");
		}

		double totalConsumption = calculateTotalConsumption();
		if (totalConsumption > (activeSource != null ? activeSource.getCapacity() : 0)) {
			logger.warning("Total consumption exceeds active source capacity. load balancing...");
			balanceLoadAcrossSources();
		}
	}

	public void balanceLoadAcrossSources() {
		System.out.println("\nBalancing load across energy sources...");
		List<Thread> threads = new ArrayList<>();
		final double[] remainingCapacity = { activeSource.getCapacity() }; // Start with the active source
		for (SmartObject object : smartObjects) {
			if (object.isOn()) {
				Thread thread = new Thread(() -> {
					synchronized (energySources) {
						for (EnergySource source : energySources) {
							if (remainingCapacity[0] >= object.getConsumption()) {
								remainingCapacity[0] -= object.getConsumption();
								System.out.println(
										object.getName() + " is powered by " + source.getClass().getSimpleName());
								break;
							} else if (energySources.indexOf(source) < energySources.size() - 1) {
								remainingCapacity[0] = energySources.get(energySources.indexOf(source) + 1)
										.getCapacity();
							} else {
								logger.warning(object.getName() + " cannot be powered due to insufficient capacity.");
							}
						}
					}
				});
				threads.add(thread);
				thread.start();
			}

		}
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public double calculateTotalConsumption() {
		double totalConsumption = 0.0;
		for (SmartObject smartObject : smartObjects) {
			if (smartObject.isOn()) {
				totalConsumption += smartObject.getConsumption();
			}
		}
		return totalConsumption;
	}
}
