package caveswing.test;

import agents.evo.EvoAgent;
import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import ggi.core.SimplePlayerInterface;
import ntuple.SlidingMeanEDA;
import utilities.JEasyFrame;

import java.awt.*;

public class EvoAgentVisTest {

    static boolean showEvolution = true;

    static boolean useFalseModel = false;

    public static void main(String[] args) throws Exception {
        EvoAgent player = getEvoAgent();

        CaveSwingParams params = CoolTestParams.getParams();
        params.nAnchors *=2;

        CaveSwingParams falseParams = params.copy();
        falseParams.hooke *= 2.1;

        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        view.scrollView = true;
        view.scrollWidth = 800;

        String title = "Evo Agent Visual Test";
        JEasyFrame frame = new JEasyFrame(view, title);
        if (showEvolution) frame.setLocation(0, 350);
        ViewUtil.waitUntilReady(view);

        while (!gameState.isTerminal()) {
            // get the action from the player, update the game state, and show a view


            CaveGameState falseState = (CaveGameState) gameState.copy();
            falseState.setParams(falseParams);

            int action = useFalseModel ?
                    player.getAction(falseState, 0) :
                    player.getAction(gameState.copy(), 0);
            // recall the action array is needed for generality for n-player games
            int[] actions = new int[]{action};
            gameState.next(actions);

            CaveGameState viewState = ((CaveGameState) gameState.copy());
            if (useFalseModel) viewState.setParams(falseParams);
            view.playouts = player.evoAlg.getLogger().solutions;
            view.setGameState(viewState).repaint();
            frame.setTitle(title + " : " + gameState.nTicks + " : " + gameState.isTerminal());
            Thread.sleep(50);
        }
    }

    public static EvoAgent getEvoAgent() {
        int nResamples = 1;
        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        mutator.totalRandomChaosMutation = true;
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 1;
        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);
        EvoAlg evoAlg = simpleRMHC;
        // evoAlg = new SlidingMeanEDA();
        // evoAlg = new SimpleGA();
        int nEvals = 20;
        int seqLength = 100;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setDimension(new Dimension(800, 300));
        // set shift buffer below
        evoAgent.setUseShiftBuffer(false);
        if (showEvolution)
            evoAgent.setVisual();
        return evoAgent;
    }

}
