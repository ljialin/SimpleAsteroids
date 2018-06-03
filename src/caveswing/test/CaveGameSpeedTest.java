package caveswing.test;

import agents.dummy.RandomAgent;
import caveswing.core.CaveGameFactory;
import ggi.core.SimplePlayerInterface;
import ggi.tests.SpeedTest;

public class CaveGameSpeedTest {
    public static void main(String[] args) {
        SpeedTest speedTest = new SpeedTest().setGameFactory(new CaveGameFactory());
        speedTest.setPlayer(new RandomAgent());
        int nGames = 1000;
        int maxSteps = 1000;
        speedTest.playGames(nGames, maxSteps).report();
    }
}
