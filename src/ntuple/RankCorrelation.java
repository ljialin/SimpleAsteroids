package ntuple;

import evodef.SearchSpaceUtil;
import utilities.Ranker;

import java.util.ArrayList;

public class RankCorrelation {
    Ranker<Integer> r1, r2;
    ArrayList<Integer> keys;

    public RankCorrelation() {
        r1 = new Ranker<>();
        r2 = new Ranker<>();
        keys = new ArrayList<>();
    }

    public RankCorrelation add(int i, double x1, double x2) {
        r1.add(x1, i);
        r2.add(x2, i);
        keys.add(i);
        return this;
    }

    public double rankCorrelation() {
        double sumSquaredDiff = 0;
        for (int i : keys) {
            // System.out.println(i + "\t " + r1.getRank(i) + "\t " + r2.getRank(i));
            double diff = r1.getRank(i) - r2.getRank(i);
            sumSquaredDiff += diff * diff;
        }
        int n = keys.size();
        double spearmanCoefficient = 1 - sumSquaredDiff * 6 / (n * n * n - n);
        System.out.format("Spearman correlation = %.4f\n", spearmanCoefficient);
        System.out.println("N Points = " + n);
        return spearmanCoefficient;
    }
}
