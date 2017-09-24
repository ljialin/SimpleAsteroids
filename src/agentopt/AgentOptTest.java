package agentopt;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import ga.SimpleGASearchSpace;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import utilities.StatSummary;

import java.util.Arrays;

public class AgentOptTest {

    // this is just a placeholder for the moment
    // the idea is within this package to include
    // some very clean and simple examples of how
    // to set up GVGAI agents ready for optimisation


    public static void main(String[] args) {
        EvoAlg evoAlg = new CompactSlidingGA();
        evoAlg = new NTupleBanditEA();
        // evoAlg = new SlidingMeanEDA();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        int nTrials = 1;
        for (int i=0; i<nTrials; i++) {
            ss.add(runTrial(evoAlg));
        }
        System.out.println(ss);
    }


    public static double runTrial(EvoAlg evoAlg) {

        // ok, so the idea here is to modify ...

//        SimpleGASearchSpace eval = new SimpleGASearchSpace();
//        eval.setEvaluator(new EvalMaxM(50, 2, 0.0));

        AgentEvaluator eval = new AgentEvaluator();

        int[] solution = evoAlg.runTrial(eval, 10);

        // int[] solution = {0, 0, 0};

        System.out.println("Checking fitness");

        StatSummary ss = new StatSummary("Mean fitness");
        int nChecks = 5;
        for (int i=0; i<nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);

        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println(eval.report(solution));
        return ss.mean();
    }



}
