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

        CompactSlidingModelGA cga = new CompactSlidingModelGA();

        SolutionEvaluator evaluator = new EvalMaxM(20, 2, 1);
        cga.verbose = true;

        cga.runTrial(evaluator, 100);

    }

    // this decides how many vectors to generate each iteration
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

    static Random random = new Random();

    public CompactSlidingModelGA setHistoryLength(int historyLength) {
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
        geneArrayModel = new GeneArrayModel(searchSpace);

        int nSteps = 0;
        while (evaluator.nEvals() < nEvals) {

            // each time around the loop we make one fitness evaluation of p
            // and add this NEW information to the memory

            // double fitness = evaluator.evaluate(p);

            int prevEvals = evaluator.nEvals();

            // each time around evaluate a single new individual
            int[] x = geneArrayModel.generate();
            double f = fitness(evaluator, x, nSamples).mean();

            ScoredVec scoredVec = new ScoredVec(x, f);

            // evaluate it against all history members

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

        System.out.println("Total evaluations made: " + evaluator.nEvals());
        return solution;
    }


    @Override
    public void setModel(NTupleSystem nTupleSystem) {

    }

    @Override
    public NTupleSystem getModel() {
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


    //            int[] x = randBitVec(pVec);
//            int[] y = randBitVec(pVec);
//
//            if (nSamples == 1) {
//                xFit = evaluator.evaluate(x);
//                yFit = evaluator.evaluate(y);
//            } else {
//                xFit = fitness(evaluator, x).mean();
//                yFit = fitness(evaluator, y).mean();
//            }
//
//            if (xFit > yFit) {
//                updatePVec(pVec, x, y);
//            } else {
//                updatePVec(pVec, y, x);
//            }


}
