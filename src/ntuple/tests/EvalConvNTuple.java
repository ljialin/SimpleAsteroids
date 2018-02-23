package ntuple.tests;

import evodef.*;
import ntuple.ConvNTuple;

import java.util.Random;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class EvalConvNTuple implements SolutionEvaluator, SearchSpace, FitnessSpace {

    int nDims;
    int m;
    static Random random = new Random();

    EvolutionLogger logger;

    public EvalConvNTuple(int nDims, int m) {
        this.nDims = nDims;
        this.m = m;
        logger = new EvolutionLogger();
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public Double optimalIfKnown() {
        return 0.0;
    }

    ConvNTuple convNTuple;

    public EvalConvNTuple setConvNTuple(ConvNTuple convNTuple) {
        this.convNTuple = convNTuple;
        return this;
    }

    static double epsilon = 1e-60;
    static double noiseLevel = 0.00001;
    @Override
    public double evaluate(int[] x) {
        // keep track of whether it is truly optimal
        double fitness = -convNTuple.getKLDivergence(x, epsilon);
        boolean isOptimal = fitness == 0;
        logger.log(fitness, x, isOptimal);
        return fitness + noiseLevel * random.nextGaussian();
    }

    @Override
    public boolean optimalFound() {
        // return false for the noisy optimisation experiments in order
        // to prevent the optimiser from cheating
        return false;
    }

    @Override
    public SearchSpace searchSpace() {
        return this;
    }

    @Override
    public int nEvals() {
        return logger.nEvals();
    }

    @Override
    public EvolutionLogger logger() {
        return logger;
    }


    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return m;
    }


}
