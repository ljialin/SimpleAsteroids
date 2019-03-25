package caveswing.test;

import agents.evo.EvoAgent;
import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.design.CaveSwingFitnessSpace;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import ggi.agents.EvoAgentFactory;
import ggi.agents.SimpleEvoAgent;
import ggi.core.SimplePlayerInterface;
import ntuple.SlidingMeanEDA;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;

public class EvoAgentVisTest {

    static boolean showEvolution = true;

    static boolean useFalseModel = false;

    static int frameDelay = 50;

    public static void main(String[] args) throws Exception {
        int nEvals = 20;
        int seqLength = 100;
        boolean useShiftBuffer = true;
        EvoAgent player = getEvoAgentFromFactory(nEvals, seqLength, useShiftBuffer);

        // player = new SimpleEvoAgent();

        CaveSwingParams params = CoolTestParams.getParams();
        // params.nAnchors *=2;

        params.maxTicks = 500;
        params.gravity.y = 0.5;
        params.hooke = 0.01;
        params.gravity.x = -0.00;
        // params.width = 1500;
        // params.height *= 1.5;

        params.width = 2500;
        params.height *= 1.5;
        // params.nAnchors /= 2;
        params.meanAnchorHeight *= 2;


        // plug in here for evolved points or other selected points in the search space

        int[] point = {0, 6, 3, 3, 3, 3, 3, 3};
        // params = new CaveSwingFitnessSpace().getParams(point);

        point = new int[]{0, 4, 4, 0, 4, 0, 0, 3};

        CaveSwingParams falseParams = params.copy();

        falseParams.hooke *= 0.2;

        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        view.scrollView = true;
        view.scrollWidth = 800;

        String title = "Evo Agent Visual GVGAISimpleTest";
        JEasyFrame frame = new JEasyFrame(view, title);
        if (showEvolution) frame.setLocation(0, 350);
        ViewUtil.waitUntilReady(view);

        StatSummary actionTimes = new StatSummary("Decision time stats");
        // gameState.setSoundEnabled(true);
        while (!gameState.isTerminal()) {
            // get the action from the player, update the game state, and show a view


            CaveGameState falseState = (CaveGameState) gameState.copy();
            falseState.setParams(falseParams);

            ElapsedTimer t = new ElapsedTimer();
            int action = useFalseModel ?
                    player.getAction(falseState, 0) :
                    player.getAction(gameState.copy(), 0);
            // recall the action array is needed for generality for n-player games
            actionTimes.add(t.elapsed());

            int[] actions = new int[]{action};
            gameState.next(actions);

            CaveGameState viewState = ((CaveGameState) gameState.copy());
            if (useFalseModel) viewState.setParams(falseParams);

            view.playouts = player.evoAlg.getLogger().solutions;
            view.setGameState(viewState).repaint();
            frame.setTitle(title + " : " + gameState.nTicks + " : " + gameState.isTerminal() + " : " + (int) gameState.getScore());
            Thread.sleep(frameDelay);
        }
        System.out.println(actionTimes);
        System.out.println((int) gameState.getScore());
    }

    public static EvoAgent getEvoAgentFromFactory(int nEvals, int seqLength, boolean useShiftBuffer) {

        EvoAgentFactory factory = new EvoAgentFactory();
        factory.nEvals = nEvals;
        factory.seqLength = seqLength;
        factory.useShiftBuffer = useShiftBuffer;

        EvoAgent evoAgent = factory.getAgent();
        evoAgent.setDimension(new Dimension(800, 300));
        // set shift buffer below
        evoAgent.setUseShiftBuffer(true);
        if (showEvolution)
            evoAgent.setVisual();
        return evoAgent;
    }

    public static EvoAgent getEvoAgent() {
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
        // evoAlg = new SimpleGA();
        int nEvals = 20;
        int seqLength = 100;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setDimension(new Dimension(800, 300));
        // set shift buffer below
        evoAgent.setUseShiftBuffer(true);
        if (showEvolution)
            evoAgent.setVisual();
        return evoAgent;
    }

}
