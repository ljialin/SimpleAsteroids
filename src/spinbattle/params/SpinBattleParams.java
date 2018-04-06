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
    public int nPlanets = 20;

    public int nNeutral = nPlanets - 2;

    public double minGrowth = 0.05;
    public double maxGrowth = 0.15;

    static Random random = new Random();

    public Random getRandom() {
        return random;
    }

}
