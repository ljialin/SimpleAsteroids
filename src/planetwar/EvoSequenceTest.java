package planetwar;

import evodef.EvoAlg;
import ga.SimpleRMHC;
import plot.LineChart;
import utilities.JEasyFrame;

import java.util.Arrays;
import java.util.Random;

public class EvoSequenceTest {

    // simple example of NOT doing rolling horizon, but instead
    // of evolving a complete sequence

    public static void main(String[] args) throws Exception {
        GameState game = new GameState().setNPlanets(10).setRandomOwnerships().setRandomGrowthRates();

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 2000;
        int seqLength = 20;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(20);
        System.out.println("Initial score: " + game.getScore());
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);

        int[] seq = evoAgent.getActions(game.copy(), 0);
        System.out.println(evoAgent.simpleGameAdapter.logger().ss);
        System.out.println("Returned sequence: " + Arrays.toString(seq));

        System.out.println(evoAgent.simpleGameAdapter.logger().fa);

        new JEasyFrame( LineChart.easyPlot(evoAgent.simpleGameAdapter.logger().fa), "Evo Plot");
        // visiPlay(game, seq);
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
