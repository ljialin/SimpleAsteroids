package spinbattle;

import java.util.Random;

public class SpinBattleParams {

    public SpinBattleParams copy() {
        // todo a proper copy
        return this;
    }

    // of the arena
    int width=700, height=400;

    static Random random = new Random();

    Random getRandom() {
        return random;
    }

}
