package evodef;

import bandits.MBanditEA;
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

    static int nTrialsRMHC = 1000;
    static int nTrialsNTupleBanditEA = 30;

    static int nFitnessEvals = 100;

    public static void main(String[] args) {

        ElapsedTimer t = new ElapsedTimer();


        StatSummary nt = testNTupleBanditEA(nTrialsNTupleBanditEA);
        System.out.println(t);

        testRMHC(nt);
        System.out.println(t);


    }

    static StatSummary testNTupleBanditEA(int nTrials) {
        StatSummary ss = runTrials(new NTupleBanditEA(), nTrials);
        System.out.println(ss);
        return ss;
    }

    static void testRMHC(StatSummary nt) {
        ArrayList<StatSummary> results = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            // System.out.println("Resampling rate: " + i);
            StatSummary ss2 = runTrials(new SimpleRMHC(i), nTrialsRMHC);
            results.add(ss2);
            // System.out.println(ss2);
            System.out.format("Resample rate: %d\t %.3f\t %.3f   \n", i, ss2.mean(), ss2.stdErr());
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


    public static StatSummary runTrials(EvoAlg ea, int nTrials) {
        StatSummary ss = new StatSummary();

        for (int i = 0; i < nTrials; i++) {
            ss.add(runTrial(ea));
        }

        // System.out.println(ss);
        return ss;
    }

    static double runTrial(EvoAlg ea) {

//        SolutionEvaluator evaluator = new EvalMaxM(nDims, mValues, 1.0);
//        SolutionEvaluator trueEvaluator = new EvalMaxM(nDims, mValues, 0.0);

        SolutionEvaluator evaluator = new EvalNoisyWinRate(nDims, mValues, 1.0);
        SolutionEvaluator trueEvaluator = new EvalNoisyWinRate(nDims, mValues, 0.0);

        // just remember how best to do this !!!

        evaluator.reset();

        int[] solution = ea.runTrial(evaluator, nFitnessEvals);

//        System.out.println();
//        System.out.println("Returned solution: " + Arrays.toString(solution));
//        System.out.println("Fitness = " + trueEvaluator.evaluate(solution));

        return trueEvaluator.evaluate(solution);


    }
}
