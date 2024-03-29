package spinbattle.inference;

import agents.dummy.DoNothingAgent;
import agents.evo.EvoAgent;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.agents.SimpleEvoAgent;
import ggi.core.SimplePlayerInterface;
import logger.sample.DefaultLogger;
import spinbattle.actuator.SourceTargetActuator;
import spinbattle.core.FalseModelAdapter;
import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.TunablePriorityLauncher;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.Random;

public class ValueInferenceVisualTest {

    public static void main(String[] args) throws Exception {
        // to always get the same initial game
        long seed = new Random().nextLong();
        // seed = -6330548296303013003L;
        seed = 10;
        System.out.println("Setting seed to: " + seed);
        SpinBattleParams.random = new Random(seed);
        // SpinBattleParams.random = new Random();

        SpinBattleParams params = new SpinBattleParams();
        // params.transitSpeed *= 2;
        params.gravitationalFieldConstant *= 1.0;

        params.maxTicks = 5000;
        params.width = 700;
        params.height = 400;
        params.nPlanets = 8;
        // params.height = 700;

        // SpinBattleParams altParams = params.copy();

        // params.gravitationalFieldConstant *= 1.0;
        params.transitSpeed *= 1;
        // params.maxInitialShips *= 3;

        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        System.out.println(gameState.planets);

        Planet test = gameState.planets.get(5);
        test.ownedBy = 1;
        test.shipCount = 100;

        // BasicLogger basicLogger = new BasicLogger();
        DefaultLogger logger = new DefaultLogger();
        // gameState.setLogger(logger);

        // SpinGameState copy1 = ((SpinGameState) gameState.copy()).setParams(altParams);

        // System.out.println("Logger in copied state: " + copy1.logger);

        // set up the actuator
        gameState.actuators[0] = new SourceTargetActuator().setPlayerId(0);

        gameState.actuators[1] = new SourceTargetActuator().setPlayerId(1);

        SimplePlayerInterface evoAgent = getEvoAgent();

        SimpleEvoAgent player2 = new SimpleEvoAgent();
        player2.sequenceLength = 200;
        player2.nEvals = 10;


        SimplePlayerInterface randomPlayer = new agents.dummy.RandomAgent();
        // evoAgent = randomPlayer;

        // but now we also need to establish a player

        SpinBattleView view = new SpinBattleView().setParams(params).setGameState(gameState);
        String title = "Spin Battle Game" ;
        JEasyFrame frame = new JEasyFrame(view, title + ": Waiting for Graphics");
        frame.setLocation(new Point(800, 0));
//        MouseSlingController mouseSlingController = new MouseSlingController();
//        mouseSlingController.setGameState(gameState).setPlayerId(Constants.playerOne);
//        CaveView.addMouseListener(mouseSlingController);
        waitUntilReady(view);
        int[] actions = new int[2];
        int frameDelay = 400;

        // may want to stop before the end of the game for demo purposes
        int nTicks = 5000;
        for (int i=0; i<nTicks && !gameState.isTerminal(); i++) {
            actions[0] = evoAgent.getAction(gameState.copy(), 0);
            actions[1] = player2.getAction(gameState.copy(), 1);
            // actions[1] = randomPlayer.getAction(gameState.copy(), 1);
            gameState.next(actions);
            SpinGameState viewCopy = (SpinGameState) gameState.copy();
            viewCopy.logger = gameState.logger;
            view.setGameState(viewCopy);
            view.repaint();
            frame.setTitle(title + " : " + i); //  + " : " + CaveView.getTitle());
            Thread.sleep(frameDelay);
        }
        System.out.println(gameState.isTerminal());
        String trajTitle = String.format("g = %.3f, spd = %.3f", params.gravitationalFieldConstant, params.transitSpeed);
        // logger.showTrajectories(params.width, params.height, trajTitle);
        // System.out.println("nTraj: " + logger.getTrajectoryLogger().trajectories.size());

    }

    static void waitUntilReady(SpinBattleView view) throws Exception {
        int i = 0;
        while (view.nPaints == 0) {
            // System.out.println(i++ + " : " + CaveView.nPaints);
            Thread.sleep(50);
        }
    }

    static boolean useSimpleEvoAgent = false;

    static SimplePlayerInterface getEvoAgent() {

        if (useSimpleEvoAgent) {
            return new SimpleEvoAgent().setOpponent(new DoNothingAgent());
        }
        //
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        // mutator.totalRandomChaosMutation = true;
        mutator.pointProb = 10;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        // evoAlg = new SlidingMeanEDA();
        // evoAlg = new SimpleGA();

        int nEvals = 20;
        int seqLength = 100;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        boolean useShiftBuffer = true;
        evoAgent.setUseShiftBuffer(useShiftBuffer);
        evoAgent.setVisual();

        return evoAgent;
    }
}
