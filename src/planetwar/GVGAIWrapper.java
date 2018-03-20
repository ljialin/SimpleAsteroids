package planetwar;

import controllers.multiPlayer.ea.Agent;
import core.player.AbstractMultiPlayer;
import gvglink.PlanetWarsLinkState;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;

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
