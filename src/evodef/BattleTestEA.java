package evodef;

import bandits.MBanditEA;
import ga.SimpleRMHC;
import gvglink.EvalBattleGame;
import ntuple.NTupleBanditEA;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Arrays;

/**
 * Created by sml on 16/08/2016.
 */
public class BattleTestEA {

    public static void main(String[] args) {

        // the number of bandits is equal to the size of the array
        int nTrials = 10;
        int nFitnessEvals = 100;

        EvoAlg ea = new MBanditEA();

        ea = new SimpleRMHC(1);



        int nDims = 10;
        int mValues = 2;

        // SolutionEvaluator evaluator = new EvalMaxM(nDims, mValues);
        SolutionEvaluator evaluator = new EvalBattleGame();

        // ea = new NTupleBanditEA();


        System.out.println("Best fitness stats:");
        System.out.println(runTrials(ea, evaluator, nTrials, nFitnessEvals));

        // evaluator.
    }

    public static StatSummary runTrials(EvoAlg ea, SolutionEvaluator evaluator, int nTrials, int nFitnessEvals) {

        // record the time stats
        StatSummary ss = new StatSummary();

        for (int i=0; i<nTrials; i++) {
            ElapsedTimer t = new ElapsedTimer();
            evaluator.reset();
            System.out.println("Running trial: " + i);
            int[] solution = ea.runTrial(evaluator, nFitnessEvals);
            System.out.println(t);
            System.out.println("Solution: " + Arrays.toString(solution));
            System.out.println(evaluator.logger().ss);

            System.out.println();

            evaluator.logger().report();
            System.out.println();
            ss.add(evaluator.logger().ss.max());
        }
        return ss;
    }

}
