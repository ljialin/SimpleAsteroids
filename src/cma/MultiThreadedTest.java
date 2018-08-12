package cma;

import fr.inria.optimization.cmaes.fitness.IObjectiveFunction;

public class MultiThreadedTest {
    public static void main(String[] args) throws InterruptedException {
        // set up the problem
        IObjectiveFunction fun = new QuadraticMultiBowl();
        int maxEvals = 10000;
        int nDim = 30;
        // create the solvers
        PrioritySwitch control = new PrioritySwitch(maxEvals);
        SwitchWrapper sw1 = new SwitchWrapper(fun, control);
        SwitchWrapper sw2 = new SwitchWrapper(fun, control);
        CMASolver s1 = new CMASolver(sw1, nDim, maxEvals);
        CMASolver s2 = new CMASolver(sw2, nDim, maxEvals);
        s1.setInitialX(0.0);
        s2.setInitialX(1.0);

        control.add(sw1);
        control.add(sw2);
        CMAThread t1 = new CMAThread(s1);
        CMAThread t2 = new CMAThread(s2);

        control.start();
        System.out.println("Waiting to join");
        control.join();
        System.out.println("Joined");


    }



}
