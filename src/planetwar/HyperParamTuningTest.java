package planetwar;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import evodef.SearchSpaceUtil;
import ga.GridSearch;
import ga.SimpleGASearchSpace;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.NTupleSystem;
import ntuple.SlidingMeanEDA;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Arrays;

public class HyperParamTuningTest {


//     static int nEvals = 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpaceAsteroids().searchSpace());
    static int nEvals = 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpace().searchSpace());
    static int nChecks = 100;
    static int nTrials = 10;

    public static void main(String[] args) {


//        EvoAlg evoAlg = new CompactSlidingGA();
//        NTupleBanditEA nTupleBanditEA = new NTupleBanditEA().setReportFrequency(10);
//        evoAlg = nTupleBanditEA;
//        // evoAlg = new SlidingMeanEDA();
//        // evoAlg = new GridSearch();

        EvoAlg[] evoAlgs = {
                new NTupleBanditEA().setKExplore(1),
                new GridSearch(),
                // new CompactSlidingGA(),
                new SlidingMeanEDA(),
        };

        for (EvoAlg evoAlg : evoAlgs) {
            runTrials(evoAlg);
        }


    }

    public static void runTrials(EvoAlg evoAlg) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        for (int i = 0; i < nTrials; i++) {
            try {
                ss.add(runTrial(evoAlg));
//                 ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(new EvoAgentSearchSpaceAsteroids().getParams());
                ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(new EvoAgentSearchSpace().getParams());
            } catch (Exception e) {
            }
        }

        System.out.println("nEvals per run" + nEvals);
        System.out.println(ss);
        System.out.println("Total time for experiment: " + timer);
    }

    public static double runTrial(EvoAlg evoAlg) {

        NoisySolutionEvaluator eval = new EvoAgentSearchSpace();
        // NoisySolutionEvaluator eval = new EvoAgentSearchSpaceAsteroids();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        int[] solution = evoAlg.runTrial(eval, nEvals);

        System.out.println("Checking fitness");
        return runChecks(eval, solution);

    }

    public static double runChecks(NoisySolutionEvaluator eval, int[] solution) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Mean fitness");
        System.out.println("Running checks: " + nChecks);
        for (int i = 0; i < nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);
        System.out.println("Checks complete: " + timer.toString());
        System.out.println("Solution: " + Arrays.toString(solution));
        // System.out.println(eval.report(solution));

        // but also find out the details
        return ss.mean();

    }
}
