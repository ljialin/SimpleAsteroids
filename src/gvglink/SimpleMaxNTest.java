package gvglink;

import altgame.SimpleMaxGame;
import controllers.singlePlayer.ea.Agent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */
public class SimpleMaxNTest {

    public static void main(String[] args) {
        StatSummary ss = new StatSummary();

        int nTrials = 30;
        for (int i=0; i<nTrials; i++) {
            ss.add(runOnce());
        }

        System.out.println(ss);

    }

    public static double runOnce() {
        // make an agent to test


        StateObservation noiseFree = new SimpleMaxGame();
        StateObservation stateObs = new NoisyMaxGame();

        System.out.println(stateObs.getGameScore());
        System.out.println(stateObs.copy().getGameScore());
        // System.exit(0);

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player;

        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
                new controllers.singlePlayer.sampleOLMCTS.Agent(stateObs, timer);

        controllers.singlePlayer.discountOLMCTS.Agent discountOlmcts =
                new controllers.singlePlayer.discountOLMCTS.Agent(stateObs, timer);

        player = olmcts;

        player = discountOlmcts;


        // for the following we can pass the Evolutionary algorithm to use

        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 1000;
        double kExplore = 10;
        int nNeighbours = 100;

        evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        Mutator.totalRandomChaosMutation = true;

        Agent.useShiftBuffer = false;

        controllers.singlePlayer.ea.Agent.SEQUENCE_LENGTH = 100;
        player = new controllers.singlePlayer.ea.Agent(stateObs, timer, evoAlg, nEvals);


        int thinkingTime = 50; // in milliseconds
        int delay = 30;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();

        // this is how many steps we'll take in the actual game ...
        int nSteps = 10;

        ElapsedTimer t = new ElapsedTimer();
        // set view to null to run fast with no visuals
        // view = null;


        for (int i=0; i<nSteps && !stateObs.isGameOver(); i++) {
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);


            Types.ACTIONS action = player.act(stateObs.copy(), timer);
            // System.out.println("Selected: " + action); //  + "\t " + action.ordinal());
            stateObs.advance(action);
            noiseFree.advance(action);
            // System.out.println(stateObs.getGameScore());
        }

        System.out.println(stateObs.getGameScore());
        System.out.println(noiseFree.getGameScore());
        System.out.println(stateObs.isGameOver());

        System.out.println(t);

        return noiseFree.getGameScore();
    }

    static class NoisyMaxGame extends SimpleMaxGame {
        // same as the simple one but return a noisy game score

        static Random random = new Random();
        static double noiseLevel = 1;

        public NoisyMaxGame() {
        }

        public NoisyMaxGame(int gameScore, int gameTick) {
            super(gameScore, gameTick);
        }

        public double getGameScore() {
            return super.getGameScore() + noiseLevel * random.nextGaussian();
        }
        public StateObservation copy() {
            return new NoisyMaxGame((int) getGameScore(), getGameTick());
        }
    }
}
