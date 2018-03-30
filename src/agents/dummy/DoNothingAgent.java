package agents.dummy;

import ggi.AbstractGameState;
import ggi.SimplePlayerInterface;

public class DoNothingAgent implements SimplePlayerInterface {

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        // return zero without knowing what this will do
        return 0;
    }

    public String toString() {
        return "Do Nothing Agent";
    }
}
