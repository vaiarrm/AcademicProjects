package sim.participants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import sim.agent.TimeServer;
import sim.participants.DataFactory.CAR_DIRECTION;
import sim.util.Animator;
import sim.util.AnimatorBuilder;

final class SimpleModel extends Observable implements Model {

	private Animator _animator;
	private boolean _disposed;
	private TimeServer _timeServer;
	private SimulationProperties _prop;
	public SimpleModel(AnimatorBuilder builder, Integer rows, Integer columns) {
		this._prop = DataFactory.newSP();
		this._timeServer = this._prop.getTimeServer();
		if (rows < 0 || columns < 0 || (rows == 0 && columns == 0)) {
			throw new IllegalArgumentException();
		}
		setup(builder, rows, columns);
		_animator = builder.getAnimator();
		//super.addObserver(_animator);
		_timeServer.addObserver(_animator);
	}
	@Override
	public void run() {
		if (_disposed)
			throw new IllegalStateException();
		this._timeServer.run(_prop.getSimulaitonRunTime());
		super.setChanged();
		super.notifyObservers();

	}

	@Override
	public void dispose() {
		_animator.dispose();
		_disposed = true;

	}
	private void setup(AnimatorBuilder builder, Integer rows, Integer columns) {
		List<Road> roads = new ArrayList<Road>();
		Intersection[][] intersections = new Intersection[rows][columns];

		// Add Intersection
		
		if (rows > 0 && columns > 0){
			
			for (int i=0; i<rows; i++) {
				for (int j=0; j<columns; j++) {
					intersections[i][j] = DataFactory.newIntersection();
					builder.addLight(intersections[i][j], i, j);
					_timeServer.enqueue(_timeServer.currentTime(), intersections[i][j].getLight());
				}
			}
			
			// Add Horizontal Roads and connect them to the source, lights and sinks
			for (int i=0; i<rows; i++) {
				Source carsource = DataFactory.newSource(CAR_DIRECTION.EW);
				for (int j=0; j<=columns; j++) {
					Road l = DataFactory.newRoad();
					if (j == 0) {
						carsource.setNextRoad(l);
						l.setNextRoad(intersections[i][j]);
					}
					else if (j == columns) {
						intersections[i][j-1].setNextRoad(l, CAR_DIRECTION.EW);
						l.setNextRoad(DataFactory.newSink());
					}
					else {
						intersections[i][j-1].setNextRoad(l, CAR_DIRECTION.EW);
						l.setNextRoad(intersections[i][j]);
					}
					builder.addHorizontalRoad(l, i, j, false);
					roads.add(l);
				}
			}
			// Add Vertical Roads and connect them to the source, lights and sinks
			for (int j=0; j<columns; j++) {
				Source carsource = DataFactory.newSource(CAR_DIRECTION.NS);
				for (int i=0; i<=rows; i++) {
					Road l = DataFactory.newRoad();
					if (i == 0) {
						carsource.setNextRoad(l);
						l.setNextRoad(intersections[i][j]);
					}
					else if (i == rows) {
						intersections[i-1][j].setNextRoad(l, CAR_DIRECTION.NS);
						l.setNextRoad(DataFactory.newSink());
					}
					else {
						intersections[i-1][j].setNextRoad(l, CAR_DIRECTION.NS);
						l.setNextRoad(intersections[i][j]);
					}
					builder.addVerticalRoad(l, i, j, false);
					roads.add(l);
				}
			}
		}else if (rows == 0){
			for (int i=0; i<columns; i++) {
				Source carsource = DataFactory.newSource(CAR_DIRECTION.NS);
				Road l = DataFactory.newRoad();
				carsource.setNextRoad(l);
				l.setNextRoad(DataFactory.newSink());
				builder.addVerticalRoad(l, 0, i, false);
				roads.add(l);
			}
		}else if (columns == 0){
			for (int i=0; i<rows; i++) {
				Source carsource = DataFactory.newSource(CAR_DIRECTION.EW);
				Road l = DataFactory.newRoad();
				carsource.setNextRoad(l);
				l.setNextRoad(DataFactory.newSink());
				builder.addHorizontalRoad(l, i, 0, false);
				roads.add(l);
			}
			
		}
	}
}
