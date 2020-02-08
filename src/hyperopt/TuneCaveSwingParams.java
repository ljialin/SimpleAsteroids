package hyperopt;

import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.design.CaveSwingFitnessSpace;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.AnnotatedFitnessSpace;
// import ntuple.NTupleBanditEA;
import evodef.SearchSpaceUtil;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;


public class TuneCaveSwingParams {
    public static void main(String[] args) throws Exception {
        int nEvals = 200;
        System.out.println("Optimization budget: " + nEvals);
        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(5000);

        ntbea.logBestYet = true;
        NTupleSystem model = new NTupleSystem();
        // set up a non-standard tuple pattern
        model.use1Tuple = true;
        model.use2Tuple = true;
        model.useNTuple = true;

        ntbea.setModel(model);

        int nChecks = 50;
        int nTrials = 1;

        ElapsedTimer timer = new ElapsedTimer();

        HyperParamTuneRunner runner = new HyperParamTuneRunner();
        runner.verbose = true;
//            runner.setLineChart(lineChart);
        runner.nChecks = nChecks;
        runner.nTrials = nTrials;
        runner.nEvals = nEvals;

        // this allows plotting of the independently assessed fitness of
        // the algorithm's best guesses during each run
        // set to zero for fastest performance, set to 5 or 10 to learn
        // more about the convergence of the algorithm
        runner.plotChecks = 0;
        AnnotatedFitnessSpace caveSwingSpace = new CaveSwingFitnessSpace();
        // uncomment to run the skilful one
        // caveSwingSpace = new CaveSwingGameSkillSpace();
        System.out.println("Testing: " + ntbea);

        runner.runTrials(ntbea, caveSwingSpace);

        System.out.println("Finished testing: " + ntbea);
        System.out.println("Time for all experiments: " + timer);

        // running the first solution

        runGame(runner.solutions.get(0));

    }

    static void runGame(int[] solution) throws Exception {

        CaveSwingParams params = getSearchSpaceParams(solution);

        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        // gameState.setSoundEnabled(true);
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        view.scrollView = true;
        view.scrollWidth = 1000;

        // gameState.playRelease();

        String title = "Key Player GVGAISimpleTest";

        JEasyFrame frame = new JEasyFrame(view, title);

        KeyController player = new KeyController();
        frame.addKeyListener(player);
        ViewUtil.waitUntilReady(view);

        // play forever
        while (true) {
            while (!gameState.isTerminal()) {
                // get the action from the player, update the game state, and show a view
                int action = player.getAction(gameState.copy(), 0);
                // recall the action array is needed for generality for n-player games
                int[] actions = new int[]{action};
                gameState.next(actions);
                CaveGameState viewState = (CaveGameState) gameState.copy();
                view.setGameState(viewState).repaint();
                frame.setTitle(title + " : " + gameState.nTicks + " : " + gameState.isTerminal());
                Thread.sleep(40);
            }
            System.out.println("Final score: " + (int) gameState.getScore());
            Thread.sleep(2000);
            gameState = new CaveGameState().setParams(params).setup();
        }
    }


    static CaveSwingParams getSearchSpaceParams(int[] point) {
        CaveSwingFitnessSpace fitnessSpace = new CaveSwingFitnessSpace();
        return fitnessSpace.getParams(point);
    }


}
