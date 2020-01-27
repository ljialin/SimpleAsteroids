package distance.pattern;

import distance.kl.JSD;
import distance.kl.KLDiv;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*

Based on Sparse Distribution, but for Patterns
 */

public class PatternDistribution {

    public static void main(String[] args) {

        PatternDistribution p = new PatternDistribution();
        PatternDistribution q = new PatternDistribution();

        Pattern p1 = new Pattern().setPattern(new int[]{0, 1});
        Pattern p2 = new Pattern().setPattern(new int[]{1, 1});
        Pattern p3 = new Pattern().setPattern(new int[]{3, 1});
        Pattern p4 = new Pattern().setPattern(new int[]{1, 2});

        Pattern p5 = new Pattern().setPattern(new int[]{3, 3});
        Pattern p6 = new Pattern().setPattern(new int[]{3, 4});



        p.add(p1, 1);
        p.add(p2, 1);
        p.add(p3, 1);
        p.add(p5, 1);
        p.add(p6, 1);


//        q.add(p1);
//        q.add(p2);
//        q.add(p2);
        q.add(p4, 1);

        System.out.println(KLDiv.klDiv(p, p));
        System.out.println(KLDiv.klDiv(p, q));
        System.out.println();
        System.out.println(KLDiv.klDiv(q, p));
        System.out.println(KLDiv.klDiv(q, q));

        System.out.println();
        System.out.println(KLDiv.klDivSymmetric(p, q));
        System.out.println(KLDiv.klDivSymmetric(q, p));


        System.out.println();
        System.out.println("p:");
        p.printReport();

        System.out.println("q:");
        q.printReport();


        System.out.println();
        System.out.println("w:");
        PatternDistribution w = new PatternDistribution();
        w.add(p);
        w.add(q);
        w.printReport();
        System.out.println();
        System.out.println("JSD: " + new JSD().div( p, q, 0.5  ));
    }

    // double epsilon = 1e1;
    double epsilon = 1e-5;

    public HashMap<Pattern, StatSummary> statMap;
    public int tot = 0;

    public PatternDistribution() {
        statMap = new HashMap<>();
    }

    public PatternDistribution add(Pattern p) {
        add(p, 1);
        return this;
    }

    public PatternDistribution add(PatternDistribution pd) {
        for (Map.Entry<Pattern, StatSummary> pair : pd.statMap.entrySet()) {
            // add(pair.getKey(), pair.getValue().sum());
            add(pair.getKey(), pair.getValue().n());
        }
        return this;
    }

    public ArrayList<PatternCount> getFrequencyList() {
        ArrayList<PatternCount> list = new ArrayList<>();
        for (Map.Entry<Pattern, StatSummary> pair : statMap.entrySet()) {
            list.add(new PatternCount(pair.getKey(), -pair.getValue().n()));
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
            return (epsilon / ((tot + epsilon)*(1+epsilon))) ;
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


//    public double getProb(Pattern key) {
//        StatSummary ss = statMap.get(key);
//        if (ss != null) {
//            return epsilon + ss.sum() / tot;
//        } else {
//            return epsilon;
//        }
//    }
//
    public double getRawProb(Pattern key) {
        // no epsilon safety
        StatSummary ss = statMap.get(key);
        if (ss != null) {
            return ss.sum() / tot;
        } else {
            return 0.0;
        }
    }

    public void printReport() {
        ArrayList<PatternCount> fl = getFrequencyList();
        double rawSum = 0.0;
        double safeSum = 0.0;
        for (PatternCount pc : fl) {
            System.out.println(pc + "\t " + getProb(pc.pattern) + "\t " + getRawProb(pc.pattern));
            rawSum += getRawProb(pc.pattern);
            safeSum += getProb(pc.pattern);
        }
        System.out.println("Safe sum = " + safeSum);
        System.out.println("Raw sum = " + rawSum);
    }

}
