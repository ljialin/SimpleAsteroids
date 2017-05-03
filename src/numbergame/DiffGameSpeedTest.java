package numbergame;

import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;

/**
 * Created by simonmarklucas on 12/03/2017.
 */

public class DiffGameSpeedTest {
    public static void main(String[] args) {

        DiffGame dg = new DiffGame();
        ElapsedTimer t = new ElapsedTimer();

        Types.ACTIONS[] actions = new Types.ACTIONS[2];
        int nTicks = (int) 3e7;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        AbstractMultiPlayer player1, player2;

        player1 = new controllers.multiPlayer.sampleRandom.Agent(dg, timer, 0);
        player2 = new controllers.multiPlayer.sampleRandom.Agent(dg, timer, 1);

        int updateTick = 100;

        for (int i=0; i<nTicks; i++) {

            // update the chosen actions every so often
            if (i % updateTick == 0) {
                actions[0] = player1.act(dg, timer);
                actions[1] = player2.act(dg, timer);
            }
            // advance anyway
            dg.advance(actions);
        }

        System.out.println(dg.getGameScore());
        System.out.println(t);


    }
}
