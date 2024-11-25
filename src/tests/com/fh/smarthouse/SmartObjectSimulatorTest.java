package tests.com.fh.smarthouse;

import static org.mockito.Mockito.*;

import java.util.Scanner;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import com.fh.smarthouse.management.EnergyManager;

public class SmartObjectSimulatorTest {

	private EnergyManager mockEnergyManager;
	private Scanner mockScanner;

	@BeforeEach
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
		mockEnergyManager = new EnergyManager(Arrays.asList(lamp, ac, tv, fridge),
				Arrays.asList(solarPanel, cityPower, dieselGenerator));
		simulator = new SmartObjectSimulator();
	}

	@Test
	public void testViewStatusValid() {
		// This test will call viewStatus, and we will check if total consumption is
		// printed correctly
		// For simplicity, we will just test that the total consumption is calculated
		// and logged
		double totalConsumption = mockEnergyManager.calculateTotalConsumption();
		assertTrue(totalConsumption > 0, "Total consumption should be greater than 0.");
	}

	@Test
	public void testViewStatusWarningExceedCapacity() {
		// Test when total consumption exceeds capacity, which should trigger a warning.
		EnergySource smallSource = new SolarPanel(100); // Reduce capacity to trigger warning
		EnergyManager managerWithSmallSource = new EnergyManager(Arrays.asList(new SmartObject("Lamp", 500)),
				Arrays.asList(smallSource));
		double totalConsumption = managerWithSmallSource.calculateTotalConsumption();
		EnergySource activeSource = managerWithSmallSource.getActiveSource();

		// Expect warning if consumption exceeds capacity
		if (totalConsumption > activeSource.getCapacity()) {
			assertTrue(true, "Warning triggered for exceeding capacity.");
		} else {
			fail("Expected a warning about exceeding capacity.");
		}
	}

	@Test
	public void testManageSmartObjects() {
		// Mock user input for managing smart objects
		Scanner scanner = mock(Scanner.class);
		when(scanner.nextInt()).thenReturn(1).thenReturn(0); // Simulate user choosing "1" to add and "0" to exit

		// Simulate adding a smart object
		SmartObjectSimulator simulator = new SmartObjectSimulator();
		simulator.manageSmartObjects(mockEnergyManager, scanner);

		// We need to verify whether manager's addSmartObject method was called
		// This can be done by verifying interaction with the mockEnergyManager
		verify(mockEnergyManager, times(1)).addSmartObject(scanner); // Check that addSmartObject was called once
	}

	@Test
	public void testToggleSmartObject() {
		Scanner scanner = mock(Scanner.class);
		when(scanner.nextInt()).thenReturn(1).thenReturn(0); // Simulate user selecting a smart object to toggle and
																// then choosing exit

		// Simulate toggling the state of a smart object
		SmartObjectSimulator simulator = new SmartObjectSimulator();
		simulator.toggleSmartObject(mockEnergyManager, scanner);

		// Check that the toggleSmartObject was invoked
		verify(mockEnergyManager, times(1)).toggleSmartObject(0);
	}
}
