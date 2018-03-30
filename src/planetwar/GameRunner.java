package planetwar;

import ggi.SimplePlayerInterface;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;

public class GameRunner {

    SimplePlayerInterface p1, p2;
    int nSteps = 200;
    StatSummary scores;
    int p1Wins;
    int p2Wins;
    int nGames;

    int nInitialLeaderWins;
    static int p1Index = 0;
    static int p2Index = 1;


    ArrayList<GameLog> gameLogs;
    boolean verbose = true;

    public GameRunner setPlayers(SimplePlayerInterface p1, SimplePlayerInterface p2) {
        this.p1 = p1;
        this.p2 = p2;
        reset();
        return this;
    }

    public GameRunner setLength(int nSteps) {
        this.nSteps = nSteps;
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

    public GameRunner playGames(int n) {
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


    // boolean verbose = true;
    public GameRunner playGame() {
        GameState gameState = new GameState().defaultState();
        GameLog gameLog = new GameLog();
        gameLog.addScore(gameState.getScore());
        gameLog.setInitialGrowthRate(gameState.totalGrowthRate());
        int[] actions = new int[2];
        for (int i=0; i<nSteps; i++) {
            actions[0] = p1.getAction(gameState.copy(), p1Index);
            actions[1] = p2.getAction(gameState.copy(), p2Index);
            gameState.next(actions);
            gameLog.addScore(gameState.getScore());
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
