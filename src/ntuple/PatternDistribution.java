package ntuple;

import utilities.StatSummary;

import java.util.*;

/*

Based on Sparse Distribution, but for Patterns
 */

public class PatternDistribution {

    // set this high in order to weight
    // for inclusive use of patterns
    // zero to weight against it
    // moved this to a parameter
    // public static double pw = 1.0;

    public static void main(String[] args) {

        PatternDistribution p = new PatternDistribution();
        PatternDistribution q = new PatternDistribution();

        Pattern p1 = new Pattern().setPattern(new int[]{0, 1});
        Pattern p2 = new Pattern().setPattern(new int[]{1, 1});
        Pattern p3 = new Pattern().setPattern(new int[]{3, 1});
        Pattern p4 = new Pattern().setPattern(new int[]{1, 2});
        p.add(p1, 2);
        p.add(p2, 2);
        p.add(p3, 1);

        q.add(p1);
        q.add(p2);
        q.add(p2);
        q.add(p4, 1);

        System.out.println(klDiv(p, p));
        System.out.println(klDiv(p, q));
        System.out.println();
        System.out.println(klDiv(q, p));
        System.out.println(klDiv(q, q));

        System.out.println();
        System.out.println(klDivSymmetric(p, q));
        System.out.println(klDivSymmetric(q, p));

        System.out.println();
        System.out.println(jointEntropy(p, q));
        System.out.println(jointEntropy(q, p));

        System.out.println();
        System.out.println(infDist(p, q));
        System.out.println(infDist(q, q));

    }

    // experiment with various values
    double epsilon = 1e-5;

    public HashMap<Pattern, StatSummary> statMap;
    int tot = 0;

    public PatternDistribution() {
        statMap = new HashMap<>();
    }

    public PatternDistribution add(Pattern p) {
        add(p, 1);
        return this;
    }

    public ArrayList<PatternCount> getFrequencyList() {
        ArrayList<PatternCount> list = new ArrayList<>();
        for (Map.Entry<Pattern, StatSummary> pair : statMap.entrySet()) {
            list.add(new PatternCount(pair.getKey(), pair.getValue().n()));
        }
        Collections.sort(list);
        return list;
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
            return (epsilon + ss.sum()) / (tot * (1+epsilon));
        } else {
            return (epsilon / ((tot +epsilon)*(1+epsilon))) ;
        }
    }

    public double getProbOld(Pattern key) {
        StatSummary ss = statMap.get(key);
        if (ss != null) {
            return epsilon + ss.sum() / tot;
        } else {
            return epsilon;
        }
    }

    public double getRawProb(Pattern key) {
        // no epsilon safety
        StatSummary ss = statMap.get(key);
        if (ss != null) {
            return ss.sum() / tot;
        } else {
            return 0.0;
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

    public static double klDivWeighted(PatternDistribution pDis, PatternDistribution qDis, double w) {
        return w * klDiv(pDis, qDis) + (1-w) * klDiv(qDis, pDis);
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

    public static double jointEntropy(PatternDistribution pDis, PatternDistribution qDis) {
        // since if one of the entries is zero, the product is zero
        double tot = 0;
        for (Pattern key : pDis.statMap.keySet()) {
            double p = pDis.getRawProb(key);
            double q = qDis.getRawProb(key);
            // epsilon is already included in the getProb function
            double pq = p * q;
            if (pq > 0) {
                tot += pq * Math.log(pq);
            }
        }
        return tot;
    }

    public static double infDist(PatternDistribution pDis, PatternDistribution qDis) {
        // since if one of the entries is zero, the product is zero
        double xyH = 0;  // joint entropy
        double xHy = 0;  // entropy of X given Y
        double yHx = 0;  // entropy of Y given X
        for (Pattern key : pDis.statMap.keySet()) {
            double p = pDis.getRawProb(key);
            double q = qDis.getRawProb(key);
            // epsilon is already included in the getProb function
            double pq = p * q;
            if (pq > 0) {
                // means both p and q must be non-zero
                xyH += pq * Math.log(pq);
                xHy += pq * Math.log(pq / q);
                yHx += pq * Math.log(pq / p);
            }
        }
        System.out.println(xyH + "\t " + xHy + "\t " + yHx);
        if (xyH == 0) return 0;
        return (xHy + yHx) / xyH;
    }
}
