package agents.dummy;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

import java.util.Random;

public class RandomAgent implements SimplePlayerInterface {

    Random random = new Random();

    public RandomAgent setSeed(long seed) {
        random.setSeed(seed);
        return this;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        return random.nextInt(gameState.nActions());
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }

    public String toString() {
        return "Uniform Random Agent";
    }

}
