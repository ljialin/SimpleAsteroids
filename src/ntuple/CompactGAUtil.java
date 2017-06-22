package ntuple;

import java.util.Random;

/**
 * Created by Simon Lucas on 22/06/2017.
 *
 *  Pulled these utility functions out as they are used by both the Sliding History Window CompactGA, and also the
 *  more standard Compact GA.  I say 'more standard' as even that one has the multiple parent innovation
 */
public class CompactGAUtil {
    static Random random = new Random();

    public static void updatePVec(double[] pVec, int [] winner, int[] loser, double K) {
        for (int i=0; i<pVec.length; i++) {
            if (winner[i] != loser[i]) {
                if (winner[i] == 1) {
                    pVec[i] += 1/K;
                } else {
                    pVec[i] -= 1/K;
                }
            }
        }
    }

    // randomly dither the output to avoid weird effects such as
    // returning the optimal solution for OneMax after zero iterations!

    public static int[] argmax(double[] pVec) {
        int[] x = new int[pVec.length];
        for (int i=0; i<x.length; i++) {
            x[i] = pVec[i] + 1e-6*random.nextGaussian() > 0.5 ? 1 : 0;
        }
        return x;
    }

    public static int[] randBitVec(double[] pVec) {
        int[] x = new int[pVec.length];
        for (int i=0; i<x.length; i++) {
            x[i] = random.nextDouble() < pVec[i] ? 1 : 0;
        }
        return x;
    }
}
