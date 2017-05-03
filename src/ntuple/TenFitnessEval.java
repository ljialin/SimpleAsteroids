package ntuple;

import evodef.EvolutionLogger;
import evodef.SearchSpace;
import evodef.SolutionEvaluator;

import java.util.Random;

/**
 * Created by sml on 17/11/2016.
 */
public class TenFitnessEval extends TenSpace implements SolutionEvaluator{
    static Random random = new Random();
    static double noise = 0.1;
    public TenFitnessEval(int nDims) {
        super(nDims);
        if (nDims < 5) throw new RuntimeException("Number of dimensions less than 5: " + nDims);
        reset();
    }
    EvolutionLogger logger;

    @Override
    public void reset() {
        logger = new EvolutionLogger();
    }

    @Override
    public double evaluate(int[] p) {
//        for (int i=0; i<bits.length; i++) {
//            // if (bits)
//            // should do a sanity check, but can't be bothered...
//        }
        // System.out.println("Evaluating");
        double fitness = p[0] * p[1] - 2 * p[2] * p[3] + p[3] * p[4] + 10 * p[2] + random.nextGaussian() * noise;
        logger.log(fitness, p, false);
        return fitness;
    }


    @Override
    public Double optimalIfKnown() {
        return null;
    }

    @Override
    public boolean optimalFound() {
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
}
