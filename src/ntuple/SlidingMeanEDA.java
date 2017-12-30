package ntuple;

import evodef.*;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by simonmarklucas on 19/06/2017.
 */


public class SlidingMeanEDA implements EvoAlg {

    public static void main(String[] args) {

        SlidingMeanEDA smeda = new SlidingMeanEDA().setHistoryLength(20);

        NoisySolutionEvaluator evaluator = new EvalMaxM(100, 3, 0);
        smeda.verbose = true;

        int[] p = smeda.runTrial(evaluator, 10);

        System.out.println(Arrays.toString(p));
        System.out.println(evaluator.trueFitness(p));

    }

    public int historyLength = 50;
    ArrayList<ScoredVec> history;

    int nSamples = 1;

    boolean verbose = false;

    // this for GVGAI
    static Integer timeLimit = null; //30;

    @Override
    public void setInitialSeed(int[] seed) {
        // ignore this for now
    }

    SolutionEvaluator evaluator;

    GeneArrayMeanModel geneArrayModel;

    static Random random = new Random();

    public SlidingMeanEDA setHistoryLength(int historyLength) {
        this.historyLength = historyLength;
        return this;
    }

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();

        int n = searchSpace.nDims();

        history = new ArrayList<>();
        geneArrayModel = new GeneArrayMeanModel(searchSpace);

        int nSteps = 0;
        Long endTime = null;
        if (timeLimit != null) {
            // endTime = timeLimit + System.currentTimeMillis();
            endTime = timeLimit + System.nanoTime() / 1000000;
        }
//        while (evaluator.nEvals() < nEvals && (endTime == null || System.currentTimeMillis() < endTime)) {

        while (evaluator.nEvals() < nEvals && (endTime == null || System.nanoTime() / 1000000 < endTime)) {

            int prevEvals = evaluator.nEvals();

            // each time around evaluate a single new individual: x
            // but occasionally have the possibility of sampling the best guess so far
            int[] x = geneArrayModel.generate();
            double f = fitness(evaluator, x, nSamples).mean();

            ScoredVec scoredVec = new ScoredVec(x, f);

            // now treat the history like a circular buffer and update it
            // always add the ScoredVector in

            geneArrayModel.updateModelMean(scoredVec);
            // geneArrayModel.report();
            if (history.size() < historyLength) {
                history.add(scoredVec);

            } else {
                // if we're replacing one in the history
                // then remove it from our stats
                int ix = nSteps % historyLength;
                geneArrayModel.removeVec(history.get(ix));
                history.set(ix, scoredVec);
            }
            nSteps++;

            int diffEvals = evaluator.nEvals() - prevEvals;
            for (int i=0; i<diffEvals; i++) {
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

        // System.out.println("Total evaluations made in sliding: " + evaluator.nEvals());
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
        for (int i=0; i<nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            ss.add(fitness);

        }
        return ss;
    }


}
