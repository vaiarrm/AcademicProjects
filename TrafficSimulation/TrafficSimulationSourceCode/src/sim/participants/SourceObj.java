package sim.participants;

import java.util.concurrent.ThreadLocalRandom;

import sim.agent.Agent;
import sim.agent.TimeServer;
import sim.participants.DataFactory.CAR_DIRECTION;

final class SourceObj implements Source,Agent {
	private CAR_DIRECTION _direction;
	private Road _road;
	private double _carGeneration;
	private SimulationProperties _prop;
	private TimeServer _timeServer;

	SourceObj(CAR_DIRECTION d){
		this._prop = DataFactory.newSP();
		this._timeServer = this._prop.getTimeServer();
		this._timeServer.enqueue(this._timeServer.currentTime(), this);
		this._direction = d;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		this._carGeneration =  random.nextDouble(this._prop.getCarGenerationDelayMin(), this._prop.getCarGenerationDelayMax());
	}


	public void setNextRoad(Road road) {
		this._road = road;	
	}
	public Road getNextRoad() {
		return this._road;
	}
	public void run() {
		boolean putCar = true;
		Car car = DataFactory.newCar(this._direction);
//		for(Car c : this._road.getCars()){
//			if (c.getfrontPosition() <= c.getLengthOfCar() + c.getBrakeDistance()) // Need to check this Logic Vaibhav
//				putCar = false;
//		}
		if(putCar){
			this._road.accept(car, 0);
			this._timeServer.enqueue(this._timeServer.currentTime(), car);
		}
		this._timeServer.enqueue(this._timeServer.currentTime()+this._carGeneration, this);
	}
	/*For Testing Purpose Only */


	public CAR_DIRECTION get_direction() {
		return _direction;
	}


	public void set_direction(CAR_DIRECTION _direction) {
		this._direction = _direction;
	}


	public double get_carGeneration() {
		return _carGeneration;
	}


	public void set_carGeneration(double _carGeneration) {
		this._carGeneration = _carGeneration;
	}


	public SimulationProperties get_prop() {
		return _prop;
	}


	public void set_prop(SimulationProperties _prop) {
		this._prop = _prop;
	}


	public TimeServer get_timeServer() {
		return _timeServer;
	}


	public void set_timeServer(TimeServer _timeServer) {
		this._timeServer = _timeServer;
	}
	
}
