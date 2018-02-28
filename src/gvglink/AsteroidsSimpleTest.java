package gvglink;

import asteroids.View;
import controllers.singlePlayer.discountOLMCTS.SingleTreeNode;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

public class AsteroidsSimpleTest {

    // Note: these Test classes contain similar code to each other
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
    }

    public static double runOnce()  throws Exception {

        AsteroidsLinkState stateObs = new AsteroidsLinkState();
        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractPlayer player =
                new controllers.singlePlayer.discountOLMCTS.Agent(stateObs, timer);

        int depth = 100;
        int ticks = 2000;
        SingleTreeNode.DEFAULT_ROLLOUT_DEPTH = depth;
        SingleTreeNode.scoreDiscountFactor = 0.999;
        SingleTreeNode.useScoreDiscount = true;
        SingleTreeNode.DEFAULT_ROLLOUT_DEPTH = depth;
        controllers.singlePlayer.discountOLMCTS.Agent.MCTS_ITERATIONS = ticks / depth;

        int thinkingTime = 10; // in milliseconds
        int delay = 20;

        int nSteps = 1000;

        ElapsedTimer t = new ElapsedTimer();

        View view = new View(stateObs.state);
        // set view to null to run fast with no visuals
        view = null;

        JEasyFrame frame;
        if (view != null) {
            frame = new JEasyFrame(view, "Asteroids");

        }

        for (int i=0; i<nSteps && !stateObs.isGameOver(); i++) {
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);
            Types.ACTIONS action = player.act(stateObs.copy(), timer);
            stateObs.advance(action);
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
