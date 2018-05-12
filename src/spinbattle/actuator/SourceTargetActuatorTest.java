package spinbattle.actuator;

import agents.evo.EvoAgent;
import asteroids.Controller;
import asteroids.EvoAgentAdapter;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.SimplePlayerInterface;
import spinbattle.core.SpinGameState;
import spinbattle.log.BasicLogger;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.HeuristicLauncher;
import spinbattle.ui.MouseSlingController;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.Random;

public class SourceTargetActuatorTest {

    public static void main(String[] args) throws Exception {
        // to always get the same initial game
        SpinBattleParams.random = new Random(6);
        SpinBattleParams params = new SpinBattleParams();
        // params.transitSpeed *= 2;
        params.gravitationalFieldConstant *= 1;


        params.maxTicks = 5000;
        params.width = 400;
        params.height = 700;



        SpinBattleParams altParams = params.copy();

        params.gravitationalFieldConstant *= 2;
        params.transitSpeed *= 2;

        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();

        BasicLogger basicLogger = new BasicLogger();
        // gameState.setLogger(basicLogger);

        // set up the actuator
        gameState.actuators[0] = new SourceTargetActuator().setPlayerId(0);

        // gameState.actuators[1] = new SourceTargetActuator().setPlayerId(1);

        SimplePlayerInterface player1 = getEvoAgent();

        // SimplePlayerInterface player2 = getEvoAgent();

        // SimplePlayerInterface randomPlayer = new agents.dummy.RandomAgent();

        // but now we also need to establish a player

        SpinBattleView view = new SpinBattleView().setParams(params).setGameState(gameState);
        HeuristicLauncher launcher = new HeuristicLauncher();
        String title = "Spin Battle Game" ;
        JEasyFrame frame = new JEasyFrame(view, title + ": Waiting for Graphics");
        frame.setLocation(new Point(800, 0));
        MouseSlingController mouseSlingController = new MouseSlingController();
        mouseSlingController.setGameState(gameState).setPlayerId(Constants.playerOne);
        view.addMouseListener(mouseSlingController);
        int launchPeriod = 10; // params.releasePeriod;
        waitUntilReady(view);
        int[] actions = new int[2];

        for (int i=0; !gameState.isTerminal(); i++) {
            SpinGameState copy = ((SpinGameState) gameState.copy()).setParams(altParams);
            actions[0] = player1.getAction(gameState.copy(), 0);
            // actions[1] = player2.getAction(gameState.copy(), 1);
            // actions[0] = randomPlayer.getAction(gameState.copy(), 0);
            // actions[1] = randomPlayer.getAction(gameState.copy(), 1);
            // System.out.println(i + "\t " + actions[0]);
            gameState.next(actions);
            mouseSlingController.update();
            launcher.makeTransits(gameState, Constants.playerOne);
            if (i % launchPeriod == 0)
                launcher.makeTransits(gameState, Constants.playerTwo);
            view.setGameState((SpinGameState) gameState.copy());
            view.repaint();
            frame.setTitle(title + " : " + i); //  + " : " + view.getTitle());
            Thread.sleep(20);
        }
        System.out.println(gameState.isTerminal());
    }

    static void waitUntilReady(SpinBattleView view) throws Exception {
        int i = 0;
        while (view.nPaints == 0) {
            // System.out.println(i++ + " : " + view.nPaints);
            Thread.sleep(50);
        }
    }

    static SimplePlayerInterface getEvoAgent() {
        //
        // todo Add in the code t make this
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        mutator.totalRandomChaosMutation = true;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        // evoAlg = new SlidingMeanEDA();

        int nEvals = 20;
        int seqLength = 200;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        boolean useShiftBuffer = true;
        evoAgent.setUseShiftBuffer(useShiftBuffer);
        evoAgent.setVisual();

        return evoAgent;
    }

}
