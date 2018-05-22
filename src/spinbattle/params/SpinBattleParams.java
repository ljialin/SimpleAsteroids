package spinbattle.params;

import math.Vector2d;
import spinbattle.core.Collider;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class SpinBattleParams {


    public static void main(String[] args) {
        // run a copy test
        SpinBattleParams params = new SpinBattleParams();
        SpinBattleParams alt = params.copy();

        // test should output true, false
        alt.width = 100;
        System.out.println(params.inBounds(new Vector2d(150, 150)));
        System.out.println(alt.inBounds(new Vector2d(150, 150)));
    }


    // of the arena
    public int width=700, height=400;

    public int maxTicks = 500;
    public int nPlanets = 20;

    public int nNeutral = nPlanets - 4;

    public double minGrowth = 0.05;
    public double maxGrowth = 0.15;

    public double spinRatio = 0.4;

    public int minInitialShips = 5;
    public int maxInitialShips = 20;

    public double transitSpeed = 3;

    // this is for a heuristic AI opponent
    public int releasePeriod = 400;

    public boolean clampZeroScore = false;

    public boolean useProximityMap = true;

    public boolean useVectorField = true;

    public double transportTax = 0.0;

    public SpinBattleParams copy() {
        // todo a proper copy
        SpinBattleParams p = new SpinBattleParams();
        p.width = width;
        p.height = height;
        p.maxTicks = maxTicks;
        p.nPlanets = nPlanets;
        p.nNeutral = nNeutral;
        p.minGrowth = minGrowth;
        p.maxGrowth = maxGrowth;
        p.spinRatio = spinRatio;
        p.minInitialShips = minInitialShips;
        p.maxInitialShips = maxInitialShips;
        p.transitSpeed = transitSpeed;
        p.releasePeriod = releasePeriod;
        p.useProximityMap = useProximityMap;
        p.useVectorField = useVectorField;
        p.gravitationalFieldConstant = gravitationalFieldConstant;
        p.gravitationalForceConstant = gravitationalForceConstant;
        p.clampZeroScore = clampZeroScore;
        p.transportTax = transportTax;
        return p;
    }

    // these could be collapsed in to a single parameter
    // for functional purposes
    // but adjusting both of them gives a clumsy way to control the display
    // of the field (which should really be done in the view class
    // todo: collapse in to a single constant, and add a separate cosmetic param to control draw length of vectors
    public double gravitationalFieldConstant = 1.2;
    public double gravitationalForceConstant = 0.001;

    public static Random random = new Random();

    static Collider collider = new Collider();

    public Random getRandom() {
        return random;
    }

    public Collider getCollider() {
        return collider;
    }

    public boolean inBounds(Vector2d s) {
        return s.x >= 0 && s.x <= width && s.y >= 0 && s.y <= height;
    }

    public static Color[] playerColors = {
            Color.getHSBColor(0.17f, 1, 1),
            Color.getHSBColor(0.50f, 1, 1),
    };

}
