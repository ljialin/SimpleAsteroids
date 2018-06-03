package caveswing.test;

import agents.dummy.RandomAgent;
import caveswing.core.CaveGameFactory;
import caveswing.core.CaveSwingParams;
import ggi.tests.SpeedTest;
import math.Vector2d;

public class CaveGameParamSanityCheck {
    public static void main(String[] args) {
        CaveSwingParams params = new CaveSwingParams();
        // set gravity to vary difficulty
        params.gravity = new Vector2d(0.02, 0);
        // setting hooke to zero means that actions should have no effect
        params.hooke = 0;
        CaveGameFactory gameFactory = new CaveGameFactory().setParams(params);
        SpeedTest speedTest = new SpeedTest().setGameFactory(gameFactory);
        speedTest.setPlayer(new RandomAgent());
        int nGames = 1000;
        int maxSteps = 1000;
        speedTest.playGames(nGames, maxSteps).report();
    }
}
