package evodef;

import ntuple.NTupleSystem;

/**
 * Created by sml on 16/08/2016.
 */
public interface EvoAlg {

    // seed the algorithm with a specified point in the search space
    void setInitialSeed(int[] seed);
    int[] runTrial(SolutionEvaluator evaluator, int nEvals);
    void setModel(NTupleSystem nTupleSystem);
    NTupleSystem getModel();

    EvolutionLogger getLogger();

}

