package gvglink;

import altgame.SimpleMaxGame;
import battle.BattleGameParameters;
import battle.BattleView;
import battle.SampleEvolvedParams;
import controllers.singlePlayer.discountOLMCTS.SingleMCTSPlayer;
import controllers.singlePlayer.discountOLMCTS.SingleTreeNode;
import controllers.singlePlayer.ea.Agent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
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
public class SpaceBattleLinkTest {

    public static boolean runVisible = false;

    public static void main(String[] args) {
        StatSummary ss = new StatSummary();
        int nTrials = 10;
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<nTrials; i++) {
            System.out.println("Trial: " + i);
            ss.add(runTrial(runVisible));
            System.out.println();
        }
        System.out.println(ss);
        System.out.println();
        System.out.println(t);
    }

    public static double runTrial(boolean runVisible) {
        // make an agent to test

        StateObservation stateObs = new SimpleMaxGame();

        // BattleGameSearchSpace.inject(BattleGameSearchSpace.getRandomPoint());

        // SampleEvolvedParams.solutions[1][2] = 5;
        // BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[1]);
        // BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[2]);
        BattleGameSearchSpace.inject(SampleEvolvedParams.solutions[1]);

        System.out.println("Params are:");
        System.out.println(BattleGameParameters.params);

        // can also overide parameters by setting them directly as follows:
        // BattleGameParameters.loss = 1.1;
        SpaceBattleLinkState linkState = new SpaceBattleLinkState();


        // set some parameters for the experiment
        GameActionSpaceAdapter.useHeuristic = false;
        Agent.useShiftBuffer = true;
        Mutator.totalRandomChaosMutation = false;



        // // supercl
        // StateObservation stateObs = linkState;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player;

//        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
//                new controllers.singlePlayer.sampleOLMCTS.Agent(linkState, timer);

        player = new controllers.singlePlayer.discountOLMCTS.Agent(linkState, timer);


        // try the evolutionary players


        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        double kExplore = 10;
        int nNeighbours = 100;

        int nEvals = 500;
        evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        player = new controllers.singlePlayer.ea.Agent(linkState, timer, evoAlg, nEvals);



        int thinkingTime = 50; // in milliseconds
        int delay = 10;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();
        int nSteps = 500;

        ElapsedTimer t = new ElapsedTimer();
        BattleView view = new BattleView(linkState.state);

        // set view to null to run fast with no visuals
         if (!runVisible) view = null;

        if (view != null) {
            new JEasyFrame(view, "Simple Battle Game");
        }

        boolean verbose = false;

        for (int i=0; i<nSteps && !linkState.isGameOver(); i++) {
            ArrayList<Types.ACTIONS> actions = linkState.getAvailableActions();

            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);
            Types.ACTIONS action = player.act(linkState.copy(), timer);

            // use this for a random action
            // action = actions.get(random.nextInt(actions.size()));
            if (verbose)
                System.out.println(i + "\t Selected: " + action); //  + "\t " + action.ordinal());
            linkState.advance(action);

            if (view != null) {
                view.repaint();
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {}
            }

            if (verbose)
                System.out.println(linkState.getGameScore());
        }

        System.out.println("Game score: " + linkState.getGameScore());
        // System.out.println(linkState.isGameOver());

        // System.out.println(SingleTreeNode.rollOutScores);

        return linkState.getGameScore();

    }
}
