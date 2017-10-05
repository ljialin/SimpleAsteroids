package evodef;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class EvalMaxM implements NoisySolutionEvaluator, SearchSpace {

    int nDims;
    int m;

    double noise = 0.0;
    static Random random = new Random();

    EvolutionLogger logger;
    int nOptimal;


    public EvalMaxM(int nDims, int m) {
        this(nDims, m, 0);
    }

    public EvalMaxM(int nDims, int m, double noise) {
        this.nDims = nDims;
        this.m = m;
        this.noise = noise;
        logger = new EvolutionLogger();
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public Double optimalIfKnown() {
        return (double) nDims * (m-1);
    }


    @Override
    public double evaluate(int[] a) {
        // keep track of whether it is truly optimal
        double tot = trueFitness(a);

        if (tot >= a.length && TestFHT.foundOpt == false) {
            TestFHT.foundOpt = true;
            // System.out.println("Stumbled on opt: " + Arrays.toString(a));
        }
        boolean isOptimal = isOptimal(a);
        tot += noise * random.nextGaussian();

        logger.log(tot, a, isOptimal);
        return tot;
    }

    @Override
    public Double trueFitness(int[] a) {
        double tot = 0;
        for (int i=0; i<a.length; i++) {
            if (a[i] <0 || a[i] >= m) {
                throw new RuntimeException("Value out of bounds: " + a[i]);
            }
            tot += a[i];
        }
        return tot; //  / optimalIfKnown();
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

    @Override
    public Boolean isOptimal(int[] solution) {
        return trueFitness(solution) == solution.length * (m-1);
    }

}
