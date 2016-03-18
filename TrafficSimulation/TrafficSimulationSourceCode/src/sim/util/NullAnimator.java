package sim.util;

import java.util.Observable;

final class NullAnimator implements Animator {
	public void update(Observable o, Object arg) {}
	public void dispose() {}
}
