package evodef;

import ga.SimpleRMHC;
import ntuple.CompactBinaryGA;
import ntuple.CompactSlidingGA;
import ntuple.SlidingMeanEDA;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.LineChart;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by simonmarklucas on 10/07/2017.
 */
public class TestEAGraphRunTrials {

    public static void main(String[] args) {

        // create and run a test
        // showing flexibility to create multiple graphs

        int nDims=100, mValues = 2;
        double noise = 1.0;
        int nEvals = 500;
        int nTrials = 20;

        NoisySolutionEvaluator solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, noise);
        solutionEvaluator = new EvalMaxM(nDims, mValues, noise);
        // solutionEvaluator = new Eval2DNonLinear(8, noise);

        TestEAGraph tester = new TestEAGraph(solutionEvaluator, nEvals).setColor(Color.red);


        // Set up all the algorithms to test

        SimpleRMHC rmhc1 = new SimpleRMHC(1);
        SimpleRMHC rmhc5 = new SimpleRMHC(4);

        int windowLength = 20;
        CompactSlidingGA slidingGA = new CompactSlidingGA().setHistoryLength(windowLength);
        slidingGA.K = nDims * windowLength;

        int nParents = 10;
        CompactBinaryGA cga = new CompactBinaryGA().setParents(nParents);
        cga.K = nDims * nParents;


        // add them to the test list
        ArrayList<EvoAlg> evos = new ArrayList<>();
        // evos.add(new SlidingMeanEDA().setHistoryLength(30));
        evos.add(slidingGA);
        evos.add(cga);


//        evos.add(rmhc1);
//        evos.add(rmhc5);

        Color[] colors = {Color.red, Color.green, Color.blue, Color.yellow, Color.pink, Color.magenta};

        LineChart lineChart = new LineChart();

        for (int i=0; i<evos.size(); i++) {

            ElapsedTimer elapsedTimer = new ElapsedTimer();
            tester.setColor(colors[i]);
            TestEvoResults results = tester.runTrials(evos.get(i), nTrials);
            System.out.println("Tested: " + evos.get(i).getClass().getName());
            System.out.println(results.trueOpt);
            System.out.println(elapsedTimer);
            System.out.println();
            lineChart.addLines(results.linePlots);
        }

        new JEasyFrame(lineChart, "Fitness Evolution");
    }
}
