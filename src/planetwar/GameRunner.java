package planetwar;

import utilities.ElapsedTimer;
import utilities.StatSummary;

public class GameRunner {

    SimplePlayerInterface p1, p2;
    int nSteps = 200;
    StatSummary scores;
    int p1Wins;
    int p2Wins;
    int nGames;

    static int p1Index = 0;
    static int p2Index = 1;

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
    }

    public GameRunner playGames(int n) {
        // plays an additional n games without resetting the stats
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<n; i++) {
            playGame();
        }
        if (verbose) {
            System.out.println(scores);
            System.out.println();
            System.out.println("p1 wins:\t " + p1Wins);
            System.out.println("p2 wins:\t " + p2Wins);
            System.out.println("n games:\t " + nGames);
            System.out.println(t);
        }
        System.out.println();
        return this;
    }

    public GameRunner playGame() {
        GameState gameState = new GameState().defaultState();
        int[] actions = new int[2];
        for (int i=0; i<nSteps; i++) {
            actions[0] = p1.getAction(gameState.copy(), p1Index);
            actions[1] = p2.getAction(gameState.copy(), p2Index);
            gameState.next(actions);
        }
        scores.add(gameState.getScore());
        if (gameState.getScore() > 0) p1Wins++;
        if (gameState.getScore() < 0) p2Wins++;
        nGames++;
        return this;
    }
}
