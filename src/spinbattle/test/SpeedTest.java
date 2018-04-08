package spinbattle.test;

import spinbattle.core.SpinGameState;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.HeuristicLauncher;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class SpeedTest {

    static StatSummary constructionTime = new StatSummary("Construction Time");
    static StatSummary runningTime = new StatSummary("Running Time");

    public static void main(String[] args) {

        int nSteps = 1000;
        int nGames = 1000;

        ElapsedTimer timer = new ElapsedTimer();

        StatSummary ss = new StatSummary("Game Scores");
        int nWins = 0;
        int nRand = 0;

        for (int i=0; i<nGames; i++) {
            SpinGameState gameState = playGame(nSteps);
            ss.add(gameState.getScore());
            if (gameState.getScore() > 0) nWins++;
            if (Math.random() < 0.5) nRand++;
        }
        System.out.println(ss);
        System.out.println(constructionTime);
        System.out.println(runningTime);
        System.out.println(timer);
        System.out.println();
        System.out.println("nWins for Player One: " + nWins);
        System.out.println("nHead (forcomparison) " + nRand);

    }

    public static SpinGameState playGame(int nSteps) {
        ElapsedTimer t = new ElapsedTimer();
        SpinBattleParams params = new SpinBattleParams();
        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        HeuristicLauncher launcher = new HeuristicLauncher();

        constructionTime.add(t.elapsed());

        t = new ElapsedTimer();
        for (int i = 0; i < nSteps; i++) {
            gameState.next(null);
            launcher.makeTransits(gameState, Constants.playerTwo);
            launcher.makeTransits(gameState, Constants.playerOne);
        }
        runningTime.add(t.elapsed());
        return gameState;
    }
}
