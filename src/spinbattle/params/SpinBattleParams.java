package spinbattle.params;

import java.util.Random;

public class SpinBattleParams {

    public SpinBattleParams copy() {
        // todo a proper copy
        return this;
    }

    // of the arena
    public int width=700, height=400;

    public int maxTicks = 500;
    int nPlanets = 20;

    int nNeutral = nPlanets - 2;

    static Random random = new Random();

    public Random getRandom() {
        return random;
    }

}
