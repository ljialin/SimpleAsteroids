package ggi.tests;

import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class SpeedTest {
    public SimplePlayerInterface[] players;
    public AbstractGameFactory gameFactory;
    boolean copyTest = false;

    StatSummary runTime = new StatSummary("Run Times");
    StatSummary gameLength = new StatSummary("Game Length");
    StatSummary gameScores = new StatSummary("Game Scores");
    // StatSummary copyTime

    int totalTicks = 0;

    public SpeedTest playGames(int nGames, int maxSteps) {
        for (int i=0; i<nGames; i++) {
            playGame(maxSteps);
        }
        return this;
    }

    public SpeedTest playGame(int maxSteps) {
        AbstractGameState gameState = gameFactory.newGame();
        int nSteps = 0;
        ElapsedTimer t = new ElapsedTimer();
        for (int i = 0; i<maxSteps && !gameState.isTerminal(); i++) {
            int[] actions = new int[players.length];
            for (int j=0; j<players.length; j++) {
                actions[j] = players[j].getAction(gameState, j);
            }
            gameState.next(actions);
            if (copyTest)
                gameState = gameState.copy();
            nSteps++;
            totalTicks++;
        }
        runTime.add(t.elapsed());
        gameLength.add(nSteps);
        gameScores.add(gameState.getScore());

        return this;
    }

    public void report() {
        System.out.println(runTime + "\n");
        System.out.println(gameLength + "\n");
        System.out.println(gameScores + "\n");
        System.out.println();
        double ticksPerMilli = totalTicks / runTime.sum();
        System.out.println("Ticks per millisecond: " + (int) ticksPerMilli);
        System.out.println("Game copied each tick? " + copyTest);
    }

}
