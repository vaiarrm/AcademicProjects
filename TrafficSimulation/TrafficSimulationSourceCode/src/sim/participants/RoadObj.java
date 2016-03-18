package sim.participants;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import sim.participants.DataFactory.CAR_DIRECTION;

final class RoadObj implements Road{
	private Set<Car> _cars;
	private double _endPosition;
	private Intersection _nextRoad;
	private SimulationProperties _prop;
	public String toString(){
		StringBuilder  roadString = new StringBuilder();
		roadString.append("Road end Positon: ");
		roadString.append(this.getEndPosition());
		roadString.append(" Number of cars");
		roadString.append(this.getCars().size());
		return roadString.toString();
	}
	RoadObj(){
		ThreadLocalRandom random = ThreadLocalRandom.current();
		this._prop = DataFactory.newSP();
		this._endPosition = random.nextDouble(this._prop.getRoadLengthMin(),this._prop.getRoadLengthMax()); 
		this._cars = new HashSet<>();
	}

	public boolean accept(Car car, double frontPosition) {
		this._cars.remove(car);
		if (frontPosition > this._endPosition){
			return this._nextRoad.accept(car, frontPosition-this._endPosition);
		}else{
			car.setCurrentRoad(this);
			car.setfrontPosition(frontPosition);
			this._cars.add(car);
			return true;
		}
	}
	public double distanceToObstacle(double fromPosition, CAR_DIRECTION d) {
		double obstaclePosition = this.distanceToCarBack(fromPosition);
		if (obstaclePosition == Double.POSITIVE_INFINITY) {
			double distanceToEnd = this._endPosition - fromPosition;//Edited
			obstaclePosition = _nextRoad.distanceToObstacle(0,d)+distanceToEnd;
			return obstaclePosition;
		}
		return obstaclePosition - fromPosition;
	}
	private double distanceToCarBack(double fromPosition){
		double carBackPosition = Double.POSITIVE_INFINITY;
		for (Car c : this._cars){
			double temp = c.getbackPosition(); 
			if (temp >= fromPosition && temp < carBackPosition)
				carBackPosition = temp;
		}
		return carBackPosition;
	}

	public boolean remove(Car car) {
		return this._cars.remove(car);
	}
	public void setNextRoad(Intersection intersection) {
		this._nextRoad = intersection;
	}
	// Check if we need direction???
	public Intersection getNextRoad(CAR_DIRECTION d) {
		return this._nextRoad;
	}
	public double getEndPosition() {
		return this._endPosition;
	}

	public Set<Car> getCars() {
		return this._cars;
	}
	
	/*For Testing purpose*/
	public SimulationProperties get_prop() {
		return _prop;
	}
	public void set_prop(SimulationProperties _prop) {
		this._prop = _prop;
	}	
	
}
