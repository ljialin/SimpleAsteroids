package evodef;

import bandits.MBanditEA;
import evomaze.ShortestPathTest;
import ga.SimpleRMHC;
import ntuple.NTuple;
import ntuple.NTupleBanditEA;
import ntuple.NTupleSystem;
import utilities.ElapsedTimer;
import utilities.StatSummary;

/**
 * Created by sml on 16/08/2016.
 */
public class TestEA {

    static int nDims = 10;
    static int mValues =2;
    static int nTrials = 10;
    static int nFitnessEvals = 400;


    public static void main(String[] args) {

        // the number of bandits is equal to the size of the array

        sweepSamplingRate(1, 1);

        // evaluator.
    }

    public static void sweepSamplingRate(int from, int to) {
        EvoAlg ea = new MBanditEA();


//        SolutionEvaluator evaluator = new EvalMaxM(nDims, mValues, 1.0);
//        SolutionEvaluator noiseFree = new EvalMaxM(nDims, mValues, 0.0);

        SolutionEvaluator evaluator = new EvalNoisyWinRate(nDims, mValues, 1.0);
        SolutionEvaluator noiseFree = new EvalNoisyWinRate(nDims, mValues, 0.0);



        for (int i=from; i<=to; i++) {
            ea = new SimpleRMHC(i);

            // ea = new MBanditEA();
            ea = new NTupleBanditEA();

            StatSummary ss = runTrials(ea, evaluator, noiseFree, nTrials, nFitnessEvals);
            // System.out.format("%d\t %.2f \t %.3f \t %.3f \t %.2f\n", i, ss.mean(), ss.stdErr(), ssOpt.mean(), nOpt.mean());
            System.out.format("%d\t %.3f \t %.3f \t %.3f \t %.2f\t  %.3f\n",
                    i, ss.mean(), nOpt.mean(), ssOpt.mean(),  ntOpt.mean(), ntPerf.mean());
            // System.out.format("%d\t %.2f \t %.2f \t %.2f \n", i, ss.mean(), ss.stdErr(), ssOpt.mean());

        }
    }

    // bung in a static to track the number of times the optimal was visited on each run
    static StatSummary ssOpt;
    static StatSummary nOpt;
    static StatSummary ntOpt;
    static StatSummary ntPerf;

    public static StatSummary runTrials(EvoAlg ea, SolutionEvaluator evaluator, SolutionEvaluator noiseFree, int nTrials, int nFitnessEvals) {

        // record the time stats
        StatSummary ss = new StatSummary();

        ssOpt = new StatSummary();
        nOpt = new StatSummary();
        ntOpt = new StatSummary();
        ntPerf = new StatSummary();

        for (int i=0; i<nTrials; i++) {
            ElapsedTimer t = new ElapsedTimer();
            evaluator.reset();

            NTupleSystem nts = new NTupleSystem();
            nts.setSearchSpace(evaluator.searchSpace());
            ea.setModel(nts);

            ea.runTrial(evaluator, nFitnessEvals);

//            System.out.println("Ran the trial");
//            System.out.println(t);

            // foundOpt is 1 if the optimum was visted, zero otherwise
            int foundOpt = evaluator.logger().nOptimal() > 0 ? 1 : 0;
            nOpt.add( foundOpt );  //  evaluator.logger().nOptimal() );
            // System.out.println(t);
            // evaluator.logger().report();
            // System.out.println();
            double trueFit = noiseFree.evaluate(evaluator.logger().finalSolution());
            // System.out.println("\t True fit = " + trueFit);

            ss.add(trueFit);
            ssOpt.add( trueFit == evaluator.searchSpace().nDims() ? 1 : 0 );

            // NTupleSystem nts =  ea.getModel();
            // nts.printDetailedReport();
            if (nts != null) {
                double ntFit = noiseFree.evaluate(nts.getBestSolution());
                ntPerf.add(ntFit == evaluator.searchSpace().nDims() ? 1 : 0);
                ntOpt.add(ntFit);
            }
//            System.out.println("Retrieved from the N-Tuple");
//            System.out.println(t);
//            System.out.println();
            // System.out.println();
        }
        return ss;
    }
}
