package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public class SingleThreadedTest {
    public static void main(String[] args) throws InterruptedException {
        // set up the problem
        IObjectiveFunction fun = new QuadraticMultiBowl();
        int maxEvals = 10000;
        int nDim = 10;
        // create the solvers
        PrioritySwitch control = new PrioritySwitch(maxEvals);
        SwitchWrapper sw1 = new SwitchWrapper(fun, control);
        CMASolver s1 = new CMASolver(sw1, nDim, maxEvals);
        s1.setInitialX(1.0);

        control.add(sw1);
        CMAThread t1 = new CMAThread(s1);
        control.start();
        System.out.println("Waiting to join");
        control.join();
        System.out.println("Joined");
    }
}
