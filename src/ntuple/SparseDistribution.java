package ntuple;

import utilities.StatSummary;

import java.util.HashMap;

public class SparseDistribution {

    double epsilon = 1e-20;

    HashMap<Integer, StatSummary> statMap;
    int tot = 0;

    public SparseDistribution() {
        statMap = new HashMap<>();
    }

    public SparseDistribution add(int x) {
        StatSummary ss = statMap.get(x);
        if (ss == null) {
            ss = new StatSummary();
            statMap.put(x, ss);
        }
        ss.add(1);
        tot++;
        return this;
    }

    public double getProb(Integer key) {
        StatSummary ss = statMap.get(key);
        if (ss != null) {
            return ss.n() / tot;
        } else {
            return epsilon;
        }
    }


    public static double klDiv(SparseDistribution pDis, SparseDistribution qDis) {
        double tot = 0;
        // iterate only over the values in p
        // since any ones not in p will have a contribution of zero
        for (Integer key : pDis.statMap.keySet()) {
            double p = pDis.getProb(key);
            double q = qDis.getProb(key);
            // epsilon is already included in the getProb function
            tot += p * Math.log(p/q);
        }
        return tot;
    }

}
