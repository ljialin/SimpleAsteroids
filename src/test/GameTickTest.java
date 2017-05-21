package test;

import battle.SimpleBattleState;
import core.game.StateObservationMulti;
import gvglink.SpaceBattleLinkState;
import gvglink.SpaceBattleLinkStateTwoPlayer;
import ontology.Types;
import utilities.ElapsedTimer;

import java.util.Random;

/**
 * Created by Simon Lucas on 21/05/2017.
 *
 *  On my desktop it can make around 4 million game ticks per second when using the SimpleBattleState
 *
 *  and around 3 million game ticks per second when using the SpaceBattleLinkStateTwoPlayer GVGAI Wrapper class
 */

public class GameTickTest {

    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();

        int gameTickMillis = 40;
        int nTicks = 500;
        int nTrials = 10000;
        Random random = new Random();


        for (int i = 0; i < nTrials; i++) {

            StateObservationMulti game = new SpaceBattleLinkStateTwoPlayer();

            // SimpleBattleState game = new SimpleBattleState();

            controllers.multiPlayer.sampleRandom.Agent randomAgent = new controllers.multiPlayer.sampleRandom.Agent(game, null, 0);

            // do not need separate agents for player one and plater 2
            // since actions are simply random




            for (int j=0; j<nTicks; j++) {
                Types.ACTIONS a1 = randomAgent.act(game, null);
                Types.ACTIONS a2 = randomAgent.act(game, null);

                Types.ACTIONS[] actions = new Types.ACTIONS[]{a1, a2};
                game.advance(actions);

//                 int[] actions = new int[]{random.nextInt(game.nActions()), random.nextInt(game.nActions())};

   //              game.next(actions);

            }
            // System.out.println(i + "\t " + game.getGameTick());

        }

        int totalTicks = nTicks * nTrials;
        System.out.format("Made %d ticks\n", totalTicks);
        System.out.format("Ticks per milli-second: %.1f\n\n", totalTicks / (double) timer.elapsed());
//        System.out.format("Game tick time = %d ms\n", (int) gameTick);
        System.out.format("Updates per game tick:   %d\n\n", (int) (gameTickMillis * totalTicks / (double) timer.elapsed()));
        System.out.println(timer);

    }
}
