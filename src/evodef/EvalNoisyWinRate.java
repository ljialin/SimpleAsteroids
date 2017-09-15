package evodef;

import java.util.Random;

/**
 * Created by simonmarklucas on 08/01/2017.
 *
 *  Idea is to use this to formulate a win rate.
 *
 *  Interpret each point in the search space as
 *  a number in the appropriate base.
 *
 *  Take the value as a proportion of its maximum.
 **
 *  This value is the win probability, and what the search algorithm will try to
 *  optimise.
 *
 *  However, in noisy mode the result of a biased coin flip is returned (i.e. either 1 or 0)
 *  If the noise is zero, then the win probability is returned.
 *
 *  This enables us to truly evaluate i.e. to compare the noisy ranking with the true one.
 *
 *
 */
public class EvalNoisyWinRate implements NoisySolutionEvaluator, SearchSpace, FitnessSpace {


    // setting to true aims for a 50% win rate
    // setting to false aims for a 100% win-rate

    // this idea is not currently used

    // public static boolean aimForEvenRate = true;

    public static void main(String[] args) {
        // first of all test the main one

        EvalNoisyWinRate eval = new EvalNoisyWinRate(5, 2);

        int[] a1 = {1, 1, 0, 1, 0};
        int[] a2 = {1, 1, 1, 1, 1};

        eval.evaluate(a1);
        eval.logger().report();
        System.out.println();

        eval.evaluate(a2);
        eval.evaluate(a1);
        eval.evaluate(a1);
        eval.evaluate(a1);
        eval.evaluate(a1);

        eval.logger().report();


    }


    int nDims;
    int m;

    double maxVal;


    double noise = 0.0;
    static Random random = new Random();


    EvolutionLogger logger;
    int nOptimal;


    public EvalNoisyWinRate(int nDims, int m) {
        this(nDims, m, 0);
    }

    public EvalNoisyWinRate(int nDims, int m, double noise) {
        this.nDims = nDims;
        this.m = m;
        this.noise = noise;
        logger = new EvolutionLogger();

        // calculate max possible value
        maxVal = Math.pow(m, nDims) - 1;
        // System.out.println("Max val = " + maxVal);

    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public Double optimalIfKnown() {

        // this is not correct, but not currently use
        return (double) nDims;
    }


    @Override
    public double evaluate(int[] a) {
        boolean isOptimal = isOptimal(a);
        double pVal = pVal(a);

        double fitnessValue;

        if (noise == 0) {
            fitnessValue = pVal;
        } else {
            fitnessValue = random.nextDouble() <= pVal ? 1 : 0;
        }

        logger.log(fitnessValue, a, isOptimal);
        return fitnessValue;
    }

    public double pVal(int[] a) {
        double tot = 0;
        int pow = 1;
        for (int i=0; i<a.length; i++) {
            if (a[i] <0 || a[i] >= m) {
                throw new RuntimeException("Value out of bounds: " + a[i]);
            }
            tot += a[i] * pow;
            pow *= m;
        }
        // keep track of whether it is truly optimal

        boolean isOptimal = tot == maxVal;

        // System.out.println(tot + " : " + maxVal + " : " + isOptimal);

        double pVal = tot / maxVal;
        return pVal;

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
        return m;
    }

    @Override
    public Boolean isOptimal(int[] solution) {
        for (int i : solution) {
            if (i != m-1) return false;
        }
        return true;
    }

    @Override
    public Double trueFitness(int[] solution) {
        // true fitness is the proprtion of the max value
        return pVal(solution);
    }
}
