package cma.noisyopt;

import evodef.AnnotatedFitnessSpace;
import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;
import utilities.StatSummary;

import java.util.Arrays;

public class IObjectiveDiscreteAdapter implements IObjectiveFunction {

    AnnotatedFitnessSpace fitnessSpace;
    boolean useFeasibility = true;
    StatSummary trials = new StatSummary("CMA Trials");

    public IObjectiveDiscreteAdapter setFitnessSpace(AnnotatedFitnessSpace fitnessSpace) {
        this.fitnessSpace = fitnessSpace;
        return this;
    }

    public static boolean verbose = false;

    @Override
    public double valueOf(double[] x) {

        // set each one up

        // System.out.println("Evaluting: " + Arrays.toString(x));
        int[] solution = discretise(x);
        double fitness = fitnessSpace.evaluate(solution);
        trials.add(fitness);

        if (verbose) {
            System.out.println("Solution: " + Arrays.toString(solution));
            System.out.println(trials);
        }

        return -fitness;
    }

    public int[] discretise(double[] x) {
        if (!isFeasible(x)) throw new RuntimeException("Value outside range");
        int[] solution = new int[fitnessSpace.nDims()];

        // go through each one, simply selecting the value based on the value of each element
        // spread evenly over the possible choices

        for (int i=0; i<x.length; i++) {
            int ix = (int) (x[i] * fitnessSpace.nValues(i));
            solution[i] = ix;
        }
        return solution;

    }

    @Override
    public boolean isFeasible(double[] x) {
        if (!useFeasibility) return true;
        for (double v : x) {
            if (v < 0 || v >= 1) return false;
        }
        return true;
    }

    public int nDimensions () {
        return fitnessSpace.nDims();
    }
}
