package agents.dummy;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

public class DoNothingAgent implements SimplePlayerInterface {

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        // return zero without knowing what this will do
        return 0;
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }

    public String toString() {
        return "Do Nothing Agent";
    }
}
