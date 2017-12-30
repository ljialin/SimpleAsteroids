package ntuple;

import evodef.*;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by simonmarklucas on 19/06/2017.
 */
public class CompactSlidingModelGA implements EvoAlg {

    public static void main(String[] args) {

        CompactSlidingModelGA cga = new CompactSlidingModelGA(20);

        NoisySolutionEvaluator evaluator = new EvalMaxM(50, 2, 0);
        cga.verbose = true;

        int[] p = cga.runTrial(evaluator, 100);

        System.out.println(Arrays.toString(p));
        System.out.println(evaluator.trueFitness(p));


    }

    public int historyLength = 50;
    ArrayList<ScoredVec> history;

    int nSamples = 1;
    public static double defaultK = 2000;
    public double K = 10;

    public CompactSlidingModelGA() {
        this(defaultK);
    }

    public CompactSlidingModelGA(double k) {
        K = k;
    }

    boolean verbose = false;


    @Override
    public void setInitialSeed(int[] seed) {
        // ignore this for now
    }

    SolutionEvaluator evaluator;

    GeneArrayModel geneArrayModel;
    static Integer timeLimit = 30;

    static Random random = new Random();

    public CompactSlidingModelGA setHistoryLength(int historyLength) {
        this.historyLength = historyLength;
        return this;
    }

    // worth a try but it works badly
    static boolean resetStatsWithFullHistory = false;

    // also was worth a try, but works badly
    static double pArgMaxInjection = 0.0;

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();

        int n = searchSpace.nDims();

        history = new ArrayList<>();
        geneArrayModel = new GeneArrayModel(searchSpace);

        int nSteps = 0;
        Long endTime = null;
        if (timeLimit != null) {
            endTime = timeLimit + System.currentTimeMillis();
        }
        while (evaluator.nEvals() < nEvals && (endTime == null || System.currentTimeMillis() < endTime)) {

            // each time around the loop we make one fitness evaluation of p
            // and add this NEW information to the memory

            // double fitness = evaluator.evaluate(p);

            int prevEvals = evaluator.nEvals();

            // each time around evaluate a single new individual: x
            // but occasionally have the possibility of sampling the best guess so far
            int[] x;
            if (random.nextDouble() < pArgMaxInjection) {
                x = geneArrayModel.argMax();
            } else {
                x = geneArrayModel.generate();
            }
            double f = fitness(evaluator, x, nSamples).mean();

            ScoredVec scoredVec = new ScoredVec(x, f);

            // evaluate it against all history members

            if (resetStatsWithFullHistory && history.size() >= historyLength) {
                geneArrayModel.resetStats();
            }

            for (ScoredVec sv : history) {
                if (scoredVec.score > sv.score) {
                    geneArrayModel.updateModel(scoredVec, sv);
                } else {
                    geneArrayModel.updateModel(sv, scoredVec);
                }
            }

            // now treat the history like a circular buffer and update it

            if (history.size() < historyLength) {
                history.add(scoredVec);
            } else {
                history.set(nSteps % historyLength, scoredVec);
            }
            nSteps++;

            int diffEvals = evaluator.nEvals() - prevEvals;
            for (int i = 0; i < diffEvals; i++) {
                evaluator.logger().logBestYest(geneArrayModel.argMax());
            }

//            if (verbose) {
//                int[] solution = CompactGAUtil.argmax(pVec);
//                System.out.format("%.3f\t %s\n", evaluator.evaluate(solution), Arrays.toString(solution));
//                // System.out.println(Arrays.toString(pVec));
//                for (double p : pVec) {
//                    System.out.format("%.4f\t", p);
//                }
//                System.out.println();
//                System.out.println();
//
//            }
        }

        // now draw each x and y vec according to pVec

        // indeed, what to return

        // finally, return the argmax of each dimension

        int[] solution = geneArrayModel.argMax();
        // logger.
        evaluator.logger().keepBest(solution, evaluator.evaluate(solution));

        // System.out.println("Total evaluations made in compact: " + evaluator.nEvals());
        // geneArrayModel.report();
        return solution;
    }


    @Override
    public void setModel(BanditLandscapeModel nTupleSystem) {

    }

    @Override
    public BanditLandscapeModel getModel() {
        return null;
    }

    @Override
    public EvolutionLogger getLogger() {
        return evaluator.logger();
    }

    @Override
    public void setSamplingRate(int samplingRate) {

        nSamples = samplingRate;
    }


    static StatSummary fitness(SolutionEvaluator evaluator, int[] sol, int nSamples) {
        StatSummary ss = new StatSummary();
        for (int i = 0; i < nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            ss.add(fitness);

        }
        return ss;
    }



}
