package asteroids;

import planetwar.SimplePlayerInterface;

public class EvoAgentAdapter implements Controller {
    // this is needed because the EvoAgent returns an int and this needs to return an Action
    // as defined in the Asteroids gameState

    static ActionAdapter actionAdapter = new ActionAdapter();

    SimplePlayerInterface agent;

    public EvoAgentAdapter setAgent(SimplePlayerInterface agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public Action action(AsteroidsGameState game) {
        return actionAdapter.getAction(agent.getAction(game, 0));
    }

    @Override
    public void setVehicle(Ship ship) {

    }
}
