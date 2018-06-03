package planetwar;

import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class RandomTestPlanetWars {

    public static void main(String[] args) {

        int nTrials = 100;

        SimplePlayerInterface player = new RandomAgent();

        StatSummary ss = new StatSummary("Random Player on Planet Wars");
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<nTrials; i++) {
            ss.add(evaluate(player));
        }

        System.out.println(ss);
        System.out.println();
        System.out.println(t);

    }


    public static double evaluate(SimplePlayerInterface player) {


        GameRunnerTwoPlayer gameRunner = new GameRunnerTwoPlayer();


        EvoAlg evoAlgOpponent = new SimpleRMHC();

        // set up some defaults for opponent
        int nOpponentEvals = 400;
        int opponentSeqLength = 5;

        EvoAgent evoOpponent =
                new EvoAgent().setEvoAlg(evoAlgOpponent, nOpponentEvals).setSequenceLength(opponentSeqLength);

        // setting to false provides a much weaker opponent
        evoOpponent.setUseShiftBuffer(true);

        // now run a game and return the result

        gameRunner.setGameFactory(new PlanetWarGameFactory());
        gameRunner.verbose = false;
        gameRunner.reset();
        gameRunner.setPlayers(player, evoOpponent);
        gameRunner.playGame();

        double fitness = gameRunner.scores.mean();
        double value = 0;

        if (fitness > 0) value = 1;
        if (fitness < 0) value = -1;

        System.out.println("Fitness: " + (int) fitness + " : " + value);
        System.out.println();

        return value;
    }

}
