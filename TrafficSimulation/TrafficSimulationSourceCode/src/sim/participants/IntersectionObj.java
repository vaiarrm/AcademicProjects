package sim.participants;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import sim.participants.DataFactory.CAR_DIRECTION;
import sim.participants.DataFactory.LIGHT_STATE;

final class IntersectionObj implements Intersection{

	private SimulationProperties _prop;
	private double _intersectionLength;
	private LightController _lightController;
	private Road _nextRoadEW;
	private Set <Car> _carsEW;
	private Road _nextRoadNS;
	private Set <Car> _carsNS;
	IntersectionObj(){
		ThreadLocalRandom random = ThreadLocalRandom.current();
		this._prop = DataFactory.newSP();
		this._intersectionLength = random.nextDouble(this._prop.getIntersectionLengthMin(),this._prop.getIntersectionLengthMax());
		this._carsEW = new HashSet<>();
		this._carsNS = new HashSet<>();
		this._lightController = DataFactory.newLightController();
	}
	public boolean accept(Car car, double frontPosition) {
		if(car.getDirection()==CAR_DIRECTION.EW){
			return accept(car,frontPosition,this._carsEW,this._carsNS);
		}else{
			return accept(car,frontPosition,this._carsNS,this._carsEW);
		}
	}
	private boolean accept(Car car, double frontPosition, Set<Car> cars,Set<Car> carsOtherDirection){
		cars.remove(car);
		if (frontPosition > this._intersectionLength && distanceToObstacle(frontPosition, car.getDirection()) != 0) {
			return this._nextRoadEW.accept(car, frontPosition - this._intersectionLength);
		}else if (!carsOtherDirection.isEmpty()){ // Vaibhav these condition added
			return false;
		}
		else {
			car.setCurrentIntersection(this);
			car.setfrontPosition(frontPosition);
			cars.add(car);
			return true;
		}
	}
	public double distanceToObstacle(double fromPosition, CAR_DIRECTION d) {
		if (d == CAR_DIRECTION.EW){
			return distanceToObstacle(this._nextRoadEW,fromPosition,d,LIGHT_STATE.EW_YELLOW,LIGHT_STATE.EW_GREEN,this._carsEW,this._carsNS,CAR_DIRECTION.NS);
		} 
		else {
			return distanceToObstacle(this._nextRoadNS,fromPosition,d,LIGHT_STATE.NS_YELLOW,LIGHT_STATE.NS_GREEN,this._carsNS,this._carsEW,CAR_DIRECTION.EW);
		}			
	}
	private double distanceToObstacle(Road r, double fromPosition, CAR_DIRECTION d, LIGHT_STATE yellow,LIGHT_STATE green,Set<Car> cars,Set<Car> carsOtherDirection,CAR_DIRECTION otherD) {
		if ((this._lightController.getLightstate() != green && this._lightController.getLightstate() != yellow)&& (!cars.isEmpty())){
			if(fromPosition == 0){
				return 0.0;
			}else{
				return caldistanceToObstacle(r,fromPosition,cars,d);	
			}
		}else if ((this._lightController.getLightstate() != green && this._lightController.getLightstate() != yellow) && (cars.isEmpty())){
			return 0.0;
		}else if ((this._lightController.getLightstate() == green || this._lightController.getLightstate() == yellow)&& (!carsOtherDirection.isEmpty())){
			return 0.0;
		}else if ((this._lightController.getLightstate() == green || this._lightController.getLightstate() == yellow)&& (carsOtherDirection.isEmpty())){
			return caldistanceToObstacle(r,fromPosition,cars,d);
		}else{
			return 0.0;
		}
	}
	private double caldistanceToObstacle(Road r,double fromPosition,Set<Car> cars,CAR_DIRECTION d){
		double obstaclePosition = this.distanceToCarBack(fromPosition, cars);
		if (obstaclePosition == Double.POSITIVE_INFINITY) {
			double distanceToEnd = this._intersectionLength - fromPosition;
			obstaclePosition = r.distanceToObstacle(0, d) + distanceToEnd;
			return obstaclePosition;
		}else{
			return obstaclePosition - fromPosition;
		}
	}
	private double distanceToCarBack(double fromPosition, Set<Car> cars) {
		double carBackPosition = Double.POSITIVE_INFINITY;
		for (Car c : cars) {
			double temp = c.getbackPosition(); 
			if ( temp >= fromPosition && temp < carBackPosition)
				carBackPosition = c.getbackPosition();
		}
		return carBackPosition;
	}
	public double getEndPosition() {
		return this._intersectionLength;
	}
	public void setNextRoad(Road road, CAR_DIRECTION d) {
		if (d == CAR_DIRECTION.EW) this._nextRoadEW = road;
		else this._nextRoadNS = road;
	}
	public Road getNextRoad(CAR_DIRECTION d) {
		if (d == CAR_DIRECTION.EW) return this._nextRoadEW;
		else  return this._nextRoadNS;
	}
	public boolean remove(Car car) {
		if(car.getDirection()==CAR_DIRECTION.EW){
			return this._carsEW.remove(car);
		}else{
			return this._carsNS.remove(car);
		}
	}
	public LightController getLight() {
		return this._lightController;
	}


}
