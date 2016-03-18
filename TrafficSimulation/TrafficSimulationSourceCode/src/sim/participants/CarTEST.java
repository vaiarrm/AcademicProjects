package sim.participants;

import org.junit.Test;

import junit.framework.TestCase;
import sim.participants.DataFactory.CAR_DIRECTION;


public class CarTEST extends TestCase {

	public CarTEST(String name) {
		super(name);
	}
	@Test
	public void testCarConstructor(){
		SimulationProperties prop = DataFactory.newSP();
		Car c = new CarObj(CAR_DIRECTION.NS);
		assertTrue(c.getLengthOfCar() <= prop.getCarLengthMax());
		assertTrue(c.getLengthOfCar() >= prop.getCarLengthMin());
		assertTrue(c.getBrakeDistance() <= prop.getCarBrakeDistanceMax());
		assertTrue(c.getBrakeDistance() >= prop.getCarBrakeDistanceMin());
		assertTrue(c.getMaxVelocity() <= prop.getCarVelocityMax());
		assertTrue(c.getMaxVelocity() >= prop.getCarVelocityMin());
		assertTrue(c.getStopDistance() <= prop.getCarStopDistanceMax());
		assertTrue(c.getStopDistance() >= prop.getCarStopDistanceMin());
	}
}
