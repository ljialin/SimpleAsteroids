package ntuple;

import evodef.SearchSpaceUtil;
import utilities.Picker;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by sml on 17/01/2017.
 */
public class EvaluateChoices {

    static Random random = new Random();
    static double epsilon = 1e-6;


    NTupleSystem nTupleSystem;
    double kExplore;

    public EvaluateChoices(NTupleSystem nTupleSystem, double kExplore) {
        this.nTupleSystem = nTupleSystem;
        this.kExplore = kExplore;
    }

    Picker<int[]> picker = new Picker<int[]>(Picker.MAX_FIRST);
    Set<Integer> indices = new HashSet<>();

    int nAttempts = 0;

    // careful: the exploration term is used here
    public void add(int[] p) {
        Integer ix = SearchSpaceUtil.indexOf(nTupleSystem.searchSpace, p);
        if (!indices.contains(ix)) {
            indices.add(ix);
            double exploit = nTupleSystem.getMeanEstimate(p);
            double explore = nTupleSystem.getExplorationEstimate(p);

            // add small random noise to break ties
            double combinedValue = exploit + kExplore * explore +
                    random.nextDouble() * epsilon;
            // System.out.format("\t %d\t %d\t %.2f\t %.2f\t %.2f\n", i, j,
            // exploit, explore, combinedValue);
            picker.add(combinedValue, p);

        } else {
            nAttempts++;
        }
    }

    public int n() {
        return indices.size() + nAttempts/4;
    }
}
