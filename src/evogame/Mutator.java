package evogame;

import evodef.EvalMaxM;
import evodef.SearchSpace;
import evodef.SearchSpaceUtil;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by sml on 17/01/2017.
 */
public class Mutator {

    public static void main(String[] args) {

        // check operation on a simple case
        Mutator mutator = new Mutator(new EvalMaxM(3, 2));

        System.out.println(Arrays.toString(mutator.randMut(new int[]{0, 0, 0})));
        System.out.println(Arrays.toString(mutator.randMut(new int[]{1, 1, 1})));

        // check operation of this
        mutator.totalRandomChaosMutation = false;
        mutator = new Mutator(new EvalMaxM(50, 10));
        System.out.println(Arrays.toString(mutator.randMut(new int[mutator.searchSpace.nDims()])));
        // mutator.totalRandomChaosMutation = true;
        System.out.println(Arrays.toString(mutator.randMut(new int[mutator.searchSpace.nDims()])));

        System.out.println();
        Mutator mutator1 = new Mutator(new EvalMaxM(50, 10));
        System.out.println(mutator1);
        mutator1.flipAtLeastOneValue = false;
        mutator1.pointProb = 3.0;
        System.out.println(mutator1);

        Mutator mutator2 = new Mutator(new EvalMaxM(50, 2));
        mutator2.pointProb = 5;
        System.out.println(mutator2);
        System.out.println(Arrays.toString(mutator2.randMut(new int[mutator2.searchSpace.nDims()])));

    }

    // this will be set each time a Mutator is created
    public double pointProb;
    static Random random = new Random();

    public static boolean totalRandomChaosMutation = false;
    public static double defaultPointProb = 1.0;
    public static boolean flipAtLeastOneValueDefault = true;

    public boolean flipAtLeastOneValue = flipAtLeastOneValueDefault;

    SearchSpace searchSpace;

    public Mutator(SearchSpace searchSpace) {
        this.searchSpace = searchSpace;
        pointProb = defaultPointProb;
        flipAtLeastOneValue = flipAtLeastOneValueDefault;
    }

    public int[] randMut(int[] v) {
        // note: the algorithm ensures that at least one of the bits is different in the returned array
        if (totalRandomChaosMutation) {
            return SearchSpaceUtil.randomPoint(searchSpace);
        }
        // otherwise do a proper mutation
        int n = v.length;
        int[] x = new int[n];
        // pointwise probability of additional mutations
        double mutProb = pointProb / n;
        // choose element of vector to mutate
        int ix = random.nextInt(n);
        if (!flipAtLeastOneValue) {
            // setting this to -1 means it will never match the first clause in the if statement in the loop
            // leaving it at the randomly chosen value ensures that at least one bit (or more generally value) is always flipped
            ix = -1;
        }
        // copy all the values faithfully apart from the chosen one
        for (int i=0; i<n; i++) {
            if (i == ix || random.nextDouble() < mutProb) {
                x[i] = mutateValue(v[i], searchSpace.nValues(i));
            } else {
                x[i] = v[i];
            }
        }
        return x;
    }

    int mutateValue(int cur, int nPossible) {
        // the range is nPossible-1, since we
        // selecting the current value is not allowed
        // therefore we add 1 if the randomly chosen
        // value is greater than or equal to the current value
        if (nPossible <= 1) return cur;
        int rx = random.nextInt(nPossible-1);
        return rx >= cur ? rx+1 : rx;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mutator\n");
        sb.append("Totally random mutations: " + totalRandomChaosMutation + "\n");
        sb.append("Flip at least one value:  " + flipAtLeastOneValue + "\n");
        sb.append("Point mutation prob:      " + pointProb + "\n");
        return sb.toString();
    }

    public Mutator setSearchSpace(SearchSpace searchSpace) {
        this.searchSpace = searchSpace;
        return this;
    }
}
