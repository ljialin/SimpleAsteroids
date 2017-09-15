package ga;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import utilities.StatSummary;

import java.util.Arrays;

public class EvoSGATest {
    public static void main(String[] args) {

        // ok, so the idea here is to modify ...

        SimpleGASearchSpace eval = new SimpleGASearchSpace();
        eval.setEvaluator(new EvalMaxM(50, 2, 0.0));

        EvoAlg evoAlg = new CompactSlidingGA();
        evoAlg = new NTupleBanditEA();
        // evoAlg = new SlidingMeanEDA();

        int[] solution = evoAlg.runTrial(eval, 1000);

        // int[] solution = {0, 0, 0};


        System.out.println("Checking fitness");

        StatSummary ss = new StatSummary("Mean fitness");
        int nChecks = 30;
        for (int i=0; i<nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);

        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println(eval.report(solution));
    }
}
