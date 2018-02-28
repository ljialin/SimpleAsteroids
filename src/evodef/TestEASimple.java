package evodef;

import ntuple.*;
import plot.LineChart;
import plot.LinePlot;
import utilities.*;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide a simple main method to test this...
 */

public class TestEASimple {

    static int nDims = 20;
    static int mValues = 2;

    static int nTrialsRMHC = 10;
    // static int nTrialsNTupleBanditEA = 30;

    static int nFitnessEvals = 500;

    static boolean useFirstHit;
    static int minSamples = 1;
    static int maxResamples = 1;
    static NoisySolutionEvaluator solutionEvaluator;
    static double noise = 1.0;

    static String outputName = "data/noisefree_w10.dat";

    public static void main(String[] args) {
        for (int w=30; w<=30; w=w+10) {
            outputName = "data/noisefree_w" + w + ".dat";
            File f = new File(outputName);
            if (f.exists() && !f.isDirectory()) {
                f.delete();
            }
            // run configuration for an experiment

            // todo need an easy and general way to log the best solution yet
            // just add a new log method to the logger

            useFirstHit = false;
            // DefaultMutator.flipAtLeastOneValueDefault = true;
            // DefaultMutator.defaultPointProb = 1.0;

            // select which one to use
            // solutionEvaluator = new EvalMaxM(nDims, mValues, noise);
            solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, noise);
            // solutionEvaluator = new Eval2DNonLinear(8, noise);

            System.out.println("Running experiment with following settings:");
            System.out.println("Solution evaluator: " + solutionEvaluator.getClass());
            System.out.format("Use first hitting time    :\t %s\n", useFirstHit);
            System.out.format("RMHC: (flip at least one) :\t %s\n", DefaultMutator.flipAtLeastOneValueDefault);
            System.out.format("Point mutation probability:\t %.4f\n", DefaultMutator.defaultPointProb / nDims);

            ElapsedTimer t = new ElapsedTimer();

//        StatSummary nt = testNTupleBanditEA(nTrialsNTupleBanditEA);
//        System.out.println(t);

            // DefaultMutator.totalRandomChaosMutation = true;

            int defaultK = 2000;

            CompactBinaryGA cga = new CompactBinaryGA(defaultK);
            cga.nParents = 10
            ;

            // each time we run a test, we want to get
            // the way the fitness evolves over time
            // hence we need to get a list of arrays for each experiment

            // have an alpha less than 1 to be able to spot overlapping lines more easily
            float alpha = 0.8f;

//            lineColor = new Color(1f, 1f, 0, alpha);
//            testEvoAlg(new SimpleRMHC());

//            lineColor = new Color(0f, 1f, 1, alpha);
//            testEvoAlg(cga);
//
//            lineColor = new Color(1f, 0f, 1, alpha);
//            testEvoAlg(new CompactSlidingGA(2000).setHistoryLength(w));

            // lineColor = Color.getHSBColor(0.7f, 1, 1);
//            lineColor = Color.red;
//            testEvoAlg(new CompactSlidingModelGA().setHistoryLength(w));

            // lineColor = Color.getHSBColor(0.5f, 1, 1);
            lineColor = Color.green;
            // testEvoAlg(new NTupleBanditEA());

            // lineColor = Color.getHSBColor(0.3f, 1, 1);
            lineColor = Color.blue;
            testEvoAlg(new SlidingMeanEDA().setHistoryLength(w));

            // testBanditEA();
            System.out.println(t);
            LineChart lineChart = new LineChart();
            for (LinePlot line : linePlots) {
                lineChart.addLine(line);
            }

            // add a linear plot

//            LinePlot linear = new LinePlot();
//            linear.setColor(Color.white);
//            for (int i = 0; i < nDims; i++)
//                linear.add(solutionEvaluator.optimalIfKnown()  * (0.5 + i / (2.0 * nDims)));
//            lineChart.addLine(linear);

            new JEasyFrame(lineChart, "Evolution Traces");
        }
    }

    static Color lineColor;

//    static StatSummary testNTupleBanditEA(int nTrials) {
//        StatSummary ss = runTrials(new NTupleBanditEA(), nTrials, 0);
//        System.out.println(ss);
//        return ss;
//    }
//
//    static StatSummary testNTupleBanditEA(int nTrials, int nSamples) {
//        StatSummary ss = runTrials(new NTupleBanditEA(), nTrials, 0);
//        System.out.println(ss);
//        return ss;
//    }
//

    // ok need to have a single version
    static void testEvoAlg(EvoAlg evoAlg) {
        // simpler version does not compare performance with NT
        ArrayList<StatSummary> successRates = new ArrayList<>();
        ArrayList<StatSummary> fitness = new ArrayList<>();


        for (int i = minSamples; i <= maxResamples; i++) {
            // System.out.println("Resampling rate: " + i);
            evoAlg.setSamplingRate(i);
            StatSummary trueFit = new StatSummary();
            StatSummary ss2 = runTrials(evoAlg, nTrialsRMHC, i, trueFit);
            successRates.add(ss2);
            fitness.add(trueFit);
            // System.out.println(ss2);
            // System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", i, ss2.mean(), ss2.stdErr(), ss2.max());
        }

        // now suppose we may just want to track the number of successes
        // in this case we just take the number of samples out of each StatSummary

        ArrayList<Double> successRate = new ArrayList<>();
        for (StatSummary ss : successRates) {
            successRate.add(ss.n() * 100.0 / nTrialsRMHC);
        }
        ArrayList<Double> fitnessArray = new ArrayList<>();
        for (StatSummary ss : fitness) {
            fitnessArray.add(ss.mean());
        }
        System.out.println("Success rate array:");
        System.out.println(successRate);
        System.out.println("Fitness array:");
        System.out.println(fitnessArray);
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

    // for now use static array lists

    static ArrayList<LinePlot> linePlots = new ArrayList<>();


    public static StatSummary runTrials(EvoAlg ea, int nTrials, int nResamples, StatSummary trueFit) {
        // summary of fitness stats
        // StatSummary ss = new StatSummary();

        // summary of optima found
        //


        StatSummary nTrueOpt = new StatSummary("N True Opt Hits");

        for (int i = 0; i < nTrials; i++) {
            runTrial(ea, nTrueOpt, trueFit);
        }

        // the ss summary shows the average level of fitness reached
        // but instead we may be looking at the percentage of times the optimum is found
        // System.out.println(ss);

        System.out.format("N Resamples: \t %2d ;\t n True Opt %s: %.2f\n", nResamples, useFirstHit ? "hits" : "returns", nTrueOpt.n() * 100.0 / nTrials);

        // commented version prints out absolute of successes instead of percentage
        // System.out.format("N Resamples: \t %2d ;\t n True Opt %s: %3d\n",nResamples, useFirstHit ? "hits" : "returns", nTrueOpt.n()  );
        return nTrueOpt;
    }


    static double runTrial(EvoAlg ea, StatSummary nTrueOpt, StatSummary trueFit) {

        // grab from static var for now
        NoisySolutionEvaluator evaluator = solutionEvaluator;
        evaluator.reset();

        int[] solution = ea.runTrial(evaluator, nFitnessEvals);
        // System.out.println("Solution: " + Arrays.toString(solution) + " : " + solutionEvaluator.trueFitness(solution));
        trueFit.add(solutionEvaluator.trueFitness(solution));

        // linePlots.add(new LinePlot().setData(solutionEvaluator.logger().fa).setColor(lineColor));

        // ok, instead look at the true fitnesses of the evaluated solutions

        ArrayList<Double> noiseFree = new ArrayList<>();
        // System.out.println("Best yet solutions length: " + solutionEvaluator.logger().bestYetSolutions.size());
        for (int[] p : solutionEvaluator.logger().bestYetSolutions) {
            noiseFree.add(solutionEvaluator.trueFitness(p));
        }
        // TODO: 23/06/2017 save data
        try
        {
            FileWriter fw = new FileWriter(outputName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            for (int i=0; i<noiseFree.size(); i++)
            {
                out.print(noiseFree.get(i)+ " ");
            }
            out.print("\n");
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("No such file exists.");
        }

        linePlots.add(new LinePlot().setData(noiseFree).setColor(lineColor));

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


    //    static void testBanditEA() {
//        // need to make the code more general and merge this and testEvoAlg()
//        // simpler version does not compare performance with NT
//        ArrayList<StatSummary> results = new ArrayList<>();
//        for (int i = 1; i <= maxResamples; i++) {
//            // System.out.println("Resampling rate: " + i);
//            NTupleBanditEA banditEA = new NTupleBanditEA();
//            banditEA.nSamples = i;
//
//            // System.out.println("Running trials: " + nTrialsRMHC);
//            StatSummary ss2 = runTrials(banditEA, nTrialsRMHC, i);
//            results.add(ss2);
//            // System.out.println(ss2);
//            // System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", i, ss2.mean(), ss2.stdErr(), ss2.max());
//        }
//
//        // now suppose we may just want to track the number of successes
//        // in this case we just take the number of samples out of each StatSummary
//
//        ArrayList<Double> successRate = new ArrayList<>();
//        for (StatSummary ss : results) {
//            successRate.add(ss.n() * 100.0 / nTrialsRMHC);
//        }
//        System.out.println("Results array:");
//        System.out.println(successRate);
//    }
//

//    static void testRMHC(StatSummary nt) {
//        ArrayList<StatSummary> successRate = new ArrayList<>();
//
//        for (int i = 1; i <= 20; i++) {
//            // System.out.println("Resampling rate: " + i);
//            StatSummary ss2 = runTrials(new SimpleRMHC(i), nTrialsRMHC, i);
//            results.add(ss2);
//            // System.out.println(ss2);
//            // System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", i, ss2.mean(), ss2.stdErr(), ss2.max());
//        }
//        printJS(results, nt);
//        //
//    }


}
