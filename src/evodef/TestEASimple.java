package evodef;

import bandits.MBanditEA;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import ntuple.NTupleSystem;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provide a simple main method to test this...
 */

public class TestEASimple {

    static int nDims = 10;
    static int mValues = 2;

    static int nTrialsRMHC = 10000;
    static int nTrialsNTupleBanditEA = 30;

    static int nFitnessEvals = 500;

    static boolean useFirstHit = true;
    static int maxResamples = 20;
    static NoisySolutionEvaluator solutionEvaluator;


    public static void main(String[] args) {

        // run configuration for an experiment

        useFirstHit = false;
        Mutator.flipAtLeastOneValue = true;
        Mutator.defaultPointProb = 0.0;

        // select which one to use
        solutionEvaluator = new EvalMaxM(nDims, mValues, 1.0);
        // solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, 1.0);

        System.out.println("Running experiment with following settings:");
        System.out.println("Solution evaluator: " + solutionEvaluator.getClass());
        System.out.format("Use first hitting time    :\t %s\n" , useFirstHit );
        System.out.format("RMHC: (flip at least one) :\t %s\n" , Mutator.flipAtLeastOneValue );
        System.out.format("Point mutation probability:\t %.4f\n" , Mutator.defaultPointProb / nDims );

        ElapsedTimer t = new ElapsedTimer();

//        StatSummary nt = testNTupleBanditEA(nTrialsNTupleBanditEA);
//        System.out.println(t);

        // Mutator.totalRandomChaosMutation = true;

        testRMHCAlone();
        System.out.println(t);


    }

    static StatSummary testNTupleBanditEA(int nTrials) {
        StatSummary ss = runTrials(new NTupleBanditEA(), nTrials, 0);
        System.out.println(ss);
        return ss;
    }

    static void testRMHCAlone() {
        // simpler version does not compare performance with NT
        ArrayList<StatSummary> results = new ArrayList<>();
        for (int i = 1; i <= maxResamples; i++) {
            // System.out.println("Resampling rate: " + i);
            StatSummary ss2 = runTrials(new SimpleRMHC(i), nTrialsRMHC, i);
            results.add(ss2);
            // System.out.println(ss2);
            // System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", i, ss2.mean(), ss2.stdErr(), ss2.max());
        }

    }


    static void testRMHC(StatSummary nt) {
        ArrayList<StatSummary> results = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            // System.out.println("Resampling rate: " + i);
            StatSummary ss2 = runTrials(new SimpleRMHC(i), nTrialsRMHC, i);
            results.add(ss2);
            // System.out.println(ss2);
            System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", i, ss2.mean(), ss2.stdErr(), ss2.max());
        }

        printJS(results, nt);

        //
    }

    // this is to print out rows of JavaScript for use in a GoogleChart
    public static void printJS(List<StatSummary> ssl, StatSummary nt) {
        double ntm = nt.mean();
        double ntUpper = ntm + nt.stdErr() * 3;
        double ntLower = ntm - nt.stdErr() * 3;
        for (int i = 0; i < ssl.size(); i++) {
            StatSummary ss = ssl.get(i);
            double m = ss.mean();
            double upper = m + 3 * ss.stdErr();
            double lower = m - 3 * ss.stdErr();
            System.out.format("[ %d, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, ],\n", (1 + i), m, lower, upper, ntm, ntLower, ntUpper);
        }
    }


    public static StatSummary runTrials(EvoAlg ea, int nTrials, int nResamples) {
        StatSummary ss = new StatSummary();
        StatSummary nTrueOpt = new StatSummary("N True Opt Hits");

        for (int i = 0; i < nTrials; i++) {
            ss.add(runTrial(ea, nTrueOpt));
        }

        // System.out.println(ss);

        System.out.format("N Resamples: \t %2d ;\t n True Opt %s: %.2f\n",nResamples, useFirstHit ? "hits" : "returns", nTrueOpt.n() * 100.0 / nTrials);
        return ss;
    }


    static double runTrial(EvoAlg ea, StatSummary nTrueOpt) {

        // grab from static var for now
        NoisySolutionEvaluator evaluator = solutionEvaluator;
        evaluator.reset();

        int[] solution = ea.runTrial(evaluator, nFitnessEvals);

        //  horrible mess at the moment - changing to a different evaluator
//        if (useFirstHit && evaluator.logger().firstHit != null) {
//            // System.out.println("Optimal first hit?: " + evaluator.logger().firstHit);
//            nTrueOpt.add(evaluator.logger().firstHit);
//        } else if (trueEvaluator.evaluate(solution) == 1.0) {
//            nTrueOpt.add(1);
//        }

        if (useFirstHit && evaluator.logger().firstHit != null) {
            // System.out.println("Optimal first hit?: " + evaluator.logger().firstHit);
            nTrueOpt.add(evaluator.logger().firstHit);
        } else if (evaluator.isOptimal(solution)) {
            nTrueOpt.add(1);
        }

        return solutionEvaluator.trueFitness(solution);

    }
}
