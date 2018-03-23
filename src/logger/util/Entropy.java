package logger.util;

public class Entropy {
    public static double entropy(double[] a) {
        double tot = 0;
        for (double x : a) {
            tot += x;
        }
        if (tot <= 0) return 0;
        double entropy = 0;

        for (double x : a) {

            double p = x / tot;
            if (p > 0) {
                entropy += -p * log2(p);
            }
        }
        return entropy / log2(a.length);
    }

    static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

}
