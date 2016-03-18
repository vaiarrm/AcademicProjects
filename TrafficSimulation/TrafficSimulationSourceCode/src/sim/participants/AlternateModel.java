package sim.participants;

import sim.util.AnimatorBuilder;
import java.util.ArrayList;
import java.util.List;

import sim.agent.TimeServer;
import sim.participants.DataFactory.CAR_DIRECTION;
import sim.util.Animator;


public class AlternateModel implements Model{

	private Animator _animator;
	private boolean _disposed;
	private TimeServer _timeServer;
	private SimulationProperties _prop;

	public AlternateModel(AnimatorBuilder builder, Integer rows, Integer columns) {
		this._prop = DataFactory.newSP();
		this._timeServer = this._prop.getTimeServer();
		if (rows < 0 || columns < 0 || (rows == 0 && columns == 0)) {
			throw new IllegalArgumentException();
		}
		setup(builder, rows, columns);
		this._animator = builder.getAnimator();
		//super.addObserver(_animator);
		this._timeServer.addObserver(_animator);
	}
	public void run() {
		if (_disposed)
			throw new IllegalStateException();
		this._timeServer.run(_prop.getSimulaitonRunTime());
	}
	public void dispose() {
		_animator.dispose();
		_disposed = true;
	}
	private void setup(AnimatorBuilder builder, Integer rows, Integer columns) {
		List<Road> roads = new ArrayList<Road>();
		Intersection[][] intersections = new Intersection[rows][columns];

		//Add Intersection
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				intersections[i][j] = DataFactory.newIntersection();
				builder.addLight(intersections[i][j], i, j);
				_timeServer.enqueue(_timeServer.currentTime(), intersections[i][j].getLight());
			}
		}

		// Add Horizontal Roads

		boolean eastToWest = false;
		for (int i=0; i<rows; i++) {
			if(!eastToWest){
				Source source = DataFactory.newSource(CAR_DIRECTION.EW);
				for(int j = 0; j <= columns; j++){
					Road road = DataFactory.newRoad();
					if(j == 0){
						source.setNextRoad(road);
						road.setNextRoad(intersections[i][j]);
					}else if(j == columns){
						intersections[i][j-1].setNextRoad(road, CAR_DIRECTION.EW);
						road.setNextRoad(DataFactory.newSink());
					}else{
						intersections[i][j-1].setNextRoad(road, CAR_DIRECTION.EW);
						road.setNextRoad(intersections[i][j]);
					}
					builder.addHorizontalRoad(road, i, j, eastToWest);
					roads.add(road);
				}
			}else{
				Source source = DataFactory.newSource(CAR_DIRECTION.EW);
				for(int j = columns; j >= 0; j--){
					Road road = DataFactory.newRoad();
					if(j == columns){
						source.setNextRoad(road);
						road.setNextRoad(intersections[i][j-1]);
					}else if(j == 0){
						intersections[i][j].setNextRoad(road, CAR_DIRECTION.EW);
						road.setNextRoad(DataFactory.newSink());
					}else{
						intersections[i][j].setNextRoad(road, CAR_DIRECTION.EW);
						road.setNextRoad(intersections[i][j-1]);
					}
					builder.addHorizontalRoad(road, i, j, eastToWest);
					roads.add(road);
				}
			}
			eastToWest = !eastToWest;
		}

		// Add Vertical Roads
		boolean southToNorth = false;
		for (int j=0; j<columns; j++) {
			if (!southToNorth) {
				Source source = DataFactory.newSource(CAR_DIRECTION.NS);
				for (int i=0; i<=rows; i++) {
					Road road = DataFactory.newRoad();
					if (i == 0) {
						source.setNextRoad(road);
						road.setNextRoad(intersections[i][j]);
					}
					else if (i == rows) {
						intersections[i-1][j].setNextRoad(road, CAR_DIRECTION.NS);
						road.setNextRoad(DataFactory.newSink());
					}
					else {
						intersections[i-1][j].setNextRoad(road, CAR_DIRECTION.NS);
						road.setNextRoad(intersections[i][j]);
					}

					builder.addVerticalRoad(road, i, j, southToNorth);
					roads.add(road);
				}
			} else {
				Source carsource = DataFactory.newSource(CAR_DIRECTION.NS);
				for (int i=rows; i>=0; i--) {
					Road road = DataFactory.newRoad();
					if (i == rows) {
						carsource.setNextRoad(road);
						road.setNextRoad(intersections[i-1][j]);
					}
					else if (i == 0) {
						intersections[i][j].setNextRoad(road, CAR_DIRECTION.NS);
						road.setNextRoad(DataFactory.newSink());
					}
					else {
						intersections[i][j].setNextRoad(road, CAR_DIRECTION.NS);
						road.setNextRoad(intersections[i-1][j]);
					}

					builder.addVerticalRoad(road, i, j, southToNorth);
					roads.add(road);
				}
			}
			southToNorth= !southToNorth;
		}
	}
}