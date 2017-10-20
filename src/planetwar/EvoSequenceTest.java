package planetwar;

import evodef.EvoAlg;
import evodef.GameActionSpaceAdapterMulti;
import ga.SimpleRMHC;
import gvglink.PlanetWarsLinkState;
import plot.LineChart;
import utilities.JEasyFrame;

import java.util.Arrays;
import java.util.Random;

public class EvoSequenceTest {

    // simple example of NOT doing rolling horizon, but instead
    // of evolving a complete sequence

    public static void main(String[] args) throws Exception {
        GameState game = new GameState().defaultState();

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 2000;
        int seqLength = 30;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(20);
        System.out.println("Initial score: " + game.getScore());
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);

        int[] seq = evoAgent.getActions(game.copy(), 0);

        // System.out.println(evoAgent.simpleGameAdapter.logger().fa);

        // new JEasyFrame( LineChart.easyPlot(evoAgent.simpleGameAdapter.logger().fa), "Evo Plot");
        // visiPlay(game, seq);

        // check the sequence fitness

        System.out.println(evoAgent.simpleGameAdapter.logger().ss);
        System.out.println("Returned sequence: " + Arrays.toString(seq));

        for (int i=0; i<5; i++) {
            System.out.println(evoAgent.simpleGameAdapter.evaluate(seq));
        }
        System.out.println();
        System.out.println("Now using the other one");


        for (int i=0; i<5; i++) {
            PlanetWarsLinkState linkState = new PlanetWarsLinkState(game.copy());

            GameActionSpaceAdapterMulti adapter = new GameActionSpaceAdapterMulti(linkState, seqLength, 0, 1);
            System.out.println(adapter.evaluate(seq));

        }

        System.out.println();
        for (int i=0; i<5; i++) {
            System.out.println(evoAgent.simpleGameAdapter.evaluate(seq));
        }
        System.out.println();



    }

    public static void visiPlay(GameState game, int[] seq) throws Exception {
        PlanetWarView view = new PlanetWarView(game);
        JEasyFrame frame = new JEasyFrame(view, "Sequence View: " + Arrays.toString(seq));
        for (int x : seq) {
            int[] a = new int[]{x, GameState.doNothing};
            game.next(a);
            view.update(game);
            Thread.sleep(100);
        }
    }
}
