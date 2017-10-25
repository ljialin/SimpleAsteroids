package gvglink;

import altgame.SimpleMaxGame;
import controllers.singlePlayer.ea.Agent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import ontology.Types;
import rl.grid.GridModel;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */
public class SimpleGridTest {

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


        StateObservation gridGame = new GridModel();

        System.out.println(gridGame.getGameScore());
        System.out.println(gridGame.copy().getGameScore());
        // System.exit(0);

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player;

        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
                new controllers.singlePlayer.sampleOLMCTS.Agent(gridGame, timer);

        controllers.singlePlayer.discountOLMCTS.Agent discountOlmcts =
                new controllers.singlePlayer.discountOLMCTS.Agent(gridGame, timer);

        controllers.singlePlayer.nestedMC.Agent nestedMC =
                new controllers.singlePlayer.nestedMC.Agent(gridGame, timer);


        player = olmcts;

        // player = discountOlmcts;


        // for the following we can pass the Evolutionary algorithm to use

        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 2000;
        double kExplore = 10;
        int nNeighbours = 100;

        evoAlg = new NTupleBanditEA(kExplore, nNeighbours);
        evoAlg = new SlidingMeanEDA();

        // Mutator.totalRandomChaosMutation = false;

        Agent.useShiftBuffer = true;

        Agent.SEQUENCE_LENGTH = 30;

        player = new Agent(gridGame, timer, evoAlg, nEvals);

        nestedMC.maxRolloutLength = 30;
        nestedMC.nestDepth = 3;

        // player = nestedMC;

        int thinkingTime = 50; // in milliseconds
        int delay = 30;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();

        // this is how many steps we'll take in the actual game ...
        int nSteps = 30;

        ElapsedTimer t = new ElapsedTimer();
        // set view to null to run fast with no visuals
        // view = null;


        for (int i=0; i<nSteps && !gridGame.isGameOver(); i++) {
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);


            Types.ACTIONS action = player.act(gridGame.copy(), timer);
            System.out.println();
            System.out.println("Selected: " + action); //  + "\t " + action.ordinal());
            gridGame.advance(action);
            System.out.println("Game state: " + gridGame);
            System.out.println();
        }

        System.out.println(gridGame.getGameScore());

        return gridGame.getGameScore();
    }

}
