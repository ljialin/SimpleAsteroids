package bandits;

import java.util.Arrays;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class MBanditGeneTest {


    // simple stand alone way to test the
    // operation of a single bandit gene
    public static void main(String[] args) {

        int nArms = 3;
        MBanditGene gene = new MBanditGene(nArms);

        for (int i=0; i<100; i++) {
            int prev = gene.x;

            gene.banditMutate();

            int cur = gene.x;

            double delta = (cur - prev) * 0.1;

            gene.applyReward(delta);
            System.out.println("Mutated value = " + gene.x);

            gene.revertOrKeep(delta);

            System.out.println(i + "\t " + gene.maxDelta());

            System.out.println(Arrays.toString(gene.deltaRewards));
            System.out.println(Arrays.toString(gene.armPulls));
            System.out.println("Gene value = " + gene.x);

            System.out.println();
            System.out.println();
        }
    }
}
