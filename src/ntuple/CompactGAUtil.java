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

        // for now also splice in the Bayes update
        // can then also compare them
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

    public static void updateBayes(SlidingBayes[] bayes, int [] winner, int[] loser) {

        // for now also splice in the Bayes update
        // can then also compare them
        for (int i=0; i<bayes.length; i++) {
            if (winner[i] != loser[i]) {
                if (winner[i] == 1) {
                    bayes[i].add(1);
                } else {
                    bayes[i].add(0);
                    // pVec[i] -= 1/K;
                }
            }
        }
    }

    public static void removeBayes(SlidingBayes[] bayes, int [] winner, int[] loser) {

        // for now also splice in the Bayes update
        // can then also compare them
        for (int i=0; i<bayes.length; i++) {
            if (winner[i] != loser[i]) {
                if (winner[i] == 1) {
                    bayes[i].remove(1);
                } else {
                    bayes[i].remove(0);
                    // pVec[i] -= 1/K;
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

    public static int[] argmax(SlidingBayes[] bayes) {
        int[] x = new int[bayes.length];
        for (int i=0; i<x.length; i++) {
            x[i] =  bayes[i].argmax();
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

    public static int[] randBitVec(double[] pVec, int[] base, int nDiff) {
        int[] x = new int[pVec.length];
        // first of all make a faithful copy of the base
        for (int i=0; i<x.length; i++)  {
            x[i] = base[i];
        }
        // now just pick a few to flip
        // this could be done in a better way but is ok for now ...
        for (int i=0; i<nDiff; i++) {
            int ix = random.nextInt(x.length);
            x[ix] = 1 - x[ix];
        }
        return x;
    }

    public static int[] randBitBayes(SlidingBayes[] bayes) {
        int[] x = new int[bayes.length];
        for (int i=0; i<x.length; i++) {
            x[i] = random.nextDouble() < bayes[i].pOne() ? 1 : 0;
        }
        return x;
    }
}
