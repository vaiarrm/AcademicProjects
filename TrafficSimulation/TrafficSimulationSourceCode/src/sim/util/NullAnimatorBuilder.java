package sim.util;

import sim.participants.Intersection;
import sim.participants.Road;

final class NullAnimatorBuilder implements AnimatorBuilder {

	@Override
	public Animator getAnimator() { return new NullAnimator(); }
	public void addLight(Intersection d, int i, int j) { }
	public void addHorizontalRoad(Road l, int i, int j, boolean eastToWest) { }
	public void addVerticalRoad(Road l, int i, int j, boolean southToNorth) { }

}
