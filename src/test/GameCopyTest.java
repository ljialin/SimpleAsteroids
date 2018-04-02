package test;

import battle.SimpleBattleState;
import utilities.ElapsedTimer;

/**
 * Created by Simon Lucas on 21/05/2017.
 *
 *  On my deskptop can make 300,000 copies per second of the SimpleBattleState
 */
public class GameCopyTest {

    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();
        double gameTick = 40;

        int nCopies = (int) 1e7;

        // StateObservationMulti game = new SimpleBattleState();
        SimpleBattleState game = new SimpleBattleState();

        // AsteroidsGameState game = new AsteroidsGameState(3);

        for (int i=0; i<nCopies; i++) {

            game = game.copyState();
            // game = game.copy();
        }

        System.out.format("Made %d copies\n", nCopies);

        System.out.format("Copies per milli-second: %.1f\n\n", nCopies / (double) timer.elapsed());
        System.out.format("Game tick time = %d ms\n",(int) gameTick);
        System.out.format("Copies per game tick: %d\n\n", (int) (gameTick * nCopies / (double) timer.elapsed()));

        System.out.println(timer);

    }
}
