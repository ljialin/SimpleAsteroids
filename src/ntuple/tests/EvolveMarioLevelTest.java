package ntuple.tests;

import evodef.EvoAlg;
import evodef.SolutionEvaluator;
import ga.SimpleRMHC;
import ntuple.ConvNTuple;
import ntuple.LevelView;
import plot.LineChart;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

import static levelgen.MarioReader.flip;
import static levelgen.MarioReader.readLevel;
import static levelgen.MarioReader.tileColors;

public class EvolveMarioLevelTest {
    public static void main(String[] args) throws Exception {

        int[][] level = getAndShowLevel(true);

        int nTrials = 1;
        EvoAlg evoAlg = new SimpleRMHC();
        // evoAlg = new SlidingMeanEDA().setHistoryLength(30);
        // evoAlg = new CompactSlidingGA();

        int nEvals = 500000;
        StatSummary results = new StatSummary();
        EvolveMarioLevelTest evolver = new EvolveMarioLevelTest();
        for (int i=0; i<nTrials; i++) {

            ElapsedTimer timer = new ElapsedTimer();
            results.add(evolver.runTrial(evoAlg, nEvals, level));

            System.out.println(timer);
        }

    }


    public static int[][] getAndShowLevel(boolean show) throws Exception {
        String inputFile = "data/mario/levels/mario-3-1.txt";

        System.out.println("Reading: " + inputFile);
        int[][] level = flip(readLevel(new Scanner(new FileInputStream(inputFile))));
        if (show) {
            LevelView levelView = new LevelView(level).setColorMap(tileColors).setCellSize(10);
            new JEasyFrame(levelView, inputFile);
        }
        return level;
    }

    public static void printLevel(int[][] level) {
        for (int[] a : level) {
            System.out.println(Arrays.toString(a));
        }
    }



    static int imageWidth = 30, imageHeight = 20;
    static int filterWidth = 2, filterHeight = 2;
    static int stride = 2;


    public double runTrial(EvoAlg ea, int nEvals, int[][] sample) {
        int nDims = imageWidth * imageHeight;
        int mValues = distinctValues(sample);
        ConvNTuple convNTuple = getTrainedConvNTuple(sample, mValues);

        SolutionEvaluator evaluator = new EvalConvNTuple(nDims, mValues).setConvNTuple(convNTuple);
        int[] solution = ea.runTrial(evaluator, nEvals);
        double fitness = evaluator.evaluate(solution);
        String label = String.format("Fitness: %.6f", fitness);

        LevelView.showMaze(solution, imageWidth, imageHeight, label, tileColors);
        new JEasyFrame(LineChart.easyPlot(evaluator.logger().fa), "Evolution of Fitness");
        return fitness;
    }

    private int distinctValues(int[][] a) {
        StatSummary ss = new StatSummary();
        for (int i=0; i<a.length; i++) {
            for (int j=0; j<a[0].length; j++) {
                ss.add(a[i][j]);
            }
        }
        return 1 + (int) ss.max();
    }

    public static ConvNTuple getTrainedConvNTuple(int[][] sample, int mValues) {
        int w = sample.length;
        int h = sample[0].length;


        System.out.println(w + " : " + h);


        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        convNTuple.setMValues(mValues).setStride(stride);

        convNTuple.makeWrapAroundIndices();
        convNTuple.reset();

        System.out.println("Address space size: " + convNTuple.addressSpaceSize());
        // System.out.println("Mean of empty summary: " + new StatSummary().mean());

        // now put some random data in to it
        ElapsedTimer timer = new ElapsedTimer();

        // 'x' is the Red Maze example explained here:
        // https://adamsmith.as/papers/wfc_is_constraint_solving_in_the_wild.pdf

        int[] x = flatten(sample);

        convNTuple.addPoint(x, 1);

        // now iterate over all the values in there
        System.out.println("Training finished: ");
        System.out.println(timer);

        // convNTuple.report();
        // System.out.println(timer);

        // now reset the indices to the true image size, but do not use wrap around
        convNTuple.setImageDimensions(imageWidth, imageHeight);
        convNTuple.makeWrapAroundIndices();
        // convNTuple.report();

        return convNTuple;
    }

    static int[] flatten(int[][] a) {
        int n = a.length * a[0].length;
        int w = a.length;
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            x[i] = a[i % w][i / w];
        }
        return x;
    }


}

