package planetwar;

import evodef.EvoAlg;
import ga.SimpleRMHC;
import ntuple.SlidingMeanEDA;
import utilities.JEasyFrame;

import java.util.Arrays;
import java.util.Random;

public class TwoPlayerTest {

    public static void main(String[] args) throws Exception {
        GameState game = new GameState().setNPlanets(10).setRandomOwnerships().setRandomGrowthRates();

        PlanetWarView view = new PlanetWarView(game);
        JEasyFrame frame = new JEasyFrame(view, "Test View");
        KeyController controller = new KeyController();
        // frame.addKeyListener(controller);

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 200;
        int seqLength = 20;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(20);
        System.out.println("Initial score: " + game.getScore());
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);

        Random random = new Random();
        for (int i=0; i<1000; i++) {
            int p1, p2;
            p1 = random.nextInt(GameState.nActions);

            p1 = evoAgent.getAction(game, 0);

            p1 = random.nextInt(GameState.nActions);

            // p2 = controller.action(game);

            p2 = GameState.doNothing;
            p2 = random.nextInt(GameState.nActions);
            p2 = evoAgent.getAction(game, 1);
            int[] a = new int[]{p1, p2};
            game.next(a);
            // game.update();
            view.update(game);
            Thread.sleep(100);
        }
    }

}
