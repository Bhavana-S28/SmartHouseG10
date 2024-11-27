package tests.com.fh.smarthouse;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ EnergyManagerTest.class, SmartObjectSimulatorTest.class })
public class AllTests {

}
