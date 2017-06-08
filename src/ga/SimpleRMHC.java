package ga;

import evodef.*;
import evogame.Mutator;
import evomaze.MazeView;
import evomaze.ShortestPathTest;
import ntuple.NTupleSystem;
import utilities.ElapsedTimer;
import utilities.StatSummary;

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

    // should not be a static, just did it quick and dirty

    // this just adds a noisy test within the algorithm
    // normally set to false
    static boolean noisy = false;

    static double epsilon = 1.0;

    static boolean accumulateBestYetStats = false;

    // this is only checked if not resampling parent
    static boolean resampleParent = true;


    public void setInitialSeed(int[] seed) {

        this.seed = seed;
    }


    /**
     *
     * @param evaluator
     * @param maxEvals
     * @return: the solution coded as an array of int
     */
    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int maxEvals) {
        init(evaluator);
        StatSummary fitBest = fitness(evaluator, bestYet, new StatSummary());
        Mutator mutator = new Mutator(searchSpace);



        while (evaluator.nEvals() < maxEvals && !evaluator.optimalFound()) {
            // System.out.println("nEvals: " + evaluator.nEvals());
            int[] mut = mutator.randMut(bestYet);
            // int[] mut = randMutAll(bestYet);
            // int[] mut = randAll(bestYet);
            StatSummary fitMut = fitness(evaluator, mut, new StatSummary());
            if (accumulateBestYetStats) {
                fitBest = fitness(evaluator, bestYet, fitBest);

            } else {
                if (resampleParent) {
                    fitBest = fitness(evaluator, bestYet, new StatSummary());
                }
            }
            // System.out.println(fitBest.mean() + " : " + fitMut.mean());
            if ( fitMut.mean() >= fitBest.mean()) {
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
        }
        // System.out.println("Ran for: " + evaluator.nEvals());
        return bestYet;
    }

    NTupleSystem model;
    @Override
    public void setModel(NTupleSystem nTupleSystem) {
        this.model = nTupleSystem;
    }

    @Override
    public NTupleSystem getModel() {
        return model;
    }

    @Override
    public EvolutionLogger getLogger() {
        return evaluator.logger();
    }

    StatSummary fitness(SolutionEvaluator evaluator, int[] sol, StatSummary ss) {
        for (int i=0; i<nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            ss.add(fitness);

        }
        if (model != null) {
            // System.out.println("Added summary");
            model.addSummary(sol, ss);
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
