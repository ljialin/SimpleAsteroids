package evodef;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class EvalMaxSat implements SolutionEvaluator, SearchSpace {

    int nDims;

    // m should always be two for a binary problem, which this is
    // fundamentally
    int m;

    EvolutionLogger logger;

    public EvalMaxSat(int nDims, int m) {
        this.nDims = nDims;
        this.m = m;
        logger = new EvolutionLogger();
    }

    @Override
    public Double optimalIfKnown() {
        return null;
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public double evaluate(int[] a) {
        int tot = 0;
        for (int i=0; i<a.length; i++) {
            if (a[i] <0 || a[i] >= m) {
                throw new RuntimeException("Value out of bounds: " + a[i]);
            }
            tot += a[i];
        }
        logger.log(tot, a, false);
        return tot;
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

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return 2;
    }
}
