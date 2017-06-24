package ntuple;

import utilities.Picker;

import java.util.Random;

/**
 * Created by simonmarklucas on 24/06/2017.
 */
public class GeneModel {



    static Random random = new Random();

    int nValues;
    double[] nWins;
    int[] count;
    static int init = 100;

    public GeneModel(int nValues) {
        this.nValues = nValues;
        nWins = new double[nValues];
        count = new int[nValues];
        // give each one a 50% chance of being on
        for (int i=0; i<nValues; i++) {
            nWins[i] = 0.5 * init;
            count[i] = init;
        }
    }

    public int generate() {
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

    public void update(int winner, int loser) {
        nWins[winner]++;
        count[winner]++;
        count[loser]++;
    }

    public int argmax() {
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<nValues; i++) {
            // add small noise to dither
            picker.add(nWins[i] / count[i] + random.nextDouble() * 1e-10, i);
        }
        return picker.getBest();
    }
}
