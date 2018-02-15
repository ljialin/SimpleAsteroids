package asteroids;

import evodef.EvoAlg;
import evogame.GameParameters;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgent;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;

import static asteroids.Constants.*;

public class Game {

    View view;
    AsteroidsGameState gameState;
    JEasyFrame frame;
    public Controller controller;

    // weird error at the moment
    // when copyTest is set to true
    // the ship can be controlled but it won't fire
    // missiles!!!

    // it is as though they are added to the wrong list...

    static boolean copyTest = false;

    public static void main(String[] args) {
        System.out.println(font);
        boolean visible = true;
        int nTicks = 20000;
        int startLevel = 4;
        int nLives = 5;
        GameParameters params = new GameParameters();
        // AsteroidsGameState gameState = new AsteroidsGameState(params, startLevel, nLives);
        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params).initForwardModel();
        Game game = new Game(gameState, visible);

        ElapsedTimer t = new ElapsedTimer();

        System.out.println("Level = " + gameState.forwardModel.level);

        game.run(nTicks);
        System.out.println(t);

//        if (visible) {
//            gameState.run(nTicks);
//        } else {
//            ElapsedTimer t = new ElapsedTimer();
//            gameState.runHeadless(nTicks);
//            System.out.println(t);
//        }

    }

    public Game(boolean visible) {

        this(new AsteroidsGameState(), visible);
    }

    public Game(AsteroidsGameState gameState, boolean visible) {
        this.gameState = gameState;
        // gameState.setParams(new GameParameters());
        gameState.initForwardModel();
        if (visible) {
            view = new View(gameState.copy());
            frame = new JEasyFrame(view, "Simple Asteroids");
        }
        if (visible) {
            controller = getEvoAgent(); // new KeyController();
            // controller = new KeyController();
        } else {
            controller = new RotateAndShoot();
            controller = getEvoAgent();
        }
        // controller = new RotateAndShoot();
        if (controller instanceof KeyController) {
            frame.addKeyListener((KeyController) controller);
        }
    }

    Controller getEvoAgent() {
        //
        // todo Add in the code t make this
        int nResamples = 1;


        Mutator mutator = new Mutator(null);

        // setting to true may give best performance
        mutator.totalRandomChaosMutation = true;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;



        // evoAlg = new SlidingMeanEDA();

        int nEvals = 30;
        int seqLength = 150;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setUseShiftBuffer(true);
        evoAgent.setVisual();

        return new EvoAgentAdapter().setAgent(evoAgent);
    }

    public void run(int maxTicks) {
        int i = 0;
        while (gameState.gameOn() && ++i < maxTicks) {

            Action action = controller.action(gameState);
            gameState.update(action);
            if (copyTest) {
                gameState = gameState.copy();
            }

            // System.out.println("3: " + gameState.ship.action);
            // System.out.println();

            // update the view to show this one
            if (view != null) {
                // in case the gameState has been copied
                view.gameState = gameState.copy();
                setPlayouts();
                // System.out.println(gameState.ship.s);
                view.repaint();
                sleep();
            }
        }
        System.out.println("Ran for " + i + " gameState ticks");
        System.out.println("Score = " + gameState.getScore());
    }

    static boolean showPlayouts = true;

    public void setPlayouts() {
        if (showPlayouts && controller instanceof EvoAgentAdapter) {
            EvoAgentAdapter adapter = (EvoAgentAdapter) controller;
            EvoAgent evoAgent = (EvoAgent) adapter.agent;
            view.playouts = evoAgent.evoAlg.getLogger().solutions;
        }
    }

    public void sleep() {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
