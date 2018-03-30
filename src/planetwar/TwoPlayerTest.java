package planetwar;

import agents.evo.EvoAgent;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapterMulti;
import ga.SimpleRMHC;
import utilities.JEasyFrame;

import java.util.Random;

public class TwoPlayerTest {

    public static void main(String[] args) throws Exception {
        // AsteroidsGameState game = new AsteroidsGameState().setNPlanets(10).setRandomOwnerships().setRandomGrowthRates();
        GameState game = new GameState().defaultState(); // .setNPlanets(10).setDOwnerships().setRandomGrowthRates();

        GameState.includeBuffersInScore = true;
        GameState.wrapAround = false;

        PlanetWarView view = new PlanetWarView(game);
        JEasyFrame frame = new JEasyFrame(view, "Test View");
        KeyController controller = new KeyController();
        frame.addKeyListener(controller);

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 100;
        int seqLength = 20;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(20);
        System.out.println("Initial score: " + game.getScore());
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setUseShiftBuffer(true);
        evoAgent.setVisual();

        int delay = 2000;
        GameActionSpaceAdapterMulti.visual = true;
        Random random = new Random();
        for (int i=0; i<500; i++) {
            int p1, p2;

            p1 = evoAgent.getAction(game, 0);

            // p1 = random.nextInt(AsteroidsGameState.nActions);

            p2 = controller.action(game);

            // p2 = AsteroidsGameState.doNothing;
            int[] a = new int[]{p1, p2};
            game.next(a);
            // game.update();
            view.update(game);
            Thread.sleep(delay);
        }
    }

}


