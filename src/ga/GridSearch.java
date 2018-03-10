package ga;

import evodef.*;
import evodef.BanditLandscapeModel;
import utilities.Picker;
import utilities.StatSummary;

import java.util.Arrays;

public class GridSearch implements EvoAlg {

    public static void main(String[] args) {

    }

    @Override
    public void setInitialSeed(int[] seed) {

    }

    SearchSpace searchSpace;
    EvolutionLogger logger;

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {

        searchSpace = evaluator.searchSpace();
        logger = new EvolutionLogger();

        double searchSpaceSize = SearchSpaceUtil.size(searchSpace);
        double samplesPerPoint = nEvals / searchSpaceSize;
        System.out.println("Search space size = " + searchSpaceSize);
        System.out.println("Evaluation budget = " + nEvals);
        System.out.format("Samples per point  = %.2f\n", samplesPerPoint);

        if (samplesPerPoint < 1)
            throw new RuntimeException("Cannot run GridSearch with less than 1 sample per point");

        // otherwise we're ok to proceed ...
        // the idea is that we'll search each point for as much as we have budget
        // but do this in a breadth-first way

        // and we'll record each one in a StatSummary object - so need an array of these
        StatSummary[] results = new StatSummary[(int) searchSpaceSize];
        for (int i=0; i<results.length; i++) {
            results[i] = new StatSummary();
        }

        for (int i=0; i<nEvals; i++) {
            // each time pick the next point in the search space
            // evaluate it, and add it to the stats
            int index = i % (int) searchSpaceSize;
            int[] solution = SearchSpaceUtil.nthPoint(searchSpace, index);
            double fitness = evaluator.evaluate(solution);
            results[index].add(fitness);
        }

        //

        // iterate over the points to find the best one

        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<results.length; i++) {
            picker.add(results[i].mean(), i);
        }

        int best = picker.getBest();
        System.out.println("Best index = " + best);
        System.out.println("Best stats: " + results[best]);
        int[] solution = SearchSpaceUtil.nthPoint(searchSpace, best);
        System.out.println("Best solution: " + Arrays.toString(solution));

        return solution;
    }

    @Override
    public void setModel(BanditLandscapeModel nTupleSystem) {

    }

    @Override
    public BanditLandscapeModel getModel() {
        return null;
    }

    @Override
    public EvolutionLogger getLogger() {
        return logger;
    }

    @Override
    public void setSamplingRate(int samplingRate) {

        // may comment this out if the exception is too inconvenient
        throw new RuntimeException("Sampling rate not supported for Grid Search");
    }
}
