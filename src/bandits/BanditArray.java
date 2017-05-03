package bandits;

import utilities.Picker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */


public class BanditArray {

    static Random random = new Random();
    static double eps = 1e-6;

    int nBandits;
    ArrayList<BanditGene> genome;

    public BanditArray(int nBandits) {
        this.nBandits = nBandits;
        genome = new ArrayList<>();
        for (int i=0; i<nBandits; i++) {
            genome.add(new BanditGene());
        }
    }

    public int[] toArray() {
        int[] a = new int[nBandits];
        int ix = 0;
        for (BanditGene gene : genome) {
            a[ix++] = gene.x;
        }
        return a;
    }

    public BanditGene selectGeneToMutate(int nEvals) {

        Picker<BanditGene> picker = new Picker<>();

        int i = 0;
        for (BanditGene gene : genome) {
            // break ties with small random values
            // System.out.println(i++ + "\t " + gene.statusString(nEvals));
            picker.add(gene.urgency(nEvals) + eps * random.nextDouble(), gene);
        }

        // System.out.println(picker.getBestScore());
        return picker.getBest();

    }
}
