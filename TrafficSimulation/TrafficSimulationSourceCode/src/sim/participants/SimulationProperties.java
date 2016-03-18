package sim.participants;


import sim.agent.TimeServer;



/**
 * @author vaibhavsharma
 * This class contains all the properties required for running and modifying the simulation
 */
public final class SimulationProperties {


	private SimulationProperties(){}
	public Model getModel() {
		if (this.isAlternating)
			return DataFactory.newAltModel();
		else 
			return DataFactory.newSimpleModel(); 
	}

	private static SimulationProperties properties = null;
	
	/* Car Properties*/
	
	private double carLengthMin = 5.0;
	private double carLengthMax = 10.0;
	private double carVelocityMin = 10.0;
	private double carVelocityMax = 30.0;
	private double carStopDistanceMin = 0.5;
	private double carStopDistanceMax = 5.0;
	private double carBrakeDistanceMin = 9.0;
	private double carBrakeDistanceMax = 10.0;
	
	/* Road Properties */
	private double roadLengthMin = 200.0;
	private double roadLengthMax = 500.0;
	private double intersectionLengthMin = 25;//10.0;
	private double intersectionLengthMax = 30;//15.0;
	private double trafficLightGreenMin = 30.0;
	private double trafficLightGreenMax = 40.0;//180.0;
	private double trafficLightYellowMin = 10.0;//4.0;
	private double trafficLightYellowMax = 15.0;//5.0;

	/* Simulation Properties*/
	private double carGenerationDelayMin = 2.0;
	private double carGenerationDelayMax = 3.0;//25.0;
	private double simulationTimeStep = .1;
	private double simulaitonRunTime = 500;
	private int numRoadsRows = 2;
	private int numRoadsColumns = 3;
	private boolean isAlternating =false;
	private TimeServer timeServer = DataFactory.newTimeServerLinked();
	
	public static double constantGUIcarLength = 10;
	public static double constantGUIroadLength = 200;
	public static double constantGUImaxVelocity = 6;
	
	/* Generating a Single Object for this Class */
	public static SimulationProperties getProperties() {
		if (SimulationProperties.properties == null)
			SimulationProperties.properties = new SimulationProperties();
		return SimulationProperties.properties;
	}

	public void setDefault() {
		SimulationProperties.properties = null;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Simulation time step (seconds) [" + this.getSimulationTimeStep() + "]\n");
		buffer.append("Simulation run time (seconds) [" + this.getSimulaitonRunTime() + "]\n");
		buffer.append("Grid size (number of roads) [row=" + this.getNumRoadsRows() + ", column="+this.getNumRoadsColumns()+"]\n");
		buffer.append("Traffic pattern [" + this.getTrafficPattern() + "]\n");
		buffer.append("Car entry rate (seconds/car) [min="+this.getCarGenerationDelayMin()+",max="+this.getCarGenerationDelayMax()+"]\n");
		buffer.append("Road segment length (meters) [min="+this.getRoadLengthMin()+",max="+this.getRoadLengthMax()+"]\n");
		buffer.append("Intersection length (meters) [min="+this.getIntersectionLengthMin()+",max="+this.getIntersectionLengthMax()+"]\n");
		buffer.append("Car length (meters) [min="+this.getCarLengthMin()+",max="+this.getCarLengthMax()+"]\n");
		buffer.append("Car maximum velocity (meters/second) [min="+this.getCarVelocityMin()+",max="+this.getCarVelocityMax()+"]\n");
		buffer.append("Car stop distance (meters) [min="+this.getCarStopDistanceMin()+",max="+this.getCarStopDistanceMax()+"]\n");
		buffer.append("Car brake distance (meters) [min="+this.getCarBrakeDistanceMin()+",max="+this.getCarBrakeDistanceMax()+"]\n");
		buffer.append("Traffic light green time (seconds) [min="+this.getTrafficLightGreenMin()+",max="+this.getTrafficLightGreenMax()+"]\n");
		buffer.append("Traffic light yellow time (seconds) [min="+this.getTrafficLightYellowMin()+",max="+this.getTrafficLightYellowMax()+"]\n");
		return buffer.toString();
	}
	double getCarLengthMin() {
		return carLengthMin;
	}
	private String getTrafficPattern() {
		if (isAlternating)
			return "alternating";
		return "simple";
	}

	public void setCarLengthMin(double carLengthMin) {
		this.carLengthMin = carLengthMin;
	}

	public double getCarLengthMax() {
		return carLengthMax;
	}

	public void setCarLengthMax(double carLengthMax) {
		this.carLengthMax = carLengthMax;
	}

	public double getRoadLengthMin() {
		return roadLengthMin;
	}

	public void setRoadLengthMin(double roadLengthMin) {
		this.roadLengthMin = roadLengthMin;
	}

	public double getRoadLengthMax() {
		return roadLengthMax;
	}

	public void setRoadLengthMax(double roadLengthMax) {
		this.roadLengthMax = roadLengthMax;
	}

	public double getIntersectionLengthMin() {
		return intersectionLengthMin;
	}

	public void setIntersectionLengthMin(double intersectionLengthMin) {
		this.intersectionLengthMin = intersectionLengthMin;
	}

	public double getIntersectionLengthMax() {
		return intersectionLengthMax;
	}

	public void setIntersectionLengthMax(double intersectionLengthMax) {
		this.intersectionLengthMax = intersectionLengthMax;
	}

	public double getCarVelocityMin() {
		return carVelocityMin;
	}

	public void setCarVelocityMin(double carVelocityMin) {
		this.carVelocityMin = carVelocityMin;
	}

	public double getCarVelocityMax() {
		return carVelocityMax;
	}

	public void setCarVelocityMax(double carVelocityMax) {
		this.carVelocityMax = carVelocityMax;
	}

	public double getCarStopDistanceMin() {
		return carStopDistanceMin;
	}

	public void setCarStopDistanceMin(double carStopDistanceMin) {
		this.carStopDistanceMin = carStopDistanceMin;
	}

	public double getCarStopDistanceMax() {
		return carStopDistanceMax;
	}

	public void setCarStopDistanceMax(double carStopDistanceMax) {
		this.carStopDistanceMax = carStopDistanceMax;
	}

	public double getCarBrakeDistanceMin() {
		return carBrakeDistanceMin;
	}

	public void setCarBrakeDistanceMin(double carBrakeDistanceMin) {
		this.carBrakeDistanceMin = carBrakeDistanceMin;
	}

	public double getCarBrakeDistanceMax() {
		return carBrakeDistanceMax;
	}

	public void setCarBrakeDistanceMax(double carBrakeDistanceMax) {
		this.carBrakeDistanceMax = carBrakeDistanceMax;
	}

	public double getCarGenerationDelayMin() {
		return carGenerationDelayMin;
	}

	public void setCarGenerationDelayMin(double carGenerationDelayMin) {
		this.carGenerationDelayMin = carGenerationDelayMin;
	}

	public double getCarGenerationDelayMax() {
		return carGenerationDelayMax;
	}

	public void setCarGenerationDelayMax(double carGenerationDelayMax) {
		this.carGenerationDelayMax = carGenerationDelayMax;
	}

	public double getTrafficLightGreenMin() {
		return trafficLightGreenMin;
	}

	public void setTrafficLightGreenMin(double trafficLightGreenMin) {
		this.trafficLightGreenMin = trafficLightGreenMin;
	}

	public double getTrafficLightGreenMax() {
		return trafficLightGreenMax;
	}

	public void setTrafficLightGreenMax(double trafficLightGreenMax) {
		this.trafficLightGreenMax = trafficLightGreenMax;
	}

	public double getTrafficLightYellowMin() {
		return trafficLightYellowMin;
	}

	public void setTrafficLightYellowMin(double trafficLightYellowMin) {
		this.trafficLightYellowMin = trafficLightYellowMin;
	}

	public double getTrafficLightYellowMax() {
		return trafficLightYellowMax;
	}

	public void setTrafficLightYellowMax(double trafficLightYellowMax) {
		this.trafficLightYellowMax = trafficLightYellowMax;
	}

	public double getSimulationTimeStep() {
		return simulationTimeStep;
	}

	public void setSimulationTimeStep(double simulationTimeStep) {
		this.simulationTimeStep = simulationTimeStep;
	}

	public double getSimulaitonRunTime() {
		return simulaitonRunTime;
	}

	public void setSimulaitonRunTime(double simulaitonRunTime) {
		this.simulaitonRunTime = simulaitonRunTime;
	}

	public int getNumRoadsRows() {
		return numRoadsRows;
	}

	public void setNumRoadsRows(int numRoadsRows) {
		this.numRoadsRows = numRoadsRows;
	}

	public int getNumRoadsColumns() {
		return numRoadsColumns;
	}

	public void setNumRoadsColumns(int numRoadsColumns) {
		this.numRoadsColumns = numRoadsColumns;
	}

	public boolean isAlternating() {
		return isAlternating;
	}

	public void setAlternating(boolean isAlternating) {
		this.isAlternating = isAlternating;
	}

	public TimeServer getTimeServer() {
		return timeServer;
	}
}
