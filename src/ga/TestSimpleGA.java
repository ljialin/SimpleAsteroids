package ga;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import evodef.SolutionEvaluator;

import java.util.Arrays;

/**
 * Created by simonmarklucas on 09/09/2017.
 */
public class TestSimpleGA {

    public static void main(String[] args) {

        NoisySolutionEvaluator evaluator = new EvalMaxM(10, 2, 1.0);

        EvoAlg evoAlg = new SimpleGA().setPopulationSize(100);

        int[] solution = evoAlg.runTrial(evaluator, 10000);

        System.out.println(Arrays.toString(solution));

    }

}
