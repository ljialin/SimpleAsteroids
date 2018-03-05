package ntuple;

import utilities.StatSummary;

import java.util.Arrays;
import java.util.HashMap;

/*

Based on Sparse Distribution, but for Patterns
 */

public class PatternDistribution {

    public static void main(String[] args) {

        PatternDistribution p = new PatternDistribution();
        PatternDistribution q = new PatternDistribution();

        Pattern p1 = new Pattern().setPattern(new int[]{0, 1});
        Pattern p2 = new Pattern().setPattern(new int[]{1, 1});
        Pattern p3 = new Pattern().setPattern(new int[]{1, 1});
        p.add(p1, 2);
        p.add(p2, 5);
        p.add(p3, 1);

        q.add(p1);
        q.add(p2);
        q.add(p2);

        System.out.println(klDiv(p, p));
        System.out.println(klDiv(p, q));
        System.out.println();
        System.out.println(klDiv(q, p));
        System.out.println(klDiv(q, q));

        System.out.println();
        System.out.println(klDivSymmetric(p, q));
        System.out.println(klDivSymmetric(q, p));

    }

    double epsilon = 1e1;

    public HashMap<Pattern, StatSummary> statMap;
    int tot = 0;

    public PatternDistribution() {
        statMap = new HashMap<>();
    }

    public PatternDistribution add(Pattern p) {
        add(p, 1);
        return this;
    }

    public PatternDistribution add(Pattern p, double w) {
        StatSummary ss = statMap.get(p);
        if (ss == null) {
            ss = new StatSummary();
            statMap.put(p, ss);
        }
        ss.add(w);
        tot+=w;
        return this;
    }

    public double getProb(Pattern key) {
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

    public static double klDivSymmetric(PatternDistribution pDis, PatternDistribution qDis) {
        return klDiv(pDis, qDis) + klDiv(qDis, pDis);
    }

    public static double klDiv(PatternDistribution pDis, PatternDistribution qDis) {
        double tot = 0;
        // iterate only over the values in p
        // since any ones not in p will have a contribution of zero
        for (Pattern key : pDis.statMap.keySet()) {
            double p = pDis.getProb(key);
            double q = qDis.getProb(key);
            // epsilon is already included in the getProb function
            tot += p * Math.log(p/q);
        }
        return tot;
    }
}