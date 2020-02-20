package ntuple.tests;

import evodef.EvoAlg;
import evodef.SolutionEvaluator;
import ga.SimpleRMHC;
import ntuple.CompactSlidingGA;
import ntuple.ConvNTuple;
import ntuple.LevelView;
import ntuple.SlidingMeanEDA;
import ntuple.operator.ConvMutator;
import plot.LineChart;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.Arrays;

public class EvolvePatternTest {

    static int imageWidth = 30, imageHeight = 20;

    public static void main(String[] args) {

        ConvNTuple.w = 0.6;

        int nTrials = 3;
        SimpleRMHC evoAlg = new SimpleRMHC();
        // evoAlg.setMutator(new ConvMutator());
        // evoAlg = new SlidingMeanEDA().setHistoryLength(30);
        // evoAlg = new CompactSlidingGA();
        int nEvals = 10000;
        StatSummary results = new StatSummary();
        EvolvePatternTest ept = new EvolvePatternTest();
        for (int i=0; i<nTrials; i++) {

            ElapsedTimer timer = new ElapsedTimer();
            results.add(ept.runTrial(evoAlg, nEvals));

            System.out.println(timer);
        }
    }

    public double runTrial(SimpleRMHC ea, int nEvals) {
        ConvNTuple convNTuple = getTrainedConvNTuple();
        ea.setMutator(new ConvMutator().setConvNTuple(convNTuple));
        int nDims = imageWidth * imageHeight;
        int mValues = 3;
        SolutionEvaluator evaluator = new EvalConvNTuple(nDims, mValues).setConvNTuple(convNTuple);
        int[] solution = ea.runTrial(evaluator, nEvals);
        double fitness = evaluator.evaluate(solution);
        String label = String.format("Fitness: %.6f", fitness);
        System.out.println(label);
        System.out.println(Arrays.toString(solution));
        LevelView.showMaze(solution, imageWidth, imageHeight, label);
        // new JEasyFrame(LineChart.easyPlot(evaluator.logger().fa), "Evolution of Fitness");
        return fitness;
    }

    public static ConvNTuple getTrainedConvNTuple() {
        int w = 4, h = 4;
        int filterWidth = 3, filterHeight = 3;
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        convNTuple.setMValues(4).setStride(1);

        convNTuple.makeWrapAroundIndices();
        convNTuple.reset();

        System.out.println("Address space size: " + convNTuple.addressSpaceSize());
        // System.out.println("Mean of empty summary: " + new StatSummary().mean());

        // now put some random data in to it
        ElapsedTimer timer = new ElapsedTimer();

        // 'x' is the Red Maze example explained here:
        // https://adamsmith.as/papers/wfc_is_constraint_solving_in_the_wild.pdf

        int[] x = {
                0,0,2,0,0,
                0,0,2,1,1,
                1,1,2,1,1,
                0,0,1,0,0,
        };
//        int[] x = {
//                0,0,0,0,
//                0,1,1,1,
//                0,1,2,1,
//                0,1,1,1,
//        };
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
}
