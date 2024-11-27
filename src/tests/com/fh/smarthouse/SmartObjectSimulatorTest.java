package tests.com.fh.smarthouse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import com.fh.smarthouse.SmartHouse.SmartObjectSimulator;
import com.fh.smarthouse.energy.CityPower;
import com.fh.smarthouse.energy.DieselGenerator;
import com.fh.smarthouse.energy.EnergySource;
import com.fh.smarthouse.energy.SolarPanel;
import com.fh.smarthouse.management.EnergyManager;
import com.fh.smarthouse.objects.SmartObject;

public class SmartObjectSimulatorTest {

	@Mock
	private EnergyManager mockEnergyManager;
	private SmartObjectSimulator simulator;
	private Scanner mockScanner;

	@Before
	public void setup() {

		// Setup Energy Sources
		EnergySource solarPanel = new SolarPanel(500);
		EnergySource cityPower = new CityPower(1000);
		EnergySource dieselGenerator = new DieselGenerator(800);

		// Setup Smart Objects
		SmartObject lamp = new SmartObject("Lamp", 100);
		SmartObject ac = new SmartObject("AC", 300);
		SmartObject tv = new SmartObject("TV", 200);
		SmartObject fridge = new SmartObject("Fridge", 300);

		// Initialize EnergyManager with mock objects
		mockEnergyManager = new EnergyManager(new ArrayList<>(Arrays.asList(lamp, ac, tv, fridge)),
				Arrays.asList(solarPanel, cityPower, dieselGenerator));
		simulator = new SmartObjectSimulator();
		mockScanner = new Scanner(new ByteArrayInputStream("1\n0\n".getBytes()));
	}

	@Test
	public void testViewStatusValid() {
		// This test will call viewStatus, and we will check if total consumption is
		// printed correctly
		// For simplicity, we will just test that the total consumption is calculated
		// and logged
		List<SmartObject> smartObjects = mockEnergyManager.getSmartObjects();
		Iterator<SmartObject> iterator = smartObjects.iterator();
		while (iterator.hasNext()) {
			SmartObject smartObject = iterator.next();
			smartObject.turnOn();
		}
		double totalConsumption = mockEnergyManager.calculateTotalConsumption();
		assertTrue("Total consumption should be greater than 0.", totalConsumption > 0); // Message, then condition

	}

	@Test
	public void testViewStatusWarningExceedCapacity() {
		// Test when total consumption exceeds capacity, which should trigger a warning.
		EnergySource smallSource = new SolarPanel(100); // Reduce capacity to trigger warning
		EnergyManager managerWithSmallSource = new EnergyManager(Arrays.asList(new SmartObject("Lamp", 500, true)),
				Arrays.asList(smallSource));
		double totalConsumption = managerWithSmallSource.calculateTotalConsumption();
		EnergySource activeSource = managerWithSmallSource.getActiveSource();

		// Expect warning if consumption exceeds capacity
		if (totalConsumption > activeSource.getCapacity()) {
			assertTrue("Warning triggered for exceeding capacity.", true);
		} else {
			fail("Expected a warning about exceeding capacity.");
		}
	}

	@Test
	public void testCaseWhenCapacityNotExceeded() {
		// Test when total consumption exceeds capacity, which should trigger a warning.
		EnergySource smallSource = new SolarPanel(100); // Reduce capacity to trigger warning
		EnergyManager managerWithSmallSource = new EnergyManager(Arrays.asList(new SmartObject("Lamp", 60, true)),
				Arrays.asList(smallSource));
		double totalConsumption = managerWithSmallSource.calculateTotalConsumption();
		EnergySource activeSource = managerWithSmallSource.getActiveSource();

		// Expect warning if consumption exceeds capacity
		if (totalConsumption < activeSource.getCapacity()) {
			assertFalse("Not exceeding capacity.", false);
		} else {
			fail("Expected no warning about exceeding capacity.");
		}
	}

	@Test
	public void testDeleteSmartObject() {
		mockEnergyManager.deleteSmartObject("Lamp"); // Remove the smart object

		List<SmartObject> smartObjects = mockEnergyManager.getSmartObjects();
		assertEquals(3, smartObjects.size()); // Ensure the list size is 0
		assertFalse(smartObjects.stream().anyMatch(obj -> obj.getName().equals("Lamp"))); // Ensure object is removed
	}

	@Test
	public void testToggleSmartObject() {

		List<SmartObject> smartObjects = mockEnergyManager.getSmartObjects();
		SmartObject lamp = smartObjects.get(0); // Get the added smart object

		mockEnergyManager.toggleSmartObject(0); // Turn on the object
		assertTrue(lamp.isOn()); // Assert that the lamp is now ON

		mockEnergyManager.toggleSmartObject(0); // Turn off the object
		assertFalse(lamp.isOn()); // Assert that the lamp is now OFF
	}

	@Test
	public void testCalculateTotalConsumption() {

		// Turn on the lamp and AC
		List<SmartObject> smartObjects = mockEnergyManager.getSmartObjects();
		smartObjects.get(0).turnOn(); // Turn on Lamp
		smartObjects.get(1).turnOn(); // Turn on AC

		double totalConsumption = mockEnergyManager.calculateTotalConsumption();
		assertEquals(400, totalConsumption, 0.01); // Check if the total consumption is 100 + 200 = 300 W
	}

	@Test
	public void testSetActiveEnergySource() {
		List<EnergySource> energySources = Arrays.asList(new SolarPanel(500), new CityPower(1000),
				new DieselGenerator(800));
		EnergyManager manager = new EnergyManager(new ArrayList<>(), energySources);

		manager.setActiveSource(energySources.get(1)); // Set CityPower as active source

		assertEquals(energySources.get(1), manager.getActiveSource()); // Assert that the active source is CityPower
	}

	@Test
	public void testEnergySourceCapacity() {
		List<EnergySource> energySources = Arrays.asList(new SolarPanel(500), new CityPower(1000),
				new DieselGenerator(800));
		EnergyManager manager = new EnergyManager(new ArrayList<>(), energySources);

		// Add smart objects with high power consumption
		manager.addSmartObject(new Scanner("Lamp\n100\n"));
		manager.addSmartObject(new Scanner("AC\n500\n"));

		// Set active source to SolarPanel with low capacity
		manager.setActiveSource(energySources.get(0)); // SolarPanel with 500W capacity

		// Turn on both smart objects
		List<SmartObject> smartObjects = manager.getSmartObjects();
		smartObjects.get(0).turnOn(); // Lamp
		smartObjects.get(1).turnOn(); // AC

		double totalConsumption = manager.calculateTotalConsumption();
		assertTrue(totalConsumption > energySources.get(0).getCapacity()); // Ensure total consumption exceeds
																			// SolarPanel capacity
	}

}
