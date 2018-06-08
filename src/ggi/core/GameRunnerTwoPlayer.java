package ggi.core;

import planetwar.GameLog;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;

public class GameRunnerTwoPlayer {

    public SimplePlayerInterface p1, p2;
    public int nSteps = 200;
    public StatSummary scores;
    public int p1Wins;
    public int p2Wins;
    public int nGames;

    public int nInitialLeaderWins;
    public static int p1Index = 0;
    public static int p2Index = 1;

    ArrayList<GameLog> gameLogs;
    public boolean verbose = true;

    public AbstractGameFactory gameFactory;


    public GameRunnerTwoPlayer setPlayers(SimplePlayerInterface p1, SimplePlayerInterface p2) {
        this.p1 = p1;
        this.p2 = p2;
        reset();
        return this;
    }

    public GameRunnerTwoPlayer setPlayersWithoutReset(SimplePlayerInterface p1, SimplePlayerInterface p2) {
        this.p1 = p1;
        this.p2 = p2;
        return this;
    }

    public GameRunnerTwoPlayer setLength(int nSteps) {
        this.nSteps = nSteps;
        reset();
        return this;
    }

    public GameRunnerTwoPlayer setGameFactory(AbstractGameFactory gameFactory) {
        this.gameFactory = gameFactory;
        reset();
        return this;
    }

    public void reset() {
        scores = new StatSummary("Game score stats");
        nGames = 0;
        p1Wins = 0;
        p2Wins = 0;
        nInitialLeaderWins = 0;
        gameLogs = new ArrayList<>();
    }

    public GameRunnerTwoPlayer playGames(int n) {
        // plays an additional n games without resetting the stats
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<n; i++) {
            playGame();
        }
        if (verbose) {
            System.out.println(p1 + " versus " + p2);
            System.out.println(scores);
            System.out.println();
            System.out.println("p1 wins:\t " + p1Wins);
            System.out.println("p2 wins:\t " + p2Wins);
            System.out.println("n games:\t " + nGames);
            System.out.println("Init leader wins: " + nInitialLeaderWins);
            System.out.println(t);
        }
        System.out.println();
        return this;
    }


    public GameRunnerTwoPlayer playGame() {
        playGame(p1, p2);
        // playGame()
        return this;
    }

    public GameRunnerTwoPlayer playBothGames() {
        playGame(p1, p2);
        playGame(p2, p1);
        return this;
    }

    // boolean verbose = true;

    public Integer delay = null;

    private void delay() {
        if (delay != null) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public GameRunnerTwoPlayer playGame(SimplePlayerInterface p1, SimplePlayerInterface p2) {
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
        if (verbose) {
            System.out.format("Game %d, score: %d\n", nGames, (int) gameState.getScore());
            System.out.println("Lead changes: "+ gameLog.leadChanges);
            System.out.println(gameLog);
            System.out.println();
            System.out.println();
        }
        nGames++;
        if (gameLog.leaderHadAdvantage()) nInitialLeaderWins++;
        gameLogs.add(gameLog);
        return this;
    }

    public LineChart plotGameScores() {
        LineChart lineChart = new LineChart();
        for (GameLog gameLog : gameLogs) {
            LinePlot linePlot = new LinePlot().setData(gameLog.scores).setRandomColor();
            lineChart.addLine(linePlot);
        }
        lineChart.xAxis = new LineChartAxis(new double[]{0, nSteps/2, nSteps});
        lineChart.setXLabel("Game Tick");
        lineChart.setYLabel("Score");
        String title = String.format("%s (%d) v. %s (%d)", p1.toString(), p1Wins,
                p2.toString(), p2Wins);
        lineChart.setTitle(title);
        lineChart.bg = Color.gray;
        lineChart.plotBG = Color.white;

        double[] scoreTics = new  double[]{scores.min(), 0, scores.max()};
        // double[] scoreTics = new  double[]{-100, 0, 1000, 2000 }; // scores.max()};

        lineChart.yAxis = new LineChartAxis(scoreTics);
        new JEasyFrame(lineChart, "Game Scores");
        return lineChart;
    }
}
