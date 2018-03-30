package planetwar;

import core.player.AbstractMultiPlayer;
import ggi.AbstractGameState;
import ggi.SimplePlayerInterface;
import gvglink.PlanetWarsLinkState;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class GVGAIWrapper implements SimplePlayerInterface {

    AbstractMultiPlayer agent;

    public GVGAIWrapper setAgent(AbstractMultiPlayer agent) {
        this.agent = agent;
        return this;
    }

    // thinking time in milli seconds
    int thinkingTime = 5;

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        PlanetWarsLinkState linkState = new PlanetWarsLinkState(gameState);

        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        timer.setMaxTimeMillis(thinkingTime);

        Types.ACTIONS actions = agent.act(linkState, timer);
        return actions.ordinal();
    }
}
