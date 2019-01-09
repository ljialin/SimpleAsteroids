package caveswing.test;

import agents.dummy.RandomAgent;
import caveswing.core.CaveGameFactory;
import caveswing.core.CaveSwingParams;
import ggi.tests.SpeedTest;

public class CaveGameSpeedTest {
    public static void main(String[] args) {
        CaveSwingParams params = new CaveSwingParams();
//        params.gravity.y = -0.0;
//        params.gravity.x = 10.5;
        SpeedTest speedTest = new SpeedTest().setGameFactory(
                new CaveGameFactory().setParams(params));
        speedTest.copyTest = false;
        speedTest.setPlayer(new RandomAgent());
        int nGames = 10000;
        int maxSteps = 1000;
        speedTest.playGames(nGames, maxSteps).report();
    }
}

