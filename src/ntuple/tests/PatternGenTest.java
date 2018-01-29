package ntuple.tests;

import ntuple.ConvNTuple;
import utilities.ElapsedTimer;

import java.util.Arrays;
import java.util.Random;

public class PatternGenTest {
    public static void main(String[] args) {
        int w = 4, h = 4;
        int filterWidth = 2, filterHeight = 2;
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        convNTuple.setMValues(3).setStride(1);

        convNTuple.makeWrapAroundIndices();

        System.out.println("Address space size: " + convNTuple.addressSpaceSize());
        // System.out.println("Mean of empty summary: " + new StatSummary().mean());

        // now put some random data in to it
        ElapsedTimer timer = new ElapsedTimer();

        // 'x' is the Red Maze example explained here:
        // https://adamsmith.as/papers/wfc_is_constraint_solving_in_the_wild.pdf

        int[] x = {
                0,0,0,0,
                0,1,1,1,
                0,1,2,1,
                0,1,1,1,
        };

        // we'll use 'y' to probe the n-tuple for mean and novelty estimates
        int[] y = {
                0,0,0,0,
                0,0,1,1,
                0,1,2,1,
                0,1,1,1,
        };
        // give this a value of 1

        int[][] tests = {x, y};

        convNTuple.addPoint(x, 1);

        // now iterate over all the values in there
        System.out.println("Training finished: ");
        System.out.println(timer);

        convNTuple.report();
        System.out.println(timer);

        double epsilon = 1e-20;
        for (int[] test : tests) {
            System.out.println("Probe:  \t " + Arrays.toString(test));
//            System.out.println("Mean:   \t " + convNTuple.getMeanEstimate(test));
//            System.out.println("Explore:   \t " + convNTuple.getExplorationEstimate(test));
//            System.out.println("Stats:\t " + convNTuple.getNoveltyStats(test));
            System.out.println("pFit:\t " + convNTuple.getKLDivergence(test, epsilon));
            System.out.println();
        }
    }
}
