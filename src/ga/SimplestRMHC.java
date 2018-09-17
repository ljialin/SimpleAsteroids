package ga;

import evodef.*;
import utilities.StatSummary;

import java.util.Random;

public class SimplestRMHC implements EvoAlg {

    int[] bestYet;

    int[] seed;
    // SolutionEvaluator evaluator
    SearchSpace searchSpace;

    public String toString() {
        return "SimplestRMHC";
    }

    public void setSamplingRate(int n) {
        System.out.println("Operation not supported in SimplestRMHC");
    }

    public void setInitialSeed(int[] seed) {
        this.seed = seed;
    }

    Mutator mutator;

    public SimplestRMHC setMutator(Mutator mutator) {
        this.mutator = mutator;
        return this;
    }

    /**
     * @param evaluator
     * @param maxEvals
     * @return: the solution coded as an array of int
     */
    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int maxEvals) {
        init(evaluator);
        double fitBest = evaluator.evaluate(bestYet);

        // create a mutator if it has not already been made
        if (mutator == null)
            mutator = new DefaultMutator(searchSpace);
        else
            mutator.setSearchSpace(searchSpace);

        while (evaluator.nEvals() < maxEvals && !evaluator.optimalFound()) {
            int[] mut = mutator.randMut(bestYet);

            double fitMut = evaluator.evaluate(mut);
            int diff = DefaultMutator.diffHamming(bestYet, mut);
//            System.out.println(fitBest + " : " + fitMut + " : " + (fitBest > fitMut) + "\t " + diff);
//            System.out.println(searchSpace.nDims() + "\t " + searchSpace.nValues(0));
//            System.out.println(evaluator);
            if (fitMut >= fitBest) {
                // System.out.println("Updating best");
                bestYet = mut;
                fitBest = fitMut;
                evaluator.logger().keepBest(mut, fitMut);
            }
            evaluator.logger().logBestYest(bestYet);
        }
        // System.out.println("Ran for: " + evaluator.nEvals());
        // System.out.println("Sampling rate: " + nSamples);
        return bestYet;
    }

    @Override
    public void setModel(BanditLandscapeModel nTupleSystem) {
        System.out.println("Operation not supported in SimplestRMHC");
    }

    @Override
    public BanditLandscapeModel getModel() {
        return null;
    }

    @Override
    public EvolutionLogger getLogger() {
        return evaluator.logger();
    }

    SolutionEvaluator evaluator;

    private void init(SolutionEvaluator evaluator) {
        this.evaluator = evaluator;
        this.searchSpace = evaluator.searchSpace();
        if (seed == null) {
            bestYet = SearchSpaceUtil.randomPoint(searchSpace);
        } else {
            bestYet = SearchSpaceUtil.copyPoint(seed);
        }
    }
}


