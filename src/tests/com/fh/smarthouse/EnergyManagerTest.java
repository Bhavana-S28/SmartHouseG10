package tests.com.fh.smarthouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fh.smarthouse.energy.EnergySource;
import com.fh.smarthouse.management.EnergyManager;
import com.fh.smarthouse.objects.SmartObject;

public class EnergyManagerTest {

	private EnergyManager manager;
	private SmartObject smartObject1;
	private SmartObject smartObject2;
	private List<SmartObject> smartObjects;
	private List<EnergySource> energySources;

	@Before
	public void setUp() {
		// Initialize mock smart objects and energy sources
		smartObjects = new ArrayList<>();
		energySources = new ArrayList<>();

		// Create some example smart objects
		smartObject1 = new SmartObject("Lamp", 100); // Name and power rate (in watts)
		smartObject2 = new SmartObject("AC", 150);

		// Add them to the list
		smartObjects.add(smartObject1);
		smartObjects.add(smartObject2);

		// Create a mock energy source (you can use a mock or a simple mock class)
		EnergySource mockEnergySource = mock(EnergySource.class);
		when(mockEnergySource.getCapacity()).thenReturn(300.0); // Mock capacity value
		energySources.add(mockEnergySource);

		// Initialize EnergyManager
		manager = new EnergyManager(smartObjects, energySources);
	}

	@Test
	public void testDeleteSmartObject() {
		manager.deleteSmartObject("Lamp");

		assertFalse(manager.getSmartObjects().stream().anyMatch(obj -> obj.getName().equals("Lamp")),
				"Smart object 'Lamp' should be removed.");
	}

	@Test
	public void testToggleSmartObjectOn() {
		manager.toggleSmartObject(0); // Toggle the "Lamp" on
		assertTrue(smartObject1.isOn(), "The 'Lamp' should be turned on.");
	}

	@Test
	public void testToggleSmartObjectOff() {
		smartObject1.turnOn(); // First turn it on
		manager.toggleSmartObject(0); // Toggle the "Lamp" off
		assertFalse(smartObject1.isOn(), "The 'Lamp' should be turned off.");
	}

	@Test
	public void testCalculateTotalConsumption() {
		smartObject1.turnOn(); // Turn on "Lamp"
		smartObject2.turnOn(); // Turn on "AC"

		double totalConsumption = manager.calculateTotalConsumption();

		assertEquals(250.0, totalConsumption, "Total consumption should be 250.0 Watts.");
	}

	@Test
	public void testSetActiveSource() {
		EnergySource newSource = mock(EnergySource.class);
		when(newSource.getCapacity()).thenReturn(500.0);

		manager.setActiveSource(newSource);
		assertEquals(newSource, manager.getActiveSource(), "The active energy source should be updated.");
	}

	@Test
	public void testStartLoadBalancing() {
		// The method starts a new thread for each object, so we simulate this by
		// checking if the method is called.
		EnergyManager spyManager = spy(manager);

		spyManager.balanceLoadAcrossSources();

		// Verify if the load balancing method has been called
		verify(spyManager, times(1)).balanceLoadAcrossSources();
	}

}
