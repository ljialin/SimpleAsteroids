package evodef;

import java.util.Random;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class Eval2DNonLinear implements NoisySolutionEvaluator, SearchSpace , FitnessSpace {

    public static void main(String[] args) {
        Eval2DNonLinear problem = new Eval2DNonLinear(5, 0);
        System.out.println(problem.optimalIfKnown());
    }

    int nDims = 2;
    int m;

    double noise = 0.0;
    static Random random = new Random();

    EvolutionLogger logger;
    int nOptimal;


    public Eval2DNonLinear(int m) {
        this(m, 0);
    }

    public Eval2DNonLinear(int m, double noise) {
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
        return f(0, m-1);
    }


    @Override
    public double evaluate(int[] a) {
        // keep track of whether it is truly optimal
        double tot = trueFitness(a);
        boolean isOptimal = isOptimal(a);
        tot += noise * random.nextGaussian();

        logger.log(tot, a, isOptimal);
        return tot;
    }

    boolean trap = true;

    @Override
    public Double trueFitness(int[] a) {
        double x = a[0];
        double y = a[1];
        return f(x,y) / optimalIfKnown();
    }

    public double f(double x, double y)
    {
        if (false && trap && x == m-1 && y == m-1 ) {
            return m;
        } else if (trap && x == 0 && y == 0) {
            return 2 * m;
        }
        else if (trap && (x == 0) && (y == 0)) {
            return 0;
        }
        else  {
            return x + y;
        }
//         return x + 2 * y - x * y;
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
        // return trueFitness(solution) == solution.length * (m-1);
        return trueFitness(solution) == 1.0 ; // solution.length * (m-1);
    }

}
