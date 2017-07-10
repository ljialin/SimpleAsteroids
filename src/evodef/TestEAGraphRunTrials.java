package evodef;

import ntuple.SlidingMeanEDA;

/**
 * Created by simonmarklucas on 10/07/2017.
 */
public class TestEAGraphRunTrials {

    public static void main(String[] args) {

        // create and run a test
        // showing flexibility to create multiple graphs

        int nDims=10, mValues = 2;
        double noise = 1.0;
        int nEvals = 500;

        NoisySolutionEvaluator solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, noise);
        solutionEvaluator = new EvalMaxM(nDims, mValues, noise);
        // solutionEvaluator = new Eval2DNonLinear(8, noise);

        TestEAGraph tester = new TestEAGraph(solutionEvaluator, nEvals);

        EvoAlg evoAlg = new SlidingMeanEDA();

        double res = tester.runTrial(evoAlg);

        System.out.println(res);



    }

}
