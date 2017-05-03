package bandits;

import evodef.SearchSpace;
import utilities.Picker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */


public class MBanditArray {

    static Random random = new Random();
    static double eps = 1e-6;

    int nBandits;
    ArrayList<MBanditGene> genome;

    public MBanditArray(SearchSpace searchSpace) {
        this.nBandits = nBandits;
        genome = new ArrayList<>();
        this.nBandits = searchSpace.nDims();
        for (int i=0; i<nBandits; i++) {
            genome.add(new MBanditGene(searchSpace.nValues(i)));
        }
    }

    public int[] toArray() {
        int[] a = new int[nBandits];
        int ix = 0;
        for (MBanditGene gene : genome) {
            a[ix++] = gene.x;
        }
        return a;
    }

    public MBanditGene selectGeneToMutate(int nEvals) {

        Picker<MBanditGene> picker = new Picker<>();

        int i = 0;
        for (MBanditGene gene : genome) {
            // break ties with small random values
            // System.out.println(i++ + "\t " + gene.statusString(nEvals));
            picker.add(gene.urgency(nEvals) + eps * random.nextDouble(), gene);
        }

        // System.out.println(picker.getBestScore());
        return picker.getBest();

    }
}
