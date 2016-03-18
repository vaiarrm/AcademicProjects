package sim.participants;


import java.util.Set;
import sim.participants.DataFactory.CAR_DIRECTION;

public interface Road{
	public boolean accept(Car car, double frontPosition);
	public double distanceToObstacle(double fromPosition, CAR_DIRECTION d);
	public double getEndPosition();
	public void setNextRoad(Intersection intersection);
	public Intersection getNextRoad(CAR_DIRECTION d);
	public boolean remove(Car car);
	public Set<Car> getCars();
}
