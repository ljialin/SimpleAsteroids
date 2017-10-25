package planetwar;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.SearchSpaceUtil;
import ga.GridSearch;
import ga.SimpleGASearchSpace;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Arrays;

public class HyperParamTuningTest {


    static int nEvals = 200;
    static int nChecks = 100;
    public static void main(String[] args) {

        ElapsedTimer timer = new ElapsedTimer();

        EvoAlg evoAlg = new CompactSlidingGA();
        evoAlg = new NTupleBanditEA();
        // evoAlg = new SlidingMeanEDA();
        // evoAlg = new GridSearch();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        int nTrials = 5;
        for (int i=0; i<nTrials; i++) {
            ss.add(runTrial(evoAlg));
        }

        System.out.println(ss);
        System.out.println("Total time for experiment: " + timer);
    }


    public static double runTrial(EvoAlg evoAlg) {

        // ok, so the idea here is to modify ...

        EvoAgentSearchSpace eval = new EvoAgentSearchSpace();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        int[] solution = evoAlg.runTrial(eval, nEvals);

        System.out.println("Checking fitness");

        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Mean fitness");
        System.out.println("Running checks: " + nChecks);
        for (int i=0; i<nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);
        System.out.println("Checks complete: " + timer.toString());
        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println(eval.report(solution));
        return ss.mean();
    }
}
