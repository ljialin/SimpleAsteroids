package ntuple;

import utilities.Picker;
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
                picker.add(stats[i].mean() + random.nextGaussian() * 1e-10, i);
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

    public void updateMean(int i, double score) {

        stats[i].add(score);
    }

}
