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
        // JEasyFrame frame = new JEasyFrame(view, "Test View");
        KeyController controller = new KeyController();
        // frame.addKeyListener(controller);

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 20000;
        int seqLength = 20;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(20);
        System.out.println("Initial score: " + game.getScore());
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);

        int[] seq = evoAgent.getActions(game.copy(), 0);
        System.out.println(evoAgent.simpleGameAdapter.logger().ss);
        System.out.println("Returned sequence: " + Arrays.toString(seq));

        // now check the fitness
        int nTrials = 5;
        for (int i=0; i<nTrials; i++) {
            // GameState tmp = game.copy();
            // evoAgent.actionSequencer.setGameState(game.copy());
//            double score = evoAgent.actionSequencer.actVersusDoNothing(seq,0).getGameState().getScore();
//            System.out.println("Score: " + score);
            System.out.println("Check: " + evoAgent.actionSequencer.fitness(seq));
        }

        visiPlay(game, seq);


        System.exit(0);
        // now play
        Random random = new Random();
        for (int i=0; i<1000; i++) {
            int p1, p2;
            p1 = random.nextInt(GameState.nActions);

            p1 = evoAgent.getAction(game, 0);

            // p2 = controller.action(game);

            p2 = GameState.doNothing; // random.nextInt(GameState.nActions);
            int[] a = new int[]{p1, p2};
            game.next(a);
            // game.update();
            view.update(game);
            Thread.sleep(100);
        }
    }

    public static void visiPlay(GameState game, int[] seq) throws Exception {
        PlanetWarView view = new PlanetWarView(game);
        JEasyFrame frame = new JEasyFrame(view, "Sequence View: " + Arrays.toString(seq));
        for (int x : seq) {
            int[] a = new int[]{x, GameState.doNothing};
            game.next(a);
            view.update(game);
            Thread.sleep(1000);
        }

    }
}
