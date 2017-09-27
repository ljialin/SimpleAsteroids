package ga;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import evodef.SearchSpaceUtil;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import utilities.StatSummary;

import java.util.Arrays;

public class EvoSGATest {

    public static void main(String[] args) {
        EvoAlg evoAlg = new CompactSlidingGA();
        evoAlg = new NTupleBanditEA();
        // evoAlg = new SlidingMeanEDA();
        evoAlg = new GridSearch();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        int nTrials = 1;
        for (int i=0; i<nTrials; i++) {
            ss.add(runTrial(evoAlg));
        }
        System.out.println(ss);
    }

    public static double runTrial(EvoAlg evoAlg) {

        // ok, so the idea here is to modify ...

        SimpleGASearchSpace eval = new SimpleGASearchSpace();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));
        eval.setEvaluator(new EvalMaxM(50, 2, 0.0));


        int[] solution = evoAlg.runTrial(eval, 5000);

        // int[] solution = {0, 0, 0};


        System.out.println("Checking fitness");

        StatSummary ss = new StatSummary("Mean fitness");
        int nChecks = 50;
        for (int i=0; i<nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);

        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println(eval.report(solution));
        return ss.mean();
    }

}
