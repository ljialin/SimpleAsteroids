package gvglink;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.DefaultMutator;
import ga.SimpleRMHC;
import numbergame.DiffGame;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */
public class TestDiffGame {

    public static boolean runVisible = true;

    public static void main(String[] args) {
        StatSummary ss = new StatSummary();
        int nTrials = 10;
        ElapsedTimer t = new ElapsedTimer();

        ArrayList<Double> results = new ArrayList<>();

        for (int i=0; i<nTrials; i++) {
            double result = runTrial(runVisible);
            ss.add(result);
            results.add(result);
        }
        System.out.println(ss);
        System.out.println();
        System.out.println(results);
        System.out.println(t);
    }

    public static double runTrial(boolean runVisible) {
        // make an agent to test

        DiffGame.nValues = 21;
        DiffGame.minscore = 0;
        StateObservationMulti stateObs = new DiffGame();

        DefaultMutator.totalRandomChaosMutation = false;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractMultiPlayer player1, player2;


        int idPlayer1 = 0;
        int idPlayer2 = 1;


        // try the evolutionary players

        int nResamples = 10;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);
        EvoAlg evoAlg2 = new SimpleRMHC(nResamples);



        double kExplore = 2;
        int nNeighbours = 50;

        int nEvals = 1500;
        // evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        controllers.multiPlayer.ea.Agent agentEAShift =
                new controllers.multiPlayer.ea.Agent(stateObs, timer, evoAlg, idPlayer1, nEvals);
        agentEAShift.useShiftBuffer = true;
        agentEAShift.sequenceLength = 5;
        player1 = agentEAShift;

        controllers.multiPlayer.ea.Agent agentEANoShift =
                new controllers.multiPlayer.ea.Agent(stateObs, timer, evoAlg2, idPlayer2, nEvals);
        agentEANoShift.useShiftBuffer = true;
        agentEANoShift.sequenceLength = 25;
        player2 = agentEANoShift;

        // player2 = new controllers.multiPlayer.discountOLMCTS.Agent(stateObs, timer, idPlayer2);

        // player2 = new controllers.multiPlayer.doNothing.Agent(stateObs, timer, idPlayer2);

        // player2 = new controllers.multiPlayer.sampleRandom.Agent(stateObs, timer, idPlayer2);

        // player1  = new controllers.multiPlayer.smlrand.Agent();

        // EvoAlg evoAlg2 = new SimpleRMHC(1);

        // player1 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer1, nEvals / 5);

        int thinkingTime = 50; // in milliseconds
        int delay = 10;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();
        int nSteps = 50;

        StatSummary sst1 = new StatSummary("Player 1 Elapsed Time");
        StatSummary sst2 = new StatSummary("Player 2 Elapsed Time");

        StatSummary ssTicks1 = new StatSummary("Player 1 nTicks");
        StatSummary ssTicks2 = new StatSummary("Player 2 nTicks");

        for (int i=0; i<nSteps && !stateObs.isGameOver(); i++) {
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);

            ElapsedTimer t1 = new ElapsedTimer();

            // keep track of the number of game ticks used by each algorithm
            int ticks;

            ticks = DiffGame.nTicks;
            Types.ACTIONS action1 = player1.act(stateObs.copy(), timer);
            sst1.add(t1.elapsed());
            ticks = DiffGame.nTicks - ticks;
            ssTicks1.add(ticks);
            System.out.println("Player 1 Ticks = " + ticks);

            ElapsedTimer t2 = new ElapsedTimer();
            ticks = DiffGame.nTicks;
            Types.ACTIONS action2 = player2.act(stateObs.copy(), timer);
            sst2.add(t2.elapsed());
            ticks = DiffGame.nTicks - ticks;
            ssTicks2.add(ticks);

            stateObs.advance(new Types.ACTIONS[]{action1, action2});

            // System.out.println(multi.getGameScore());
            System.out.println(stateObs);
        }

        System.out.println(stateObs.getGameScore());
        System.out.println(stateObs.isGameOver());

        // System.out.println(SingleTreeNode.rollOutScores);

        System.out.println(sst1);
        System.out.println(sst2);

        System.out.println(ssTicks1);
        System.out.println(ssTicks2);
        return stateObs.getGameScore(0);

    }
}
