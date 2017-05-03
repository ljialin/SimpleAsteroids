package bandits;

import utilities.BarChart;
import utilities.JEasyFrame;

import java.awt.*;

/**
 * Created by simonmarklucas on 26/06/2016.
 */
public class OptimalResampleTest {


    static double noiseStdDev = 1.0;

    public static void main(String[] args) {
        // the purpose of this is to test whether we can predict the optimal number
        // of resamples to make the final leap

        // they key thing is whether increasing

        int nBits = 35;

        int k = 3;

        visualise(nBits, k);


//        for (int k=1; k<=10; k++) {
//            System.out.format("k = %d,\t nBits = %d,\t expected fitness evals = %d\n", k, nBits, testK(nBits, k));
//        }

        // now for the next part

        // run the update and see what happens



        // System.out.println("pErr = " + pErr);
        // System.out.println(diffuser.toString());

    }

    public static void visualise(int nBits, int k) {
        int nIterationsMax = 100000;
        RMHCOneMaxProbabilityDiffuser diffuser = new RMHCOneMaxProbabilityDiffuser(nBits);

        double resampled = noiseStdDev / Math.sqrt(k);

        GaussTable gaussTable = new GaussTable();

        double pCorrect = gaussTable.erf(1 / resampled);

        double pErr = 1 - pCorrect;

        // use this of we need to keep stats later
        Integer iterationsNeeded = null;

        // diffuser.
        String title = "P Mass Diffusion, nEvals: ";
        BarChart bc = new BarChart(new Dimension(640, 480), diffuser.p);
        JEasyFrame frame = new JEasyFrame(bc, title);



        for (int i=0; i< nIterationsMax; i++) {
            // System.out.println(i);
            // diffuser.updateStandard(false);
            bc.update(diffuser.p);
            frame.setTitle(title + (i * k));

            diffuser.updateNoisy(false, pErr);
            // System.out.println(i + "\t " + diffuser.pSolved());


            try {
                Thread.sleep(50);
            } catch (Exception e) {};

            if (diffuser.pSolved() >= 0.5) {
                return;
                // iterationsNeeded = i;
//                System.out.println("K = " + k);
//                System.out.println("nBits = " + nBits );
//                System.out.println("Solved in iterations: " + i);
//                System.out.println("Fitness evals:        " + i * k);
            }
        }
    }

    public static Integer testK(int nBits, int k) {
        int nIterationsMax = 100000;
        RMHCOneMaxProbabilityDiffuser diffuser = new RMHCOneMaxProbabilityDiffuser(nBits);

        double resampled = noiseStdDev / Math.sqrt(k);

        GaussTable gaussTable = new GaussTable();

        double pCorrect = gaussTable.erf(1 / resampled);

        double pErr = 1 - pCorrect;

        // use this of we need to keep stats later
        Integer iterationsNeeded = null;

        for (int i=0; i< nIterationsMax; i++) {
            // System.out.println(i);
            // diffuser.updateStandard(false);
            diffuser.updateNoisy(false, pErr);
            // System.out.println(i + "\t " + diffuser.pSolved());

            if (diffuser.pSolved() >= 0.5) {
                iterationsNeeded = i;
//                System.out.println("K = " + k);
//                System.out.println("nBits = " + nBits );
//                System.out.println("Solved in iterations: " + i);
//                System.out.println("Fitness evals:        " + i * k);
                return i * k;
            }
        }
        return null;
    }

    public static double gaussian(double x) {
        return Math.exp(- x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    public static class RMHCOneMaxProbabilityDiffuser {

        double[] p;

        // now we consider what else we need to do with this

        int n;

        public RMHCOneMaxProbabilityDiffuser(int nBits) {
            // in a String of length nBits long
            // the number of on bits is between 0 and n inclusive
            // hence we make the probability mass array 1 bigger
            this.n = nBits+1;
            p = new double[n];

            // put all the probability mass in one place to begin with
            p[n/2] = 1;
            // System.out.println("Hey, n = " + n);
        }

        public void updateStandard(boolean verbose) {
            // the update is really simple:
            // at each stage we make a mutation with a
            // specified probability that it will be an improvement

            // the index refers to

            double[] pNext = new double[n];
            for (int i=1; i<n; i++) {
                // the demoninator is n-1, since there are n-1 bits
                double pWorse = (double) (i-1) / (n-1);
                double pBetter = 1 - pWorse;

                if (verbose) {
                    System.out.format("%d\t %.5f\n", i, pBetter);
                }

                // in this model if it's worse then the mass just stays put
                pNext[i-1] += p[i-1] * pWorse;

                pNext[i] += p[i-1] * pBetter;

            }
            // the solved state is absorbing, so the previous value must be added in
            pNext[n-1] += p[n-1];
            p = pNext;
            if (verbose) System.out.println();

        }

        public void updateNoisy(boolean verbose, double pErr) {
            // the update is really simple:
            // at each stage we make a mutation with a
            // specified probability that it will be an improvement

            // there are now three possible ways in which probability mass
            // may move

            // 1.  A mutation for better is correctly accepted
            //     moving the probability mass from state i to state i+1
            //       This happens with a probability of pBetter * (1 - pErr)

            // 2.  A mutation for worse is incorrectly accepted
            //     moving the mass from state i to state i-1
            //       This happens with a probability of pWorse * pErr

            // 3.  The mass stays in state i in one of two ways:
            //     either:
            //         (a) a move to a higher state set is incorrectly rejected
            //     or
            //         (b) a move to a lower state set is correctly rejected

            //    (a) happens with probability   pBetter * pErr
            //    (b) happens with probability   pWorse * (1 - pErr)

            double[] pNext = new double[n];
            for (int i=2; i<n; i++) {
                // the demoninator is n-1, since there are n-1 bits
                double pWorse = (double) (i-1) / (n-1);
                double pBetter = 1 - pWorse;

                double pImprove = pBetter * (1 - pErr);
                double pSame = pBetter * pErr + pWorse * (1 - pErr);
                double pDegrade = pWorse * pErr;

                if (verbose) {
                    System.out.format("%d\t %.5f\n", i, pBetter);
                }

                // in this model if it's worse then the mass just stays put
                pNext[i-1] += p[i-1] * pSame;

                pNext[i] += p[i-1] * pImprove;

                pNext[i-2] += p[i-1] * pDegrade;

            }
            // the solved state is absorbing, so the previous value must be added in
            pNext[n-1] += p[n-1];
            p = pNext;
            if (verbose) System.out.println();

        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            double tot = 0;
            for (int i=0; i<n; i++) {
                tot += p[i];
                sb.append(String.format("%d\t %.5f\n", i, p[i] ));
            }
            sb.append(String.format("pTot = %.5f\n", tot));
            return sb.toString();
        }

        public double pSolved() {
            return p[n-1];
        }

    }

    public static class GaussTable {
        int n = 1000;
        double min = -10;
        double max = 10;
        double[] tab;
        double[] integral;
        double step = (max - min) / n;

        public GaussTable() {
            tab = new double[n];
            integral = new double[n];
            double totArea = 0;
            for (int i=0; i<n; i++) {
                double x = min + i  * (max - min) / n;
                tab[i] = gaussian(x);
                totArea += step * tab[i];
                integral[i] = totArea;
                // System.out.println(i + "\t " + x + "\t " + tab[i] + "\t " + totArea);
            }
            // System.out.println("Integral = " + totArea);
        }

        public double erf(double x) {
            // just return the table entry
            if (x < min) return 0;
            if (x >= max) return 1;

            int ix = (int) ((x - min) * n / (max - min));
            // System.out.println("ix = " + ix);
            return integral[ix];

        }

    }

    public static void oldTests() {
        int n = 100;

        double pAccept = 1.0/n;

        GaussTable gaussTable = new GaussTable();

        System.out.println();
        for (int i=-5; i<=5; i++) {
            System.out.println(gaussTable.erf(i));
        }

        // as the number of resamples is increased, so the probability
        // of accepting a false move decreases

        // but does it have to decrease ...
        // ah well it's a Markov chain with one absorbing state - the perfection state

        double noiseLevel = 10;

        for (int k=1; k<=40; k++) {

            //
            double resampled = noiseLevel / Math.sqrt(k);

            double pCorrect = gaussTable.erf(1 / resampled);

            double expectedTime = 1.0 / (pCorrect * pAccept);

            double expectedSamples = k * expectedTime;

            // System.out.format("%d\t %.3f\t %.3f\n", k, resampled, expectedSamples);
        }


    }



}
