package sim.participants;

public interface Source {
	
	public void setNextRoad(Road road);
	public Road getNextRoad();
	public void run();

}
