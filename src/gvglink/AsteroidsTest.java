package gvglink;

import asteroids.View;
import controllers.singlePlayer.discountOLMCTS.SingleTreeNode;
import controllers.singlePlayer.ea.Agent;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evodef.DefaultMutator;
import ga.SimpleRMHC;
import ggi.agents.SimpleEvoAgent;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.Random;

public class AsteroidsTest {

    // Note: these GVGAISimpleTest classes contain similar code to each other
    // but are best thought of as experimenters notebooks, where
    // by commenting in or our lines it is easy to experiment with
    // various agents

    // set true or false to show game running
    static boolean visual = true;

    public static void main(String[] args) throws Exception {
        StatSummary ss = new StatSummary();

        int nTrials = 10;
        for (int i=0; i<nTrials; i++) {
            ss.add(runOnce());
            System.out.println();
            System.out.println("After trial: " + (i+1));
            System.out.println("Total game ticks:  " + AsteroidsLinkState.totalGameTicks);
            System.out.println("Total game copies: " + AsteroidsLinkState.totalGameCopies);
        }

        System.out.println(ss);
        // System.out.println(Agent.useShiftBuffer);
    }

    public static double runOnce()  throws Exception {
        // make an agent to test


        // AsteroidsLinkState.defaultStartLevel = 1;
        AsteroidsLinkState stateObs = new AsteroidsLinkState();

        System.out.println(stateObs.getGameScore());
        System.out.println(stateObs.copy().getGameScore());

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player;

//        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
//                new controllers.singlePlayer.sampleOLMCTS.Agent(stateObs, timer);

        controllers.singlePlayer.discountOLMCTS.Agent discountOlmcts =
                new controllers.singlePlayer.discountOLMCTS.Agent(stateObs, timer);

//        controllers.singlePlayer.nestedMC.Agent nestedMC =
//                new controllers.singlePlayer.nestedMC.Agent(stateObs, timer);


        int depth = 100;

        int ticks = 2000;
        controllers.singlePlayer.discountOLMCTS.SingleTreeNode.DEFAULT_ROLLOUT_DEPTH = depth;
        SingleTreeNode.scoreDiscountFactor = 0.999;
        SingleTreeNode.useScoreDiscount = true;
        controllers.singlePlayer.discountOLMCTS.SingleTreeNode.DEFAULT_ROLLOUT_DEPTH = depth;
        controllers.singlePlayer.discountOLMCTS.Agent.MCTS_ITERATIONS = ticks / depth;



        // player = olmcts;

        player = discountOlmcts;

        // player = new SimpleEvoAgent();

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 20;
        double kExplore = 10;
        int nNeighbours = 100;

        // evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        // evoAlg = new SlidingMeanEDA();

        // DefaultMutator.totalRandomChaosMutation = true;

        Agent.useShiftBuffer = true;

        controllers.singlePlayer.ea.Agent.SEQUENCE_LENGTH = ticks / nEvals;
        player = new controllers.singlePlayer.ea.Agent(stateObs, timer, evoAlg, nEvals);
        // player = new controllers.singlePlayer.ea.Agent(stateObs, timer);


//        nestedMC.maxRolloutLength = 5;
//        nestedMC.nestDepth = 5;

        // player = nestedMC;

        int thinkingTime = 10; // in milliseconds
        int delay = 20;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();

        // this is how many steps we'll take in the actual game ...
        int nSteps = 1000;

        ElapsedTimer t = new ElapsedTimer();

        View view = new View(stateObs.state);
        // set CaveView to null to run fast with no visuals
        view = null;

        JEasyFrame frame;
        if (view != null) {
            frame = new JEasyFrame(view, "Asteroids");

        }

        System.out.println("Testing: " + player);
        for (int i=0; i<nSteps && !stateObs.isGameOver(); i++) {
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);


            Types.ACTIONS action = player.act(stateObs.copy(), timer);
            // System.out.println("Selected: " + action); //  + "\t " + action.ordinal());
            stateObs.advance(action);
            // System.out.println(stateObs.getGameScore());
            if (view!= null) {
                view.repaint();
                Thread.sleep(delay);
            }

        }

        System.out.println(stateObs.getGameScore());
        System.out.println(stateObs.isGameOver());

        System.out.println(t);

        System.out.println("Agent of type: " + player.getClass().getSimpleName());
        return stateObs.getGameScore();
    }
}
