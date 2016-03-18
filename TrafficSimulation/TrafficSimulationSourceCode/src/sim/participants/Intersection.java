package sim.participants;

import sim.participants.DataFactory.CAR_DIRECTION;

public interface Intersection {
	public boolean accept(Car car, double frontPosition);
	public double distanceToObstacle(double fromPosition, CAR_DIRECTION d);
	public double getEndPosition();
	public void setNextRoad(Road road, CAR_DIRECTION d);
	public Road getNextRoad(CAR_DIRECTION d);
	public boolean remove(Car car);
	public LightController getLight();
}
