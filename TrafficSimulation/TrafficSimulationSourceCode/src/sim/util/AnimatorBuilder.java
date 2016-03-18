package sim.util;

import sim.participants.Intersection;
import sim.participants.Road;

public interface AnimatorBuilder {

	public Animator getAnimator();
	public void addLight(Intersection d, int i, int j);
	public void addHorizontalRoad(Road l, int i, int j, boolean eastToWest);
	public void addVerticalRoad(Road l, int i, int j, boolean southToNorth);

}
