package evodef;

import ga.SimpleGA;
import ga.SimpleRMHC;
import ntuple.CompactBinaryGA;
import ntuple.CompactSlidingGA;
import ntuple.SlidingMeanEDA;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LineGroup;
import plot.LinePlot;
import utilities.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by simonmarklucas on 10/07/2017.
 */

public class TestEAGraphRunTrials {

    public static ArrayList<ArrayList<Double>> extras = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // create and run a test
        // showing flexibility to create multiple graphs

        int nDims=100, mValues = 2;
        double noise = 1.0;
        int nEvals = 1000;
        int nTrials = 10;

        NoisySolutionEvaluator solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, noise);
        solutionEvaluator = new EvalMaxM(nDims, mValues, noise);
        // solutionEvaluator = new Eval2DNonLinear(8, noise);

        TestEAGraph tester = new TestEAGraph(solutionEvaluator, nEvals).setColor(Color.red);

        // Set up all the algorithms to test

        SimpleRMHC rmhc1 = new SimpleRMHC(1);
        SimpleRMHC rmhc5 = new SimpleRMHC(5);

        SimpleGA sga = new SimpleGA().setPopulationSize(20).setCrossoverRate(0.5);

        int windowLength = 40;
        CompactSlidingGA slidingGA = new CompactSlidingGA().setHistoryLength(windowLength);
        slidingGA.useBayesUpdates = false;
        slidingGA.K = 1000; // nDims * windowLength / 2;

        int nParents = 2;
        CompactBinaryGA cga = new CompactBinaryGA().setParents(nParents);
        // cga.K = nDims / 2; //  * nParents;  // setting from Jialin
        cga.K = 1000; // nDims * nParents;
        // cga.nToFlip = 2;



        // add them to the test list
        ArrayList<EvoAlg> evos = new ArrayList<>();
        // evos.add(new SlidingMeanEDA().setHistoryLength(30));
        evos.add(sga);
        evos.add(rmhc1);
        evos.add(rmhc5);
        evos.add(slidingGA);
        evos.add(cga);

        nParents = 20;
        CompactBinaryGA cga2 = new CompactBinaryGA().setParents(nParents);
        cga2.K = nDims * nParents * 2;

        // evos.add(cga2);

//        evos.add(rmhc1);
//        evos.add(rmhc5);

        // evos.add(new SlidingMeanEDA().setHistoryLength(30));

        Color[] colors = {Color.red, Color.green, Color.blue, Color.yellow, Color.cyan, Color.pink, Color.magenta};

        LineChart lineChart = new LineChart().setTitle(String.format("Noisy OneMax, %d dimensions", nDims));
        lineChart.setXLabel("Fitness Evaluations").setYLabel("Noise-Free Fitness");

        lineChart.xAxis = new LineChartAxis(new double[]{0, 200, 400, 600, 800, 1000});
        lineChart.yAxis = new LineChartAxis(new double[]{40, 50, 60, 70, 80, 90, 100});
        //
        // lineChart.yAxis = new LineChartAxis(new double[]{0.4, 0.6, 0.8, 1.0});


        for (int i=0; i<evos.size(); i++) {
            ElapsedTimer elapsedTimer = new ElapsedTimer();
            tester.setColor(colors[i]);
            TestEvoResults results = tester.runTrials(evos.get(i), nTrials);
            System.out.println("Tested: " + evos.get(i).getClass().getName());
            System.out.println(results.trueOpt);
            System.out.println(elapsedTimer);
            System.out.println();
            lineChart.addLines(results.linePlots);

            lineChart.addLineGroup(results.getLineGroup().setColor(colors[i]).setName(results.name));
        }

        LineGroup pVec = new LineGroup().setName("P-Vec").setColor(Color.white);
        for (ArrayList<Double> extra : extras) {
            // lineChart.addLine(new LinePlot().setColor(Color.white).setData(extra));
            pVec.add(extra);
        }
        // lineChart.addLineGroup(pVec);

        new JEasyFrame(lineChart, "Fitness Evolution");

        String dir = "results/javares/sweda/";
        // String filename = "resultsOneMax.png";
        String filename = "resultsOneMaxPVec.png";
        lineChart.saveImage(dir, filename);
    }
}
