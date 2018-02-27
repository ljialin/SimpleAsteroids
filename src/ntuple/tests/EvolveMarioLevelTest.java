package ntuple.tests;

import evodef.EvoAlg;
import evodef.EvolutionListener;
import evodef.EvolutionLogger;
import evodef.SolutionEvaluator;
import evogame.Mutator;
import ga.SimpleRMHC;
import levelgen.MarioReader;
import ntuple.ConvNTuple;
import ntuple.LevelView;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static levelgen.MarioReader.*;

public class EvolveMarioLevelTest implements EvolutionListener {

    static int imageWidth = 30, imageHeight = 20;
    static int filterWidth = 2, filterHeight = 2;
    static int stride = 1;

    static boolean useInitialSeed = true;


    public static void main(String[] args) throws Exception {

        int[][] level = getAndShowLevel(true);

        int nTrials = 1;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        Mutator mutator = new Mutator(null);
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 1;

        // mutator.setSwap(true);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;
        // evoAlg = new SlidingMeanEDA().setHistoryLength(30);
        // evoAlg = new CompactSlidingGA();

        int nEvals = 200000;
        StatSummary results = new StatSummary();
        EvolveMarioLevelTest evolver = new EvolveMarioLevelTest();
        for (int i = 0; i < nTrials; i++) {

            ElapsedTimer timer = new ElapsedTimer();
            results.add(evolver.runTrial(evoAlg, nEvals, level));

            System.out.println(timer);
        }

    }


    public static int[][] getAndShowLevel(boolean show) throws Exception {
        String inputFile = "data/mario/levels/mario-3-1.txt";

        System.out.println("Reading: " + inputFile);
        int[][] level = flip(readLevel(new Scanner(new FileInputStream(inputFile))));
        level = border(level);
        if (show) {
            LevelView levelView = new LevelView(level).setColorMap(tileColors).setCellSize(10);
            new JEasyFrame(levelView, inputFile);
        }
        return level;
    }

    public static int[][] border(int[][] a) {
        int[][] b = new int[a.length + 2][a[0].length + 2];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                if (i == 0 || j == 0 || i == b.length - 1 || j == b[0].length - 1) {
                    b[i][j] = MarioReader.border;
                } else {
                    b[i][j] = a[i - 1][j - 1];
                }
            }
        }
        return b;
    }

    public static int[][] setAll(int[][] a, int value) {
        int[][] b = new int[a.length][a[0].length];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = value;
            }
        }
        return b;
    }

    public static int[] setAll(int[] a, int value) {
        int[] b = new int[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = value;
        }
        return b;
    }

    public static void printLevel(int[][] level) {
        for (int[] a : level) {
            System.out.println(Arrays.toString(a));
        }
    }




    public double runTrial(EvoAlg ea, int nEvals, int[][] sample) {
        int nDims = imageWidth * imageHeight;
        int mValues = distinctValues(sample);
        ConvNTuple convNTuple = getTrainedConvNTuple(sample, mValues);
        System.out.println("nSamples: " + convNTuple.nSamples());

        if (useInitialSeed) {
            ea.setInitialSeed(generateSeed(sample));
        }
        SolutionEvaluator evaluator = new EvalConvNTuple(nDims, mValues).setConvNTuple(convNTuple);
        evaluator.logger().setListener(this);
        int[] solution = ea.runTrial(evaluator, nEvals);

        // can set entire solution to the most likely individual
        // solution = setAll(solution, 2);

        double fitness = evaluator.evaluate(solution);
        String label = String.format("Fitness: %.6f", fitness);

        // solution = flatten(toRect())
        plotData(evaluator.logger().fa);
        LevelView.showMaze(solution, imageWidth, imageHeight, label, tileColors);
        return fitness;
    }

    static Random random = new Random();

    private int[] generateSeed(int[][] sample) {
        int[] flatSample = flatten(sample);

        // now generate the initial seed by sampling directly from
        // the training sample

        int[] seed = new int[imageWidth * imageHeight];
        for (int i = 0; i < seed.length; i++) {
            int index = random.nextInt(flatSample.length);
            seed[i] = flatSample[index];
        }
        return seed;
    }

    private void plotData(ArrayList<Double> data) {
        LineChart lineChart = LineChart.easyPlot(data);
        int mid = (data.size() - 1) / 2;
        int end = data.size() - 2;
        System.out.println("Endpoint: " + end);
        lineChart.xAxis = new LineChartAxis(new double[]{0, mid, end});
        StatSummary ss = new StatSummary().add(data);
        lineChart.yAxis = new LineChartAxis(new double[]{ss.min(), ss.max()});
        lineChart.title = "Evolution of Fitness";
        lineChart.setXLabel("Iterations").setYLabel("Fitness");
        new JEasyFrame(lineChart, "KL-Based PCG");
    }

    private int distinctValues(int[][] a) {
        StatSummary ss = new StatSummary();
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                ss.add(a[i][j]);
            }
        }
        return 1 + (int) ss.max();
    }

    static boolean wrapAround = false;

    public static ConvNTuple getTrainedConvNTuple(int[][] sample, int mValues) {
        int w = sample.length;
        int h = sample[0].length;

        System.out.println(w + " : " + h);

        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        convNTuple.setMValues(mValues).setStride(stride);

        if (wrapAround)
            convNTuple.makeWrapAroundIndices();
        else
            convNTuple.makeIndices();
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

        if (wrapAround)
            convNTuple.makeWrapAroundIndices();
        else
            convNTuple.makeIndices();
        // convNTuple.report();

        return convNTuple;
    }

    static int[] flatten(int[][] a) {
        int n = a.length * a[0].length;
        int w = a.length;
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = a[i % w][i / w];
        }
        return x;
    }

    // the level view update frequency should be tuned in order
    // to provide an animated view of the evolutionary process
    // without slowing down the evolution too much

    // a figure of 5,000 may be reasonable
    static int freq = 5000;
    LevelView levelView;
    JEasyFrame levelFrame;

    @Override
    public void update(EvolutionLogger logger, int[] solution, double fitness) {
        if (logger.nEvals() % freq != 0) return;
        if (levelFrame == null) {
            // set them both up
            levelView = new LevelView(LevelView.toRect(solution, imageWidth, imageHeight));
            levelView.setColorMap(MarioReader.tileColors);
            levelFrame = new JEasyFrame(levelView, "Title");
        }
        levelView.setTiles(solution);
        levelFrame.setTitle(String.format("Evals: %d;\t fitness: %.3f", logger.nEvals(), fitness));
    }
}

