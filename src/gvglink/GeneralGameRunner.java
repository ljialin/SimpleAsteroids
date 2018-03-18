package gvglink;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

public class GeneralGameRunner {


    // meant to be general


    AbstractMultiPlayer p1, p2;
    int nSteps = 200;
    StatSummary scores;
    int p1Wins;
    int p2Wins;
    int nGames;

    static int p1Index = 0;
    static int p2Index = 1;

    boolean verbose = true;

    public GeneralGameRunner setPlayers(AbstractMultiPlayer p1, AbstractMultiPlayer p2) {
        this.p1 = p1;
        this.p2 = p2;
        reset();
        return this;
    }

    public GeneralGameRunner setLength(int nSteps) {
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

    StateObservationMulti gameState;
    public GeneralGameRunner setGame(StateObservationMulti gameState) {
        this.gameState = gameState;
        return this;
    }


    public GeneralGameRunner playGames(int n) {
        // plays an additional n games without resetting the stats
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<n; i++) {
            System.out.println("Playing game: " + i);
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

    static Random random = new Random();

    public GeneralGameRunner playGame() {

        StateObservationMulti currentGame = gameState.copy();
        currentGame.setNewSeed(random.nextInt(100000));
        // currentGame.setup()

        // int[] actions = new int[2];
        Types.ACTIONS[] actions = new Types.ACTIONS[2];
        for (int i=0; i<nSteps; i++) {
            ElapsedCpuTimer timer = new ElapsedCpuTimer();

            actions[0] = p1.act(currentGame.copy(), timer);
            actions[1] = p2.act(currentGame.copy(), timer);
            currentGame.advance(actions);
        }
        scores.add(currentGame.getGameScore());
        if (currentGame.getGameScore() > 0) p1Wins++;
        if (currentGame.getGameScore() < 0) p2Wins++;
        nGames++;
        return this;
    }
}
