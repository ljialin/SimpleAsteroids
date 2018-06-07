package spinbattle.players;

import core.player.AbstractMultiPlayer;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import gvglink.SpinBattleLinkState;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class GVGAIWrapper implements SimplePlayerInterface {

    AbstractMultiPlayer agent;

    public GVGAIWrapper setAgent(AbstractMultiPlayer agent) {
        this.agent = agent;
        return this;
    }

    // thinking time in milli seconds
    int thinkingTime = 10;

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        SpinBattleLinkState linkState = new SpinBattleLinkState(gameState);

        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        timer.setMaxTimeMillis(thinkingTime);

        Types.ACTIONS actions = agent.act(linkState, timer);
        return actions.ordinal();
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }

    public String toString() {
        return "GVGAIWrap: " + agent.toString();
    }
}
