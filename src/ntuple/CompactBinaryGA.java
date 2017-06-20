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
    public int nParents = 8;


    int nSamples = 1;
    public static double defaultK = 1000;
    public double K = 10;

    public CompactBinaryGA() {
        this(defaultK);
    }

    public CompactBinaryGA(double k) {
        K = k;
    }

    boolean verbose = false;


    @Override
    public void setInitialSeed(int[] seed) {
        // ignore this for now
    }

    SolutionEvaluator evaluator;
    double[] pVec;

    static Random random = new Random();


    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();

        int n = searchSpace.nDims();

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

            ScoredVec[] vecs = new ScoredVec[nParents];
            for (int i=0; i<nParents; i++) {
                // generate
                int[] x = randBitVec(pVec);
                double f = fitness(evaluator, x).mean(); // evaluator.evaluate(x);
                ScoredVec sv = new ScoredVec(x, f);
                vecs[i] = sv;
            }

            // now pair them off
            for (int i=0; i<nParents; i++) {
                for (int j = i+1; j<nParents; j++) {
                    if (vecs[i].score > vecs[j].score) {
                        updatePVec(pVec, vecs[i].p, vecs[j].p);
                    } else {
                        updatePVec(pVec, vecs[j].p, vecs[i].p);
                    }
                }
            }


            if (verbose) {
                int[] solution = argmax(pVec);
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

        int[] solution = argmax(pVec);
        // logger.
        evaluator.logger().keepBest(solution, evaluator.evaluate(solution));

        // System.out.println("Total evaluations made: " + evaluator.nEvals());
        return solution;
    }

    void updatePVec(double[] pVec, int [] winner, int[] loser) {
        for (int i=0; i<pVec.length; i++) {
            if (winner[i] != loser[i]) {
                if (winner[i] == 1) {
                    pVec[i] += 1/K;
                } else {
                    pVec[i] -= 1/K;
                }
            }
        }
    }

    // randomly dither the output to avoid weird effects such as
    // returning the optimal solution for OneMax after zero iterations!

    public static int[] argmax(double[] pVec) {
        int[] x = new int[pVec.length];
        for (int i=0; i<x.length; i++) {
            x[i] = pVec[i] + 1e-6*random.nextGaussian() > 0.5 ? 1 : 0;
        }
        return x;
    }

    public static int[] randBitVec(double[] pVec) {
        int[] x = new int[pVec.length];
        for (int i=0; i<x.length; i++) {
            x[i] = random.nextDouble() < pVec[i] ? 1 : 0;
        }
        return x;
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

    StatSummary fitness(SolutionEvaluator evaluator, int[] sol) {
        StatSummary ss = new StatSummary();
        for (int i=0; i<nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            ss.add(fitness);

        }
        return ss;
    }

    class ScoredVec {
        int[] p;
        double score;

        public ScoredVec(int[] p, double score) {
            this.p = p;
            this.score = score;
        }
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
