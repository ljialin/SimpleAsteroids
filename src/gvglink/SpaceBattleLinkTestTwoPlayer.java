package gvglink;

import battle.BattleGameParameters;
import battle.BattleView;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapterMulti;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.CompactSlidingModelGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */
public class SpaceBattleLinkTestTwoPlayer {

    public static boolean runVisible = true;
    public static boolean showVisible = true;

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

        // StateObservation stateObs = new SimpleMaxGame();

        // BattleGameSearchSpace.inject(BattleGameSearchSpace.getRandomPoint());

        // SampleEvolvedParams.solutions[1][2] = 5;
        // SampleEvolvedParams.solutions[5][4] = 0;


        // BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[2]);
        // BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[2]);
        // BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[1]);

        System.out.println("Params are:");
        System.out.println(BattleGameParameters.params);

        // can also override parameters by setting them directly as follows:
        BattleGameParameters.loss = 0.995;
        BattleGameParameters.thrust = 2;
        SpaceBattleLinkStateTwoPlayer linkState = new SpaceBattleLinkStateTwoPlayer();
        StateObservationMulti multi = linkState;

        GameActionSpaceAdapterMulti.useHeuristic = false;

        Mutator.totalRandomChaosMutation = false;

        // // supercl
        // StateObservation stateObs = linkState;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractMultiPlayer player1, player2;

//        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
//                new controllers.singlePlayer.sampleOLMCTS.Agent(linkState, timer);

        int idPlayer1 = 0;
        int idPlayer2 = 1;

        player2 = new controllers.multiPlayer.discountOLMCTS.Agent(linkState, timer, idPlayer2);

        // try the evolutionary players

        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        double kExplore = 10;
        int nNeighbours = 100;

        int nEvals = 500;
        evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        // evoAlg = new CompactSlidingModelGA().setHistoryLength(20);
        evoAlg = new SlidingMeanEDA().setHistoryLength(20);

        EvoAlg evoAlg2 = new CompactSlidingModelGA().setHistoryLength(2);


        player1 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg, idPlayer1, nEvals);
        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer2, nEvals);
        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, new SimpleRMHC(nResamples), idPlayer2, nEvals);

        // player1  = new controllers.multiPlayer.smlrand.Agent();

        // EvoAlg evoAlg2 = new SimpleRMHC(2);

        // player1 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer1, nEvals);


        int thinkingTime = 50; // in milliseconds
        int delay = 10;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();
        int nSteps = 500;

        ElapsedTimer t = new ElapsedTimer();
        BattleView view = new BattleView(linkState.state.copyState());
//        if (showVisible)
//            view.evoAlg = evoAlg;


        // set view to null to run fast with no visuals
        if (!runVisible) view = null;

        if (view != null) {
            new JEasyFrame(view, "Simple Battle Game");
        }

        StatSummary sst1 = new StatSummary("Player 1 Elapsed Time");
        StatSummary sst2 = new StatSummary("Player 2 Elapsed Time");

        StatSummary ssTicks1 = new StatSummary("Player 1 nTicks");
        StatSummary ssTicks2 = new StatSummary("Player 2 nTicks");

        for (int i=0; i<nSteps && !linkState.isGameOver(); i++) {
            linkState.state = linkState.state.copyState();
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);

            ElapsedTimer t1 = new ElapsedTimer();

            // keep track of the number of game ticks used by each algorithm
            int ticks;

            ticks = SpaceBattleLinkStateTwoPlayer.nTicks;
            Types.ACTIONS action1 = player1.act(multi.copy(), timer);
            sst1.add(t1.elapsed());
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks - ticks;
            ssTicks1.add(ticks);
            // System.out.println("Player 1 Ticks = " + ticks);

            ElapsedTimer t2 = new ElapsedTimer();
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks;
            Types.ACTIONS action2 = player2.act(multi.copy(), timer);
            sst2.add(t2.elapsed());
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks - ticks;
            ssTicks2.add(ticks);

            // System.out.println("Player 2 Ticks = " + ticks);
            multi.advance(new Types.ACTIONS[]{action1, action2});

            if (view != null) {
                view.game = linkState.state.copyState();
                view.setRolls(0, evoAlg);
                view.setRolls(1, evoAlg2);
                view.repaint();
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {}
            }

            // System.out.println(multi.getGameScore());
        }

        System.out.println(multi.getGameScore());
        System.out.println(multi.isGameOver());

        // System.out.println(SingleTreeNode.rollOutScores);

        System.out.println(sst1);
        System.out.println(sst2);

        System.out.println(ssTicks1);
        System.out.println(ssTicks2);
        return multi.getGameScore(0);
    }
}

