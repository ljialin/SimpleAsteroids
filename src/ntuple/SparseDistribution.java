package ntuple;

import utilities.StatSummary;

import java.util.Arrays;
import java.util.HashMap;

public class SparseDistribution {

    public static void main(String[] args) {

        SparseDistribution p = new SparseDistribution();
        SparseDistribution q = new SparseDistribution();

        p.add(10, 2);
        p.add(20, 5);
        p.add(30, 1);

        q.add(10);
        q.add(20);
        q.add(30);

        System.out.println(klDiv(p, p));
        System.out.println(klDiv(p, q));
        System.out.println();
        System.out.println(klDiv(q, p));
        System.out.println(klDiv(q, q));

        System.out.println();
        System.out.println(klDivSymmetric(p, q));
        System.out.println(klDivSymmetric(q, p));

    }

    double epsilon = 1e-1;

    public HashMap<Double, StatSummary> statMap;
    public HashMap<Double, int[]> valueArrays;
    int tot = 0;

    public SparseDistribution() {
        statMap = new HashMap<>();
        valueArrays = new HashMap<>();
    }

    public SparseDistribution add(double x) {
        add(x, 1);
        return this;
    }

    public SparseDistribution add(double x, double p) {
        StatSummary ss = statMap.get(x);
        if (ss == null) {
            ss = new StatSummary();
            statMap.put(x, ss);
        }
        ss.add(p);
        tot+=p;
        return this;
    }

    public SparseDistribution addValueArray(double x, int[] a) {
        // check that we're doing this correctly i.e. be alert
        // for any collisions
        int[] b = valueArrays.get(x);
        if (b != null && !Arrays.equals(a, b)) {
            System.out.println("Collision between: ");
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
        }
        valueArrays.put(x, a);
        return this;
    }

    public double getProb(Double key) {
        StatSummary ss = statMap.get(key);
        if (ss != null) {
            return epsilon + ss.sum() / tot;
        } else {
            return epsilon;
        }
    }

    public static double klDiv(double p, double q) {
        return p * (Math.log(p / q));
    }

    public static double klDivSymmetric(double p, double q) {
        return p * Math.log(p / q) + q * Math.log(q/p);
    }

    public static double klDivSymmetric(SparseDistribution pDis, SparseDistribution qDis) {
        return klDiv(pDis, qDis) + klDiv(qDis, pDis);
    }

    public static double klDiv(SparseDistribution pDis, SparseDistribution qDis) {
        double tot = 0;
        // iterate only over the values in p
        // since any ones not in p will have a contribution of zero
        for (Double key : pDis.statMap.keySet()) {
            double p = pDis.getProb(key);
            double q = qDis.getProb(key);
            // epsilon is already included in the getProb function
            tot += p * Math.log(p/q);
        }
        return tot;
    }
}