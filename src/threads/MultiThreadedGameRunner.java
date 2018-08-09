package threads;

import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import planetwar.GameLog;
import utilities.StatSummary;

import java.util.ArrayList;

public class MultiThreadedGameRunner {

    int nSteps = 100;
    public int p1Wins;
    public int p2Wins;
    public int nGames;

    public StatSummary scores;


    public int nInitialLeaderWins;
    public static int p1Index = 0;
    public static int p2Index = 1;

    public void reset() {
        scores = new StatSummary("Game score stats");
        nGames = 0;
        p1Wins = 0;
        p2Wins = 0;
        nInitialLeaderWins = 0;
        // gameLogs = new ArrayList<>();
    }



    public AbstractGameFactory gameFactory;

    public MultiThreadedGameRunner setGameFactory(AbstractGameFactory gameFactory) {
        this.gameFactory = gameFactory;
        // reset();
        return this;
    }




    public MultiThreadedGameRunner playGame(SimplePlayerInterface p1, SimplePlayerInterface p2) {
        System.out.println(gameFactory);
        AbstractGameState gameState = gameFactory.newGame();
        GameLog gameLog = new GameLog();
        gameLog.addScore(gameState.getScore());

        p1.reset();
        p2.reset();

        // this is an interesting problem to solve
        // how to provide logging of game-specific values within
        // a general game runner class
        // gameLog.setInitialGrowthRate(gameState.totalGrowthRate());
        int[] actions = new int[2];
        for (int i=0; i<nSteps && !gameState.isTerminal(); i++) {
            actions[0] = p1.getAction(gameState.copy(), p1Index);
            actions[1] = p2.getAction(gameState.copy(), p2Index);
            gameState.next(actions);
            gameLog.addScore(gameState.getScore());
            delay();
        }
        scores.add(gameState.getScore());

        if (gameState.getScore() > 0) p1Wins++;
        if (gameState.getScore() < 0) p2Wins++;
        nGames++;
        if (gameLog.leaderHadAdvantage()) nInitialLeaderWins++;
        // gameLogs.add(gameLog);
        return this;
    }


    int delayMillis = 40;
    private void delay() {
        try {
            Thread.sleep(delayMillis);
        } catch (Exception e) {}
    }



}
