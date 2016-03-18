package sim.participants;

import java.awt.Color;

import sim.agent.Agent;
import sim.participants.DataFactory.CAR_DIRECTION;


public interface Car extends Agent{
	
	public Color getColor();
	public Double getMaxVelocity();
	public Double getBrakeDistance();
	public Double getStopDistance();
	public Double getLengthOfCar();
	public CAR_DIRECTION getDirection();
	public double getbackPosition();
	public void setfrontPosition(double frontPosition);
	public double getfrontPosition();
	public void setCurrentRoad(Road road);
	public void setCurrentIntersection(Intersection intersection);
	public Road getCurrentRoad();
	public Intersection getCurrentInt();
}
