package sim.participants;

import sim.agent.TimeServer;
import sim.agent.TimeServerFactory;
import sim.util.SwingAnimatorBuilder;

public final class DataFactory {

	public static enum LIGHT_STATE {NS_GREEN, NS_YELLOW, EW_GREEN, EW_YELLOW}
	public static enum CAR_DIRECTION {EW,NS }

	public static final Car newCar(CAR_DIRECTION d) {
		return new CarObj(d);
	}
	
	public static final Source newSource(CAR_DIRECTION d){
		return new SourceObj(d);
	}
	
	public static final Road newRoad(){
		return new RoadObj();
	}
	
	public static final LightController newLightController(){
		return new LightControllerObj();
	}
	
	public static final Intersection newIntersection(){
		return new IntersectionObj();
	}
	
	public static final Sink newSink(){
		return new SinkObj();
	}

	public static final SimulationProperties newSP(){
		return SimulationProperties.getProperties();
	}
		
	public static TimeServer newTimeServerLinked(){	
		return TimeServerFactory.newTimeServerLinked();
	}
	
	public static Model newSimpleModel(){	
		return new SimpleModel(new SwingAnimatorBuilder(),newSP().getNumRoadsRows(),newSP().getNumRoadsColumns());
	}
	
	public static Model newAltModel(){	
		return new AlternateModel(new SwingAnimatorBuilder(),newSP().getNumRoadsRows(),newSP().getNumRoadsColumns());
	}
	

}
