package spinbattle.params;

import spinbattle.core.Collider;

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

    public int minInitialShips = 5;
    public int maxInitialShips = 20;

    public int transitSpeed = 2;

    static Random random = new Random();

    static Collider collider = new Collider();

    public Random getRandom() {
        return random;
    }

    public Collider getCollider() {
        return collider;
    }

}
