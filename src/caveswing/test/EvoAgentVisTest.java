package caveswing.test;

import agents.evo.EvoAgent;
import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.core.SimplePlayerInterface;
import utilities.JEasyFrame;

import java.awt.*;

public class EvoAgentVisTest {

    public static void main(String[] args) throws Exception {
        SimplePlayerInterface player = getEvoAgent();

        CaveSwingParams params = new CaveSwingParams();
        params.maxTicks = 2000;
        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        String title = "Evo Agent Visual Test";
        JEasyFrame frame = new JEasyFrame(view, title);
        frame.setLocation(0, 450);
        ViewUtil.waitUntilReady(view);

        while (!gameState.isTerminal()) {
            // get the action from the player, update the game state, and show a view
            int action = player.getAction(gameState.copy(), 0);
            // recall the action array is needed for generality for n-player games
            int[] actions = new int[]{action};
            gameState.next(actions);
            CaveGameState viewState = (CaveGameState) gameState.copy();
            view.setGameState(viewState).repaint();
            frame.setTitle(title + " : " + gameState.nTicks + " : " +gameState.isTerminal());
            Thread.sleep(40);
        }

    }

    static SimplePlayerInterface getEvoAgent() {
        //
        // todo Add in the code to make this
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        // mutator.totalRandomChaosMutation = true;
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 5;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        // evoAlg = new SlidingMeanEDA();

        int nEvals = 100;
        int seqLength = 500;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setDimension(new Dimension(800, 400));
        evoAgent.setUseShiftBuffer(true);
        // evoAgent.setVisual();

        return evoAgent;
    }
}
