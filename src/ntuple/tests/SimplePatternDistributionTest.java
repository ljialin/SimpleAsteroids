package ntuple.tests;

import evodef.DefaultMutator;
import evodef.Mutator;
import ntuple.ConvNTuple;
import ntuple.PatternCount;

import java.util.List;
import java.util.Random;

import static ntuple.tests.EvolveMarioLevelTest.getAndShowLevel;

/**
 * Aim is simple: read in and compare two training files.
 * <p>
 * Could even be rthe same file for comparison
 */

public class SimplePatternDistributionTest {
    public static void main(String[] args) throws Exception {
        int[][] level = getAndShowLevel(true);

        ConvNTuple c1 = getConvNTuple(level);

        // now randomly corrupt the level (or not)


        int[] mutated = c1.flatten(level);

        // Mutator mutator = new DefaultMutator()
        Random random = new Random();
        int nEdits = 100;
        System.out.println("KL Div versus mutations: ");
        for (int i=0; i<nEdits; i++) {
            // just set some random entries to zero
            System.out.println(i + "\t " + c1.getKLDivergence(mutated, 0.001));
            int ix = random.nextInt(mutated.length);
            mutated[ix] = 0;
        }

        System.out.println();
        List<PatternCount> patterns = c1.sampleDis.getFrequencyList();
        for (PatternCount pc : patterns) {
            System.out.println(pc);
        }
        System.out.println();
    }

    public static ConvNTuple getConvNTuple(int[][] level) {
        int w = level.length;
        int h = level[0].length;

        System.out.println(w + " : " + h);

        int filterWidth = 3;
        int filterHeight = 3;
        int stride = 1;
        int nDims = w * h;
        System.out.println("N Dims = " + nDims);
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(w, h);
        convNTuple.setFilterDimensions(filterWidth, filterHeight);
        // convNTuple.setMValues(mValues).setStride(stride);
        convNTuple.setStride(stride);
        convNTuple.makeIndices();
        convNTuple.reset();

        convNTuple.addPoint(level, 1);

        System.out.println(convNTuple.sampleDis.statMap.size());

        return convNTuple;
    }
}
