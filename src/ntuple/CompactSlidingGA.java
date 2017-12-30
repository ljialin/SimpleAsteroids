package ntuple;

import evodef.*;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by simonmarklucas on 19/06/2017.
 */
public class CompactSlidingGA implements EvoAlg {

    public static void main(String[] args) {

        // play with adding things to an ArrayList ...


        // this illustrates how we will use an array list as a circular history buffer
        int historyLength = 5;
        ArrayList<Integer> history = new ArrayList<>();
        for (int i=0; i<20; i++) {
            if (history.size() < historyLength)
                history.add(i);
            else
                history.set(i % historyLength, i);
            System.out.println(history);
        }
        System.exit(0);



        CompactSlidingGA cga = new CompactSlidingGA();

        SolutionEvaluator evaluator = new EvalMaxM(20, 2, 1);
        cga.verbose = true;

        cga.runTrial(evaluator, 100);

    }

    public boolean useBayesUpdates = false;

    // set this to true to just measure pVec convergence,
    // false to measure pVec quality (convergence to one)
    // note: this measure only makes sense for problems
    // where the optimal solution is all ones
    public boolean pVecConvergenceOnly = false;
    // this decides how many vectors to generate each iteration
    public int historyLength = 20;
    ArrayList<ScoredVec> history;

    int nSamples = 1;
    public static double defaultK = 2000;
    public double K = 10;

    public CompactSlidingGA() {
        this(defaultK);
    }

    public CompactSlidingGA(double k) {
        K = k;
    }

    boolean verbose = false;

    public String toString() {
        if (useBayesUpdates) {
            return String.format("SWcGA: bayes, w=%d", (int) historyLength);
        } else {
            return String.format("SWcGA: k=%d, w=%d", (int) K, historyLength);
        }
    }


    @Override
    public void setInitialSeed(int[] seed) {
        // ignore this for now
    }

    SolutionEvaluator evaluator;
    double[] pVec;
    SlidingBayes[] bayes;

    static Random random = new Random();

    public CompactSlidingGA setHistoryLength(int historyLength) {
        this.historyLength = historyLength;
        return this;
    }

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();

        int n = searchSpace.nDims();

        bayes = new SlidingBayes[n];
        history = new ArrayList<>();
        pVec = new double[n];
        for (int i=0; i<n; i++) {
            pVec[i] = 0.5;
            bayes[i] = new SlidingBayes();
        }

        ArrayList<Double> pVecEvo = new ArrayList<>();

        int nSteps = 0;
        while (evaluator.nEvals() < nEvals) {

            // each time around the loop we make one fitness evaluation of p
            // and add this NEW information to the memory

            // double fitness = evaluator.evaluate(p);

            int prevEvals = evaluator.nEvals();

            // each time around evaluate a single new individual
            int[] x;
            if (useBayesUpdates) {
                x = CompactGAUtil.randBitBayes(bayes);
            } else {
                x = CompactGAUtil.randBitVec(pVec);
            }
            double f = fitness(evaluator, x, nSamples).mean();

            ScoredVec scoredVec = new ScoredVec(x, f);

            // evaluate it against all history members

            for (ScoredVec sv : history) {
                if (scoredVec.score > sv.score) {
                    CompactGAUtil.updatePVec(pVec, scoredVec.p, sv.p, K);
                    CompactGAUtil.updateBayes(bayes, scoredVec.p, sv.p);
                } else {
                    CompactGAUtil.updatePVec(pVec, sv.p, scoredVec.p, K);
                    CompactGAUtil.updateBayes(bayes, sv.p, scoredVec.p);
                }
            }

            // keep track of how rapidly the probabilities converge to one
            // this only makes any sense for cases where the solution is
            // an array of 1s.
            double pTot = 0;
            for (int i=0; i<n; i++) {
                // clamp it to be between zero and one
                double p = useBayesUpdates ? bayes[i].pOne() : pVec[i];
                p = Math.max(0, (Math.min(1, p)));
                // optionally just measure it's proximity to either extreme
                if (pVecConvergenceOnly) {
                    p = 0.5 + Math.abs(p - 0.5);
                }
                pTot += p;
            }
            pVecEvo.add(pTot/n);

            // now treat the history like a circular buffer and update it

            if (history.size() < historyLength) {
                history.add(scoredVec);
            } else {
                // at this point if we're using Bayes updating then we should remove the old ScoredVec from the
                // Bayes updates
                if (useBayesUpdates) {
                    // get the old one we're about to replace
                    ScoredVec old = history.get(nSteps % historyLength);
                    history.set(nSteps % historyLength, scoredVec);
                    // now iterate over the vectors, comparing with the old one
                    // and removing it from the stats
                    for (ScoredVec sv : history) {
                        if (sv.score > old.score) {
                            CompactGAUtil.removeBayes(bayes, sv.p, old.p);
                        } else {
                            CompactGAUtil.removeBayes(bayes, old.p, sv.p);
                        }
                    }


                } else {
                    history.set(nSteps % historyLength, scoredVec);
                }
            }
            nSteps++;

            int diffEvals = evaluator.nEvals() - prevEvals;
            for (int i=0; i<diffEvals; i++) {
                if (useBayesUpdates) {
                    evaluator.logger().logBestYest(CompactGAUtil.argmax(bayes));
                } else {
                    System.out.println("Ev: " + evaluator);
                    evaluator.logger().logBestYest(CompactGAUtil.argmax(pVec));
                }
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

        int[] solution = useBayesUpdates ?
                CompactGAUtil.argmax(bayes) : CompactGAUtil.argmax(pVec);

        // logger.
        evaluator.logger().keepBest(solution, evaluator.evaluate(solution));
        TestEAGraphRunTrials.extras.add(pVecEvo);

        // System.out.println("Total evaluations made: " + evaluator.nEvals());
        // System.out.println(Arrays.toString(pVec));
        if (useBayesUpdates) {
            for (SlidingBayes sb : bayes) {
                System.out.println(sb);
            }
            System.out.println();
        }
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
