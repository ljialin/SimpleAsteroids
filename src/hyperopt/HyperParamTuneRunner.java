package hyperopt;

import evodef.*;
import ga.GridSearch;
import ga.SimpleGASearchSpace;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.NTupleSystem;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpace;
import planetwar.EvoAgentSearchSpaceAsteroids;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Arrays;

public class HyperParamTuneRunner {


    //     static int nEvals = 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpaceAsteroids().searchSpace());
    public int nEvals = 100; // 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpace().searchSpace());
    public int nChecks = 100;
    public int nTrials = 10;

    public void runTrials(EvoAlg evoAlg, AnnotatedFitnessSpace annotatedFitnessSpace) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        for (int i = 0; i < nTrials; i++) {
            try {
                ss.add(runTrial(evoAlg, annotatedFitnessSpace));
//                 ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(new EvoAgentSearchSpaceAsteroids().getParams());
//                ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(annotatedFitnessSpace.getParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("nEvals per run: " + nEvals);
        System.out.println(ss);
        System.out.println("Total time for experiment: " + timer);
    }

    public double runTrial(EvoAlg evoAlg, AnnotatedFitnessSpace eval) {

        // NoisySolutionEvaluator eval = new EvoAgentSearchSpace();
        // NoisySolutionEvaluator eval = new EvoAgentSearchSpaceAsteroids();

        // eval = new EvoAgentSearchSpace();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        eval.reset();
        int[] solution = evoAlg.runTrial(eval, nEvals);

        System.out.println("Checking fitness");
        return runChecks(eval, solution, nChecks);

    }

    public double runChecks(AnnotatedFitnessSpace eval, int[] solution, int nChecks) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Mean fitness");
        System.out.println("Running checks: " + nChecks);
        for (int i = 0; i < nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);
        System.out.println("Checks complete: " + timer.toString());
        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println();
        // System.out.println(eval.report(solution));

        // but also find out the details
        return ss.mean();

    }
}
