package cma;

import utilities.Picker;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PrioritySwitch extends Thread {
    List<SwitchWrapper> arms;
    int maxPulls = 100;

    public PrioritySwitch(int maxPulls) {
        this.maxPulls = maxPulls;
        arms = new ArrayList<SwitchWrapper>();
    }

    public void add(SwitchWrapper arm) {
        arms.add(arm);
    }

    public void run() {
        int N = 1;
        while (N <= maxPulls) {
            // each time use a Picker to select the best arm
            Picker<SwitchWrapper> picker = new Picker<SwitchWrapper>(Picker.MAX_FIRST);
            // System.out.println("Checking arms");
            for (SwitchWrapper arm : arms) {
                if (arm.isReady) {
                    double u = ucb(arm.peek(), arm.ss.n(), N);
//                    System.out.println("Ready arm: " + arm.id);
//                    System.out.format("%d\t UCT: %.4f\n\n", N, u);
                    picker.add(u, arm);
                }
            }
            if (picker.nItems != arms.size()) {
//                System.out.println("Not ready yet");
//                for (SwitchWrapper arm : arms) {
//                    System.out.println(arm);
//                }
//                System.out.println();
                sleep(1);
            } else {
                SwitchWrapper arm = picker.getBest();
                if (arm != null) {
                    // pull the selected arm
                    double x = arm.get();
//                System.out.println("Selected arm: " + arm.id);
//                System.out.format("%d\t %.4f\n\n", N, x);

                    N++;
                    // System.out.println("N = " + N);
                }
                // System.out.println(arms);
            }

        }
        System.out.println("Run terminated");
        System.out.println(arms);
        System.exit(0);
    }


    static double C = 10;
    static double eps = 1e-12;

    public static double ucb(double x, double ni, double N) {
        return -x + C * Math.sqrt(Math.log(N) / (eps + ni));
    }


    void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
