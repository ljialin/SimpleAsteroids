package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

import java.util.concurrent.Semaphore;

public class CMAServer implements IObjectiveFunction {

    Semaphore vectorLock;
    Semaphore valueLock;
    // boolean isVectorReady;
    double[] x;
    Double fitness;

    public CMAServer() {
        vectorLock = new Semaphore(0);
        // vectorLock.acquire();
        valueLock = new Semaphore(0);
        x = null;
        fitness = null;
    }

    public void returnFitness(double fitness) {
        this.fitness = fitness;
        valueLock.release();
    }

    public double[] getNext() {
        if (x == null) {
            try {
                System.out.println("Aquiring vector lock");
                vectorLock.acquire();
                System.out.println("Got vector lock, x = " + x);
            } catch (Exception e) { e.printStackTrace(); }
        }
        // by the time the lock is released x will be non-null

        double[] ret = x;
        x = null;
        return ret;
    }

    public double[] bestVec() {
        return new double[0];
    }

    public double bestScore() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double valueOf(double[] x) {

        // set up the x vector and release the vector lock
        this.x = x;
        vectorLock.release();
        System.out.println("Released vector lock");
        if (fitness == null) {
            try {
                System.out.println("Acquiring value lock");
                valueLock.acquire();
                System.out.println("Got value lock, fitness = " + fitness);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Double val = fitness;
        fitness = null;
        return val;
    }

    @Override
    public boolean isFeasible(double[] x) {
        return true;
    }
}
