package caveswing.core;

import math.Vector2d;

import java.util.Random;

public class CaveSwingParams {
    // duration
    public int maxTicks = 1000;
    // forces
    public Vector2d gravity = new Vector2d(0, 0.4);
    public double hooke = 0.02;
    public double lossFactor = 0.9999;
    // public double lossFactor = 1.1;

    // game map params
    public int width = 800;
    public int height = 250;
    public int nAnchors = 10;
    public double meanAnchorHeight = height * 0.2;

    // score related params
    public int successBonus = 1000;
    public int failurePenalty = 1000;
    public int pointPerX = 10;
    public int pointPerY = -10;
    public int costPerTick = 1;

    public Random random = new Random();

    public Random getRandom() {
        return random;
    }

    public CaveSwingParams copy() {
        CaveSwingParams params = new CaveSwingParams();
        params.maxTicks = maxTicks;
        params.gravity = gravity.copy();
        params.hooke = hooke;
        params.width = width;
        params.nAnchors = nAnchors;
        params.meanAnchorHeight = meanAnchorHeight;
        params.random = random;
        params.lossFactor = lossFactor;

        params.successBonus = successBonus;
        params.failurePenalty = failurePenalty;
        params.pointPerX = pointPerX;
        params.pointPerY = pointPerY;
        params.costPerTick = costPerTick;

        return params;
    }
}
