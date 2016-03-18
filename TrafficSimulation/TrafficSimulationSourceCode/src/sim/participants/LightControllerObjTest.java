package sim.participants;



import org.junit.Test;

import junit.framework.TestCase;
import sim.participants.DataFactory.LIGHT_STATE;

public class LightControllerObjTest extends TestCase  {

	public LightControllerObjTest(String name) {
		super(name);
	}
	LightControllerObj l = new LightControllerObj();
	@Test
	public void testLightControllerConstructor(){
		//assertSame(LIGHT_STATE.EW_YELLOW, l.getLightstate());
		assertTrue(l.get_greenEW() <= l.get_prop().getTrafficLightGreenMax());
		assertTrue(l.get_greenEW() >= l.get_prop().getTrafficLightGreenMin());
		assertTrue(l.get_greenNS() <= l.get_prop().getTrafficLightGreenMax());
		assertTrue(l.get_greenNS() >= l.get_prop().getTrafficLightGreenMin());
		assertTrue(l.get_yellowEW() <= l.get_prop().getTrafficLightYellowMax());
		assertTrue(l.get_yellowEW() >= l.get_prop().getTrafficLightYellowMin());
		assertTrue(l.get_yellowNS() <= l.get_prop().getTrafficLightYellowMax());
		assertTrue(l.get_yellowNS() >= l.get_prop().getTrafficLightYellowMin());

	}
	@Test
	public void testLightControllersetter(){
		l.setLightState(LIGHT_STATE.EW_GREEN);
		assertSame(LIGHT_STATE.EW_GREEN, l.getLightstate());
	}
	@Test
	public void testLightControllerRun(){
		LightControllerRunTest temp = new LightControllerRunTest();
		temp.l.get_prop().getTimeServer().enqueue(0, temp.l);
		temp.l.get_prop().getTimeServer().addObserver(temp);
		temp.l.get_prop().getTimeServer().run(100);

	}
}
