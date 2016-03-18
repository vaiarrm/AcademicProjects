package sim.participants;

import sim.agent.Agent;
import sim.participants.DataFactory.LIGHT_STATE;

public interface LightController extends Agent{
	public LIGHT_STATE getLightstate();
	public void setLightState(LIGHT_STATE state);
}
