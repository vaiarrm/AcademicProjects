package sim.participants;


import java.util.concurrent.ThreadLocalRandom;
import sim.agent.TimeServer;
import sim.participants.DataFactory.LIGHT_STATE;

class LightControllerObj implements LightController{
	
	private double _greenNS;
	private double _yellowNS;
	private double _greenEW;
	private double _yellowEW;
	private TimeServer _timeserver; 
	private LIGHT_STATE _lightstate;
	private SimulationProperties _prop;
	
	
	LightControllerObj() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		this._prop = DataFactory.newSP();
		
		this._greenNS = random.nextDouble(this._prop.getTrafficLightGreenMin(),this._prop.getTrafficLightGreenMax());
		this._yellowNS = random.nextDouble(this._prop.getTrafficLightYellowMin(), this._prop.getTrafficLightYellowMax());
		this._greenEW = random.nextDouble(this._prop.getTrafficLightGreenMin(),this._prop.getTrafficLightGreenMax());
		this._yellowEW = random.nextDouble(this._prop.getTrafficLightYellowMin(), this._prop.getTrafficLightYellowMax());
		
		this._timeserver = this._prop.getTimeServer();
		this._lightstate = LIGHT_STATE.EW_GREEN;
	}
	
	public void run() {	
		switch (this._lightstate) {
		case EW_GREEN:
			setLightState(LIGHT_STATE.EW_YELLOW);
			this._timeserver.enqueue(this._timeserver.currentTime()+this._yellowEW, this);
			break;
		case EW_YELLOW:
			setLightState(LIGHT_STATE.NS_GREEN);
			this._timeserver.enqueue(this._timeserver.currentTime()+this._greenNS, this);
			break;
		case NS_GREEN:
			setLightState(LIGHT_STATE.NS_YELLOW);
			this._timeserver.enqueue(this._timeserver.currentTime()+this._yellowNS, this);
			break;
		case NS_YELLOW:
			setLightState(LIGHT_STATE.EW_GREEN);
			this._timeserver.enqueue(this._timeserver.currentTime()+this._greenEW, this);
			break;
		default:
			StringBuilder error = new StringBuilder();
			error.append("Incorrect value of Light State. Light State value is : ");
			error.append(this._lightstate);
			throw new IllegalArgumentException(error.toString());
			//break;
		}	
	}

	public LIGHT_STATE getLightstate() {
		return _lightstate;
	}
	public void setLightState(LIGHT_STATE state) {
		this._lightstate = state;
	}
	/* For Purpose of Testing Only - Comment Out*/
	public double get_yellowNS() {
		return _yellowNS;
	}
	public double get_greenEW() {
		return _greenEW;
	}
	public double get_greenNS() {
		return _greenNS;
	}
	public double get_yellowEW() {
		return _yellowEW;
	}
	public TimeServer get_timeserver() {
		return _timeserver;
	}
	public SimulationProperties get_prop() {
		return _prop;
	}	
}
