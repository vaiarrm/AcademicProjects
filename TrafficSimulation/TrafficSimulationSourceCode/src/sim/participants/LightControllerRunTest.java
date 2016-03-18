package sim.participants;

import java.util.Observable;
import java.util.Observer;

import sim.participants.DataFactory.LIGHT_STATE;

public class LightControllerRunTest implements Observer {
	public  LightControllerObj l = new LightControllerObj();
	boolean changed = true;
	int count = 2;
	public  void update(Observable o, Object arg) {
		switch(count % 4){
		case(1):
			//System.out.println(count + " " + l.getLightstate());
			LightControllerObjTest.assertSame(LIGHT_STATE.EW_GREEN,l.getLightstate());
		count++;
		break;
		case(2):
			//System.out.println(count + " " + l.getLightstate());
			LightControllerObjTest.assertSame(LIGHT_STATE.EW_YELLOW, l.getLightstate());
		count++;
		break;
		case(3):
			//System.out.println(count + " " + l.getLightstate());
			LightControllerObjTest.assertSame(LIGHT_STATE.NS_GREEN,l.getLightstate());
		count++;
		break;
		case(0):
			//System.out.println(count + " " + l.getLightstate());
			LightControllerObjTest.assertSame(LIGHT_STATE.NS_YELLOW,l.getLightstate());
		count++;
		break;
		default:
			LightControllerObjTest.fail();

		}

	}

}

