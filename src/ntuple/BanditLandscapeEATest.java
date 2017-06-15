package ntuple;

import evodef.EvalMaxM;

import java.util.Arrays;

/**
 * Created by simonmarklucas on 15/06/2017.
 */
public class BanditLandscapeEATest {
    public static void main(String[] args) {
        NTupleBanditEA banditEA = new NTupleBanditEA();

        // set up a 2d grid search space

        int nDims = 2;
        int mValues = 10;
        double noiseLevel = 1.0;
        EvalMaxM problem = new EvalMaxM(nDims, mValues, noiseLevel);

        int nEvals = 100;
        int[] solution = banditEA.runTrial(problem, nEvals);

        System.out.println("Current Solution: " + Arrays.toString(solution));




    }

    static void report(NTupleSystem system) {

        

    }
}
