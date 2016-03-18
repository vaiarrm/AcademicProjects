//Done
package sim.agent;

import java.util.Observer;

public interface TimeServer {
	public double currentTime();
	public void enqueue(double waketime, Agent thing);
	public void run(double duration);
	public void addObserver(java.util.Observer o);
	public void deleteObserver(Observer o);
	//For Testing ONly - Remove public modifier from Implementation
	//public int getSize();
	//public boolean isEmpty();
}
