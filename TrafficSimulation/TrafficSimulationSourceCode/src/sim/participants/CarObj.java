package sim.participants;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import sim.agent.TimeServer;
import sim.participants.DataFactory.CAR_DIRECTION;


final class CarObj implements Car{
	/* maxVelocity - The maximum velocity of the car (in meters/second)
	 * brakeDistance - If distance to nearest obstacle is <= brakeDistance,
	 * 					then the car will start to slow down (in meters)
	 * stopDistance - If distance to nearest obstacle is == stopDistance,
	 * 					then the car will stop (in meters)
	 */
	private double _lengthOfCar;
	private double _maxVelocity;
	private double _stopDistance;
	private double _brakeDistance;
	private double _timeStep;
	private double _frontPosition;
	private double _velocity;
	private TimeServer _timeserver;
	private double _distanceToObstacle;
	private CAR_DIRECTION _direction;
	private Color _color;
	private SimulationProperties _prop;
	private Road _currentRoad;
	private Intersection _currentIntersection;
	private boolean _inIntersection;

	CarObj(CAR_DIRECTION direction){
		this._prop = DataFactory.newSP();
		this._direction = direction;
		ThreadLocalRandom random = ThreadLocalRandom.current();
		this._maxVelocity = random.nextDouble
				(this._prop.getCarVelocityMin(),this._prop.getCarVelocityMax());
		this._brakeDistance = random.nextDouble
				(this._prop.getCarBrakeDistanceMin(),this._prop.getCarBrakeDistanceMax());
		this._stopDistance = random.nextDouble
				(this._prop.getCarStopDistanceMin(),this._prop.getCarStopDistanceMax());
		this._lengthOfCar = random.nextDouble
				(this._prop.getCarLengthMin(),this._prop.getCarLengthMax());
		this._color = new java.awt.Color((int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255),(int)Math.ceil(Math.random()*255));
		this._frontPosition = 0.0;
		this._timeStep = this._prop.getSimulationTimeStep();
		this._timeserver = this._prop.getTimeServer();
	}
	public Double getMaxVelocity() {
		return this._maxVelocity;
	}
	public Double getBrakeDistance() {
		return _brakeDistance;
	}
	public Double getStopDistance() {
		return _stopDistance;
	}
	public Double getLengthOfCar() {
		return _lengthOfCar;
	}
	public Color getColor(){
		return this._color;
	}
	public CAR_DIRECTION getDirection(){
		return this._direction;
	}
	public Road getCurrentRoad() {
		return this._currentRoad;
	}
	public Intersection getCurrentInt() {
		return this._currentIntersection;
	}
	public double getfrontPosition() {
		return this._frontPosition;
	}
	public double getbackPosition() {
		return this._frontPosition - this._lengthOfCar;
	}
	
	public void setCurrentRoad(Road road) {
		this._currentRoad = road;
		this._inIntersection = false;
	}
	public void setCurrentIntersection(Intersection intersection) {
		this._currentIntersection = intersection;
		this._inIntersection = true;
	}

	public void setfrontPosition(double frontPosition) {
		if (this._inIntersection) {
			if (frontPosition > this.getCurrentInt().getEndPosition()) {
				this.getCurrentInt().getNextRoad(this._direction).accept(this, frontPosition - this.getCurrentInt().getEndPosition());
				this.getCurrentInt().remove(this);
				return;
			}
			else {
				this._frontPosition = frontPosition;
			}
		} else {
			if (frontPosition > this.getCurrentRoad().getEndPosition()) {
				//Vaibhav stopping from entering into intersection
				boolean didMove = this.getCurrentRoad().getNextRoad(_direction).accept(this, frontPosition - this.getCurrentRoad().getEndPosition());
				if(didMove){
					this.getCurrentRoad().remove(this);
				}
				
				return;
			}
			else {
				this._frontPosition = frontPosition;
			}
		}
	}
	public void run() {
		if (this._inIntersection) {
			this._distanceToObstacle = this.getCurrentInt().distanceToObstacle(this._frontPosition, this._direction);
		} else{
			this._distanceToObstacle = this.getCurrentRoad().distanceToObstacle(this._frontPosition, this._direction);
		}
		/* Originally < this._macVelocity Changed to < this._maxVelocity * this._timeStep
		 * 
		 */
//		if (this._distanceToObstacle < this._maxVelocity * this._timeStep && (this._distanceToObstacle > this._brakeDistance || this._distanceToObstacle > this._stopDistance))
//			this._velocity = this._distanceToObstacle / 2;
//		else {
//			this._velocity = (this._maxVelocity / (this._brakeDistance - this._stopDistance)) * (this._distanceToObstacle - this._stopDistance);
//		}
		this._velocity = (this._maxVelocity / (this._brakeDistance - this._stopDistance)) * (this._distanceToObstacle - this._stopDistance);
		if (this._distanceToObstacle < this._velocity * this._timeStep && (this._distanceToObstacle > this._brakeDistance || this._distanceToObstacle > this._stopDistance)){
			this._velocity = 0.0;
		}
		this._velocity = Math.max(0.0, this._velocity);
		this._velocity = Math.min(this._maxVelocity, this._velocity);
		this._frontPosition = this._frontPosition + this._velocity * this._timeStep;
		setfrontPosition(this._frontPosition);
		this._timeserver.enqueue(this._timeserver.currentTime() + this._timeStep, this);

	}
	
}
