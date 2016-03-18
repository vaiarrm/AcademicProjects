package sim.participants;

import sim.participants.DataFactory.CAR_DIRECTION;

final class SinkObj implements Sink {
	public boolean accept(Car c, double frontPosition) {
		return true;
	}
	public double distanceToObstacle(double fromPositon, CAR_DIRECTION d) {
		return Double.POSITIVE_INFINITY;
	}
	public double getEndPosition() {
		return Double.POSITIVE_INFINITY;
	}
	@Override
	public void setNextRoad(Road road, CAR_DIRECTION d) {
		throw new UnsupportedOperationException();

	}
	@Override
	public Road getNextRoad(CAR_DIRECTION d) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean remove(Car car) {
		throw new UnsupportedOperationException();
	}
	@Override
	public LightController getLight() {
		throw new UnsupportedOperationException();
	}
}
