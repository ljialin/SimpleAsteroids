package caveswing.test;

import agents.dummy.RandomAgent;
import caveswing.core.CaveGameFactory;
import ggi.core.SimplePlayerInterface;
import ggi.tests.SpeedTest;

public class CaveGameSpeedTest {
    public static void main(String[] args) {
        SpeedTest speedTest = new SpeedTest();
        speedTest.gameFactory = new CaveGameFactory();
        speedTest.players = new SimplePlayerInterface[]{new RandomAgent()};
        int nGames = 10000;
        int maxSteps = 1000;
        speedTest.playGames(nGames, maxSteps).report();
    }
}
