package logger.util;

import logger.core.ScalarLogger;

public class EntropyLogger implements ScalarLogger {

    public static void main(String[] args) {
        EntropyLogger entropy = new EntropyLogger();
        System.out.println(entropy.entropy());

        System.out.println(entropy.entropy());
        entropy.log(1);
        System.out.println(entropy.entropy());
        entropy.log(2);
        System.out.println(entropy.entropy());
        entropy.log(3);
        System.out.println(entropy.entropy());
        entropy.log(4);
        System.out.println(entropy.entropy());
        entropy.setNSymbols(16);
        System.out.println(entropy.entropy());
    }

    HashCounter counter = new HashCounter();
    public String name;

    Integer nSymbols;

    // setting this normalises to the number
    // of symbols that could possibly occur

    public EntropyLogger setNSymbols(int nSymbols) {
        this.nSymbols = nSymbols;
        return this;
    }

    public EntropyLogger setName(String name) {
        this.name = name;
        return this;
    }

    // count doubles rather than ints, just as easy
    public EntropyLogger log(double key) {
        counter.add(key);
        return this;
    }

    public double entropy() {
        double tot = 0;
        for (HashCounter.Count count : counter.map.values()) {
            tot += count.x;
        }
        double entropy = 0;
        for (HashCounter.Count count : counter.map.values()) {
            if (count.x > 0) {
                double p = count.x / tot;
                entropy += -p * log2(p);
            }
        }
        // normalise it by the maximum possible entropy
        int n = nSymbols != null ? nSymbols : counter.map.size();
        if (n > 1) entropy /= log2(n);
        return entropy;
    }

    static double log2(double x) { return Math.log(x) / Math.log(2); }

}
