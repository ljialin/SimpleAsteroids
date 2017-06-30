package ntuple;

import utilities.Picker;
import utilities.RangeMapper;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 24/06/2017.
 */

public class GeneMeanModel {

    static Random random = new Random();

    static double K = 2000;

    int nValues;
    StatSummary[] stats;

    public GeneMeanModel(int nValues) {
        this.nValues = nValues;
        resetStats();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<nValues; i++) {
            buffer.append(String.format("[%.2f]", stats[i].mean()));
            // buffer.append(String.format("[%.2f, %.2f, %.2f]", nWins[i], count[i], nWins[i] / count[i]));
        }
        // buffer.append("\n");
        return buffer.toString();
    }

    public int argmax() {
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<nValues; i++) {
            // add small noise to dither
            // System.out.println(i + " : " + stats[i].mean());
            try {
                double mean = stats[i].mean();
                picker.add(mean + random.nextGaussian() * 1e-10, i);
            } catch (Exception e) {

            }
        }
        return picker.getBest();
    }

    public void resetStats() {
        // give each one a 50% chance of being on
        // set each StatSummary as strict to avoid incorrect use of mean value
        // a StatSummary with no numbers does not have a mean
        stats = new StatSummary[nValues];
        for (int i=0; i<nValues; i++)
            stats[i] = new StatSummary().setStrict(true);
    }
    static double gain = 2;

    public int generate() {
        // add the numbers to ...
        StatSummary rangeStats = new StatSummary();
        // find the range
        for (StatSummary ss : stats) {
            if (ss.n() > 0) {
                rangeStats.add(ss.mean());
            }
        }
        double range = rangeStats.max() - rangeStats.min();

        // having got the range we now map these values in to the new range
        // when computing the softmax function
        RangeMapper map = new RangeMapper(rangeStats.min(), rangeStats.max(), 0, gain);

        // now put all the numbers through the map
        double totExp = 0;
        for (StatSummary ss : stats) {
            if (ss.n() > 0) totExp += Math.exp(map.map(ss.mean()));
        }
        // now pick one
        System.out.println("Tot exp = " + totExp);
        double x = random.nextDouble() * totExp;

        double tot = 0;
        for (int i=0; i<nValues; i++) {
            if (stats[i].n() > 0) {
                tot += Math.exp(map.map(stats[i].mean()));
                if (x <= tot) return i;
            }
        }
        throw new RuntimeException("Failed to return a valid option in GenePairedModel");
    }

    public void updateMean(int i, double score) {
        stats[i].add(score);
    }

    public void remove(int i, double score) {
        stats[i].removeFromMean(score);
    }

}
