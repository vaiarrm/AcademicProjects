package sim.participants;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import sim.participants.DataFactory.CAR_DIRECTION;

public class RoadTest extends TestCase {
	public RoadTest(String name) {
		super(name);
	}
	@Test
	public void testRoadObjConstructorTest(){
		RoadObj road = new RoadObj();
		assertNotNull(road.getCars());
		assertTrue(road.getCars().size() == 0);
		assertTrue(road.getEndPosition() >= road.get_prop().getRoadLengthMin());
		assertTrue(road.getEndPosition() <= road.get_prop().getRoadLengthMax());
	}
	@Test
	public void testRoadObjAccept(){
		RoadObj r2 = new RoadObj();
		r2.setNextRoad(new SinkObj());
		CarObj c1 = new CarObj(CAR_DIRECTION.EW);
		
		Assert.assertEquals(r2.getCars().size(), 0);
		r2.accept(c1, 0);
		Assert.assertEquals(r2.getCars().size(), 1);
		Iterator<Car> iterator = r2.getCars().iterator();
		while (iterator.hasNext()) {
			Car c = iterator.next();
			Assert.assertEquals(c.getCurrentRoad(), r2);
			Assert.assertEquals(c, c1);
		}
		r2.getCars().remove(c1);
		Assert.assertEquals(r2.getCars().size(), 0);
		
	}
	
	

	
}
