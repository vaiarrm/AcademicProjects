package sim.agent;

import java.util.NoSuchElementException;

import org.junit.Test;

import junit.framework.TestCase;

public class TimerServerTEST extends TestCase {
	public TimerServerTEST(String name) {
		super(name);
	}
	@Test
	public void testInitialState(){
		TimeServerLinked tslTest = new TimeServerLinked();
		assertTrue(tslTest.isEmpty());
		assertTrue(0==tslTest.currentTime());
		try {
			Agent x = tslTest.dequeue();
			fail();
		} catch (NoSuchElementException e) {}
	}

}
