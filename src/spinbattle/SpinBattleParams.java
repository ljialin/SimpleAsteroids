package spinbattle;

import java.util.Random;

public class SpinBattleParams {

    public SpinBattleParams copy() {
        // todo a proper copy
        return this;
    }

    // of the arena
    public int width=700, height=400;

    public int maxTicks = 500;

    static Random random = new Random();

    Random getRandom() {
        return random;
    }

}
