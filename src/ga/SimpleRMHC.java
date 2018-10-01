package ga;

import evodef.*;
import evodef.DefaultMutator;
import evodef.BanditLandscapeModel;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.Random;

public class SimpleRMHC implements EvoAlg {

    // Random mutation hill climber for testing one-max
    static Random random = new Random();

    int[] bestYet;

    int[] seed;
    // SolutionEvaluator evaluator
    SearchSpace searchSpace;

    private int nSamples;

    public SimpleRMHC() {
        this(1);
    }

    public SimpleRMHC(int nSamples) {
        this.nSamples = nSamples;
    }

    public String toString() {
        return String.format("RMHC, r=%d", nSamples);
    }

    public void setSamplingRate(int n) {
        this.nSamples = n;
    }


    // should not be a static, just did it quick and dirty

    // this just adds a noisy test within the algorithm
    // normally set to false
    static boolean noisy = false;

    static double epsilon = 1.0;

    public static boolean accumulateBestYetStats = false;

    // this is only checked if not resampling parent
    public static boolean resampleParent = true;


    public void setInitialSeed(int[] seed) {

        this.seed = seed;
    }

    Mutator mutator;

    public SimpleRMHC setMutator(Mutator mutator) {
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
        StatSummary fitBest = fitness(evaluator, bestYet, new StatSummary());

        // create a mutator if it has not already been made
        if (mutator == null)
            mutator = new DefaultMutator(searchSpace);
        else
            mutator.setSearchSpace(searchSpace);

        while (evaluator.nEvals() < maxEvals && !evaluator.optimalFound()) {
            // System.out.println("nEvals: " + evaluator.nEvals());
            int[] mut = mutator.randMut(bestYet);
            // int[] mut = randMutAll(bestYet);
            // int[] mut = randAll(bestYet);

            int oneBits = 0;
            for (int x:bestYet)
                oneBits+=x;
            if (oneBits == 10 && TestFHT.foundOpt == false) {
                TestFHT.foundOpt = true;
                // System.out.println("Stumbled on opt: " + Arrays.toString(a));
            }


            // keep track of how much we want to mutate this
            int prevEvals = evaluator.nEvals();
            StatSummary fitMut = fitness(evaluator, mut, new StatSummary());
            if (accumulateBestYetStats) {
                fitBest = fitness(evaluator, bestYet, fitBest);

            } else {
                if (resampleParent) {
                    fitBest = fitness(evaluator, bestYet, new StatSummary());
                }
            }
            // System.out.println(fitBest.mean() + " : " + fitMut.mean() + " : " + (fitBest.mean() > fitMut.mean()) );
            if (fitMut.mean() >= fitBest.mean()) {
                // System.out.println("Updating best");
                bestYet = mut;
                fitBest = fitMut;



                evaluator.logger().keepBest(mut, fitMut.mean());


                // now check whether it is better than optimal by epsilon
                // this is for noisy optimisation only

                if (noisy) {
                    Double opt = evaluator.optimalIfKnown();
                    if (opt != null) {
                        if (fitMut.mean() >= opt + epsilon) {
                            return bestYet;
                        }
                    }
                }
            }


            int evalDiff = evaluator.nEvals() - prevEvals;
            for (int i = 0; i < evalDiff; i++) {
                evaluator.logger().logBestYest(bestYet);
            }

        }
        // System.out.println("Ran for: " + evaluator.nEvals());
        // System.out.println("Sampling rate: " + nSamples);
        return bestYet;
    }

    BanditLandscapeModel model;

    @Override
    public void setModel(BanditLandscapeModel nTupleSystem) {
        this.model = nTupleSystem;
    }

    @Override
    public BanditLandscapeModel getModel() {
        return model;
    }

    @Override
    public EvolutionLogger getLogger() {
        return evaluator.logger();
    }

    StatSummary fitness(SolutionEvaluator evaluator, int[] sol, StatSummary ss) {
        for (int i = 0; i < nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            // System.out.println((int) fitness + "\t " + Arrays.toString(sol));
            ss.add(fitness);
        }
        if (model != null) {
            // System.out.println("Added summary");
            // model.addSummary(sol, ss);
        }
        return ss;
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


