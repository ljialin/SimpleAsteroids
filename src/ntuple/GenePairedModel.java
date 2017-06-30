package ntuple;

import utilities.Picker;

import java.util.Random;

/**
 * Created by simonmarklucas on 24/06/2017.
 */
public class GenePairedModel {

    static Random random = new Random();

    static double K = 2000;

    int nValues;
    double[] nWins;
    double[] count;

    // simple probablity array model
    double[] p;
    static int init = 100;

    public GenePairedModel(int nValues) {
        this.nValues = nValues;
        nWins = new double[nValues];
        count = new double[nValues];
        p = new double[nValues];
        resetStats();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<nValues; i++) {
            buffer.append(String.format("[%.2f]", p[i]));
            // buffer.append(String.format("[%.2f, %.2f, %.2f]", nWins[i], count[i], nWins[i] / count[i]));
        }
        // buffer.append("\n");
        return buffer.toString();
    }



    public int generateOld() {
        // generate a random number in the range specified by each
        // win probability
        // and then pick the biggest
        // initially do this for the
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<nValues; i++) {
            double randScore = random.nextDouble() * nWins[i] / count[i];
            picker.add(randScore, i);
        }
        return picker.getBest();
    }

    public int generate() {
        // generate a random number in the range specified by each
        // win probability
        // and then pick the biggest
        // initially do this for the
//        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
//        for (int i=0; i<nValues; i++) {
//            double randScore = random.nextDouble() * nWins[i];
//            picker.add(randScore, i);
//        }

        // this only works because the total will always sum to 1.0
        double x = random.nextDouble();
        double tot = 0;
        for (int i=0; i<nValues; i++) {
            tot += p[i];
            if (x <= tot) return i;
        }
        throw new RuntimeException("Failed to return a valid option in GenePairedModel");
        // return nValues-1;
    }

    public void update(int winner, int loser, double weight) {
//        nWins[winner] += weight;
//        count[winner] += weight;
//        count[loser]  += weight;

        p[winner] += 1/K;
        p[loser] -= 1/K;

    }

    public void updateDiff(int x, int y, double diff) {
//        nWins[winner] += weight;
//        count[winner] += weight;
//        count[loser]  += weight;

        p[x] += diff/K;
        p[y] -= diff/K;

    }

    public int argmax() {
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<nValues; i++) {
            // add small noise to dither
            picker.add(p[i] + random.nextDouble() * 1e-10, i);
        }
        return picker.getBest();
    }

    public void resetStats() {
        // give each one a 50% chance of being on

        for (int i=0; i<nValues; i++) {
            nWins[i] = 0.5 * init;
            count[i] = init;
            p[i] = 1.0 / nValues;
        }
    }

//    public void updateMean(int i, double score) {
//
//    }
}
