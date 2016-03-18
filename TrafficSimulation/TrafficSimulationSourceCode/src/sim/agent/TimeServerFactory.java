package sim.agent;

public final class TimeServerFactory {
	
	public static TimeServer newTimeServerLinked(){	
		return new TimeServerLinked();
	}
}
