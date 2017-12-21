package gvglink;

import altgame.SimpleMaxGame;
import asteroids.View;
import controllers.singlePlayer.ea.Agent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.Random;

public class AsteroidsTest {

    // Note: these Test classes contain similar code to each other
    // but are best thought of as experimenters notebooks, where
    // by commenting in or our lines it is easy to experiment with
    // various agents

    // set true or false to show game running
    static boolean visual = true;

    public static void main(String[] args) throws Exception {
        StatSummary ss = new StatSummary();

        int nTrials = 3;
        for (int i=0; i<nTrials; i++) {
            ss.add(runOnce());
            System.out.println();
            System.out.println("After trial: " + (i+1));
            System.out.println("Total game ticks:  " + AsteroidsLinkState.totalGameTicks);
            System.out.println("Total game copies: " + AsteroidsLinkState.totalGameCopies);
        }

        System.out.println(ss);

    }

    public static double runOnce()  throws Exception {
        // make an agent to test


        AsteroidsLinkState stateObs = new AsteroidsLinkState();

        System.out.println(stateObs.getGameScore());
        System.out.println(stateObs.copy().getGameScore());

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player;

        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
                new controllers.singlePlayer.sampleOLMCTS.Agent(stateObs, timer);

        controllers.singlePlayer.discountOLMCTS.Agent discountOlmcts =
                new controllers.singlePlayer.discountOLMCTS.Agent(stateObs, timer);

        controllers.singlePlayer.nestedMC.Agent nestedMC =
                new controllers.singlePlayer.nestedMC.Agent(stateObs, timer);



        player = olmcts;

        // player = discountOlmcts;

        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 20;
        double kExplore = 10;
        int nNeighbours = 100;

        // evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        // Mutator.totalRandomChaosMutation = true;

        Agent.useShiftBuffer = false;

        controllers.singlePlayer.ea.Agent.SEQUENCE_LENGTH = 200;
        // player = new controllers.singlePlayer.ea.Agent(stateObs, timer, evoAlg, nEvals);
        player = new controllers.singlePlayer.ea.Agent(stateObs, timer);


        nestedMC.maxRolloutLength = 5;
        nestedMC.nestDepth = 5;

        // player = nestedMC;

        int thinkingTime = 10; // in milliseconds
        int delay = 20;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();

        // this is how many steps we'll take in the actual game ...
        int nSteps = 500;

        ElapsedTimer t = new ElapsedTimer();

        View view = new View(stateObs.state);
        // set view to null to run fast with no visuals
        // view = null;

        JEasyFrame frame;
        if (view != null) {
            frame = new JEasyFrame(view, "Asteroids");

        }

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

        return stateObs.getGameScore();
    }
}
