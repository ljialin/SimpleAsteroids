package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;
import utilities.StatSummary;

import java.util.concurrent.Semaphore;

public class SwitchWrapper implements IObjectiveFunction {
    IObjectiveFunction fun;
    PrioritySwitch control;
    StatSummary ss;
    Semaphore lock;
    Double value;
    static int counter = 0;
    int id;
    boolean isReady;

    public SwitchWrapper(IObjectiveFunction fun, PrioritySwitch control) {
        this.fun = fun;
        this.control = control;
        ss = new StatSummary();
        lock = new Semaphore(1);
        id = counter++;
    }

    public Double peek() {
        return value;
    }

    @Override
    public double valueOf(double[] x) {
        // test whether the value is ready to be collected
        // if it is, then we need to wait until it has been
        if (isReady) {
            try {
                lock.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // evaluate the function at point x, store the value
        // add it to the statistical summary and then return it
        value = fun.valueOf(x);
        ss.add(value);
        isReady = true;
        // System.out.format("%d arm, %d pulls, returning: %.2f\n", id, ss.n(), value);
        try {
            // System.in.read();
        }   catch (Exception e) {}
        return value;
    }

    public double get() {
        isReady = false;
        lock.release();
        return value;
    }

    @Override
    public boolean isFeasible(double[] x) {
        return true;
    }

    public String toString() {
        return id + " : " + ss.n() + " : " + ss.min() + " : " + ss.mean() + " : " + isReady;
    }
}
