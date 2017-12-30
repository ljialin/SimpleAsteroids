package ntuple;

import evodef.EvalNoisyWinRate;
import evodef.FitnessSpace;
import evodef.SearchSpaceUtil;
import utilities.ElapsedTimer;
import utilities.Ranker;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by sml on 09/01/2017.
 */
public class NTupleSystemTest {

    static Random rand = new Random();
    static int nDims = 10;

    // the fitness for current purposes will be scaled in range zero to 1
    static double noise = 1.0 / nDims;

    // create a way to make N-Tuple systems ...

    public static void main(String[] args) {

        // NTupleSystem nts = new NTupleSystem(new TenSpace(nDims, 2));
        int m = 2;

        FitnessSpace evalTrue = new EvalNoisyWinRate(nDims, m);
        FitnessSpace evalNoisy = new EvalNoisyWinRate(nDims, m, 1.0);

        NTupleSystem nts = new NTupleSystem();
        nts.setSearchSpace(evalNoisy);
        // nts.addTuples();
        // nts.printSummaryReport();

        // nts = new NTupleSystem(BattleGameSearchSpace.getSearchSpace());
        // nts.addTuples();

        // now add some random data

        ElapsedTimer t = new ElapsedTimer();

        double[] hist = new double[50];
        int nReps = 1;

        int nPoints = 1000;
        nPoints = (int) SearchSpaceUtil.size(nts.searchSpace);

        nPoints *= 100;
        // nPoints

        for (int i=0; i<nPoints; i++) {
            int[] p = SearchSpaceUtil.randomPoint(nts.searchSpace);
            // p = SearchSpaceUtil.nthPoint(nts.searchSpace, i);

            // make the value depend on a few of the indexs rather than
            // the actual values, just for a simple test
            // System.out.println(Arrays.toString(p));

            for (int j=0; j<nReps; j++) {
                double value = evalNoisy.evaluate(p);
                // index of histogram calculation assumes that
                // value is in range 0 .. 1
                int ix = (int) (value * (hist.length - 1));
                // System.out.println(ix) + ;
                // hist[ix]++;
                nts.addPoint(p, value);
            }

        }
        // BarChart.display(hist, "Distribution");

        System.out.format("Added %d points\n", nPoints);
        nts.printSummaryReport();
        System.out.println(t);


        System.out.println("Now testing ...");
        System.out.println(t);

        // nPoints = 1000;

        StatSummary ss = new StatSummary();

        Ranker<Integer> trueRank = new Ranker<>();
        Ranker<Integer> estRank = new Ranker<>();


        // ensure we sample all the points in the search space when testing
        nPoints = (int) SearchSpaceUtil.size(nts.searchSpace);

        // the rank correlation was originally computed in-ine in the code,
        // but this method has now been superceded by a separate utility class
        // whose use is also demonstrated

        RankCorrelation rc = new RankCorrelation();


        for (int i=0; i<nPoints; i++) {
            int[] p = SearchSpaceUtil.randomPoint(nts.searchSpace);

            p = SearchSpaceUtil.nthPoint(nts.searchSpace, i);

            // make the value depend on a few of the indices rather than
            // the actual values, just for a simple test
            // System.out.println("Probing: " + Arrays.toString(p));

            // Double value = nts.get(p);
            Double value = nts.getMeanEstimate(p);
            // value = Math.random();


            if (value!= null) {
                double trueVal = evalTrue.evaluate(p);
                double diff = Math.abs(trueVal - value);

                rc.add(i, value, trueVal);
                ss.add(diff);
                // System.out.format("%d\t %.34f\t %.4f\n", i, value, trueVal);
                trueRank.add(trueVal, i);
                estRank.add(value, i);
                // System.out.println();
            }
            // System.out.println(ss);
            // nts.addPoint(p, value);
//            System.out.println("Returned value = " + value);
//            System.out.println();

        }
        System.out.println();
        System.out.println(ss);
        System.out.println(t);

        // now show the ranks

        double sumSquaredDiff = 0;

        for (int i=0; i<nPoints; i++) {
            // System.out.println(i + "\t " + trueRank.getRank(i) + "\t " + estRank.getRank(i));
            int[] x = SearchSpaceUtil.nthPoint(nts.searchSpace, i);
            // System.out.println(Arrays.toString(nts.getExplorationVector(x)) + " : " + nts.getExplorationEstimate(x));
            // System.out.println();
            sumSquaredDiff += sqr(trueRank.getRank(i) - estRank.getRank(i));
        }

        // System.out.println("diffSum = " + sumSquaredDiff);
        double spearmanCoefficient = 1 - sumSquaredDiff * 6 / (nPoints * nPoints * nPoints - nPoints);
        System.out.format("Spearman correlation = %.4f\n", spearmanCoefficient);

        System.out.println("And the RankCorrelation utility class test: ");


        double rankCorrelation = rc.rankCorrelation();
        System.out.println("RC = " + rankCorrelation);


        // nts.printDetailedReport();
    }

    static double sqr(double x) { return x * x; }

    static double sigmoid(double x) {
        return 1.0 / (1 + (Math.exp(-x)));
    }

    static double defaultErr = 10;





    // just declare a temporary value function ...

    // the internal value func is now not used; better instead to use
    // the true fitness value from the solution evaluator

    //    static double valueFunc(int[] p, double noise) {
//        double tot = 0;
//        for (int x : p) tot += x;
//        double x = tot / p.length + rand.nextGaussian() * noise;
//        x = Math.max(0, x);
//        x = Math.min(1, x);
//        return x;
//    }
//
    static double valueFunc2(int[] p, double noise) {
        return sigmoid(rand.nextGaussian() * noise + 0.1 * (p[0] * p[1] * p[2] - 2 * p[3] * p[4]));
    }

    static double valueFunc1(int[] p, double noise) {
        return sigmoid(rand.nextGaussian() * noise + (p[0] + p[1] + p[2] - p[3] - p[4]));
    }



    // idea here is to test the value of each n-tuple and compare it to the true value
    static int nSamples = 100;
    static double valueEstimate(NTuple nTuple, int[] p) {
        return 0;
    }


    public static void exploreNTuples(NTupleSystem nts) {
        int nPoints = (int) SearchSpaceUtil.size(nts.searchSpace);
        for (int i=0; i<nPoints; i++) {
            int[] x = SearchSpaceUtil.nthPoint(nts.searchSpace, i);
            System.out.println(Arrays.toString(nts.getExplorationVector(x)) + " : " + nts.getExplorationEstimate(x));
            System.out.println(nts.getMeanEstimate(x));
            System.out.println();
        }
    }
}
