package ntuple;

import bandits.BanditEA;
import evodef.Eval2DNonLinear;
import evodef.EvalMaxM;
import evodef.SearchSpace;
import evodef.SearchSpaceUtil;
import utilities.JEasyFrame;

import java.util.Arrays;

/**
 * Created by simonmarklucas on 15/06/2017.
 */
public class BanditLandscapeEATest {
    public static void main(String[] args) {
        NTupleBanditEA banditEA = new NTupleBanditEA();

        // set up a 2d grid search space

        int nDims = 2;
        int mValues = 4;
        double noiseLevel = 1.0;
        // EvalMaxM problem = new EvalMaxM(nDims, mValues, noiseLevel);
        Eval2DNonLinear problem = new Eval2DNonLinear(mValues, noiseLevel);

        int nEvals = 200;
        NTupleView2D nTupleView2D = showView(banditEA, mValues);
        nTupleView2D.solutionEvaluator = new Eval2DNonLinear(mValues, 0);
        banditEA.view = nTupleView2D;

        // interesting question of how to attach a listener
        // to the algorithm to get updates as it runs
        int[] solution = banditEA.runTrial(problem, nEvals);

        // report(problem, banditEA.banditLandscapeModel);

        System.out.println();

        System.out.println("Solution returned: " + Arrays.toString(solution));



    }

    static NTupleView2D showView(NTupleBanditEA banditEA, int mValues) {

        NTupleView2D nTupleView2D = new NTupleView2D(banditEA, mValues);
        new JEasyFrame(nTupleView2D, "NTuple View 2D");

        return nTupleView2D;
    }

    static void report(SearchSpace searchSpace, NTupleSystem nTupleSystem) {

        // note that there are elegant ways to iterate over any number
        // dimensions but this is only dealing with 2-dimensional case
        // and I've hard-wired it in

        for (int i=0; i<SearchSpaceUtil.size(searchSpace); i++) {
            int[] p = SearchSpaceUtil.nthPoint(searchSpace, i);
            System.out.println(Arrays.toString(p));
            nTupleSystem.report(p);
            System.out.println();
            // System.out.println(system.);
        }
    }
}
