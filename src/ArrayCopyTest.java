import battle.SimpleBattleState;
import utilities.ElapsedTimer;

/**
 * Created by simonmarklucas on 11/06/2017.
 *
 *  This is to test the speed of Array Copying -
 *  in case for speed reasons it might be worth storing
 *  the entire game state in an array of double, for example
 *
 *  Can make nearly 1 million copies per second of a 1000 size array.
 *
 */
public class ArrayCopyTest {
    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();
        double gameTick = 40;

        int nCopies = (int) 1e7;

        // size of array
        int n = 1000;

        double[] game = new double[n];
        double tot = 0;
        for (int i=0; i<n; i++) {
            game[i] = Math.random();
            tot += game[i];
        }
        System.out.println("Tot = " + tot);

        for (int i=0; i<nCopies; i++) {

            game = arrayCopy(game);

        }

        System.out.println("n = " + n);
        System.out.format("Made %d copies\n", nCopies);

        System.out.format("Copies per milli-second: %.1f\n\n", nCopies / (double) timer.elapsed());
        System.out.format("Game tick time = %d ms\n",(int) gameTick);
        System.out.format("Copies per game tick: %d\n\n", (int) (gameTick * nCopies / (double) timer.elapsed()));

        System.out.println(timer);

        tot = 0;
        for (double x : game) tot += x;
        System.out.println("Total check" + tot);

    }

    public static double[] arrayCopy(double[] a) {
        double[] x = new double[a.length];
        for (int i=0; i<a.length; i++) {
            x[i] = a[i];
        }
        return x;
    }

}
