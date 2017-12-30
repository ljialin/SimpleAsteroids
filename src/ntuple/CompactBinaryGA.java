package ntuple;

import evodef.*;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.Random;


/**
 * Created by simonmarklucas on 19/06/2017.
 */
public class CompactBinaryGA implements EvoAlg {

    public static void main(String[] args) {
        CompactBinaryGA cga = new CompactBinaryGA();

        SolutionEvaluator evaluator = new EvalMaxM(20, 2, 1);
        cga.verbose = true;

        cga.runTrial(evaluator, 100);

    }

    // this decides how many vectors to generate each iteration
    public int nParents = 2;


    int nSamples = 1;
    public static double defaultK = 2000;
    public double K = 10;

    public CompactBinaryGA() {
        this(defaultK);
    }

    public CompactBinaryGA(double k) {
        K = k;
    }

    boolean verbose = false;


    public String toString() {
        return String.format("MScGA: k=%d, s=%d", (int) K, nParents);
    }



    @Override
    public void setInitialSeed(int[] seed) {
        // ignore this for now
    }

    public CompactBinaryGA setParents(int nParents) {
        this.nParents = nParents;
        return this;
    }

    SolutionEvaluator evaluator;
    double[] pVec;

    static Random random = new Random();

    // or set to null for normal model
    public Integer nToFlip = null; // 2;


    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();

        int n = searchSpace.nDims();

        // experiment with flip few from base versio
        int[] base = new int[n];

        pVec = new double[n];
        for (int i=0; i<n; i++) {
            pVec[i] = 0.5;
        }

        while (evaluator.nEvals() < nEvals) {

            // each time around the loop we make one fitness evaluation of p
            // and add this NEW information to the memory


            // double fitness = evaluator.evaluate(p);

            // the new version enables resampling
            double xFit, yFit;

            int prevEvals = evaluator.nEvals();
            ScoredVec[] vecs = new ScoredVec[nParents];
            for (int i=0; i<nParents; i++) {
                // generate
                int[] x;
                if (nToFlip != null) {
                    x = CompactGAUtil.randBitVec(pVec, base, nToFlip);
                } else {
                    x = CompactGAUtil.randBitVec(pVec);
                }
                double f = fitness(evaluator, x, nSamples).mean(); // evaluator.evaluate(x);
                ScoredVec sv = new ScoredVec(x, f);
                vecs[i] = sv;
            }

            // now pair them off
            for (int i=0; i<nParents; i++) {
                for (int j = i+1; j<nParents; j++) {
                    if (vecs[i].score > vecs[j].score) {
                        CompactGAUtil.updatePVec(pVec, vecs[i].p, vecs[j].p, K);
                    } else {
                        CompactGAUtil.updatePVec(pVec, vecs[j].p, vecs[i].p, K);
                    }
                }
            }

            int diffEvals = evaluator.nEvals() - prevEvals;
            for (int i=0; i<diffEvals; i++) {
                evaluator.logger().logBestYest(CompactGAUtil.argmax(pVec));
            }

            if (verbose) {
                int[] solution = CompactGAUtil.argmax(pVec);
                System.out.format("%.3f\t %s\n", evaluator.evaluate(solution), Arrays.toString(solution));
                // System.out.println(Arrays.toString(pVec));
                for (double p : pVec) {
                    System.out.format("%.4f\t", p);
                }
                System.out.println();
                System.out.println();

            }


        }

            // now draw each x and y vec according to pVec

        // indeed, what to return

        // finally, return the argmax of each dimension

        int[] solution = CompactGAUtil.argmax(pVec);
        // logger.
        evaluator.logger().keepBest(solution, evaluator.evaluate(solution));

        // System.out.println("Total evaluations made: " + evaluator.nEvals());
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
