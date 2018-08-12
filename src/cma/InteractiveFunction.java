package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

import java.util.Arrays;
import java.util.Scanner;

public class InteractiveFunction implements IObjectiveFunction {
    // changing floor will change the reason for termination
    // (in conjunction with the target value)
    // see cma.options.stopFitness
    static double floor = 0.0;
    @Override
    public double valueOf(double[] x) {
        System.out.println(Arrays.toString(x));
        Scanner scanner = new Scanner(System.in);
        double val = scanner.nextDouble();
        return val;
    }

    @Override
    public boolean isFeasible(double[] x) {
        return true;
    }
}
