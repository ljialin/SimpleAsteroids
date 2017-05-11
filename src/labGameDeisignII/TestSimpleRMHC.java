package labGameDeisignII;

import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.NoisySolutionEvaluator;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jialin Liu on 11/05/2017.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */




/**
 * Provide a simple main method to test this...
 */

public class TestSimpleRMHC {
  static int nDims = 10;  // problem dimension
  static int mValues = 2; // number of possible values at each coordinate
  static int resamples = 50; // Resampling number
  static int nFitnessEvals = 500; // Optimisation budget using number of fitness evaluations
  static boolean useFirstHit = true;  // Use first hitting as stop condition if true
  static int nTrials = 10000; // Repetition of optimisation
  static NoisySolutionEvaluator solutionEvaluator;


  public static void main(String[] args) {
    // run configuration for an experiment

    // use first hitting as stop condition if true
    useFirstHit = false;
    // set up the mutation operator
    // mutate at least one coordinate
    Mutator.flipAtLeastOneValue = true;
    // matation probability in case of (1+1)-EA
    Mutator.defaultPointProb = 1.0;

    /** set the evaluator to noise-free OneMax **/
    solutionEvaluator = new EvalMaxM(nDims, mValues, 0);
    resamples = 1;; // no resampling is required for noise-free problem
    /** set the evaluator to noisy OneMax with additive Gaussian noise **/
    // solutionEvaluator = new EvalNoisyWinRate(nDims, mValues, 1.0);

    System.out.println("Running experiment with following settings:");
    System.out.println("Solution evaluator: " + solutionEvaluator.getClass());
    System.out.format("Use first hitting time    :\t %s\n" , useFirstHit );
    System.out.format("RMHC: (flip at least one) :\t %s\n" , Mutator.flipAtLeastOneValue );
    System.out.format("Point mutation probability:\t %.4f\n" , Mutator.defaultPointProb / nDims );

    // timer
    ElapsedTimer t = new ElapsedTimer();
    optimise();
    System.out.println(t);
  }

  static StatSummary testNTupleBanditEA(int nTrials) {
    StatSummary ss = runTrials(new NTupleBanditEA(), nTrials, 0);
    System.out.println(ss);
    return ss;
  }

  /**
   * Run optimisation trials using simple RMHC
   * TODO: MCTS/RHEA Lab
   * Read and understand the code. Run a trial to see the output.
  **/
  static void optimise() {
    ArrayList<StatSummary> results = new ArrayList<>();
    /** run nTrials optimisation trial **/
    StatSummary ss = runTrials(new SimpleRMHC(resamples), nTrials, resamples);
    results.add(ss);
    System.out.println(ss);
    System.out.format("Resample rate: %d\t %.3f\t %.3f \t %.3f   \n", resamples, ss.mean(), ss.stdErr(), ss.max());

    /** now suppose we may just want to track the number of successes
        in this case we just take the number of samples out of each StatSummary **/
    ArrayList<Double> successRate = new ArrayList<>();
    for (StatSummary s : results) {
      successRate.add(s.n() * 100.0 / nTrials);
    }
    System.out.println("Results array:");
    System.out.println(successRate);
  }

  // this is to print out rows of JavaScript for use in a GoogleChart
  public static void printJS(List<StatSummary> ssl, StatSummary nt) {
    double ntm = nt.mean();
    double ntUpper = ntm + nt.stdErr() * 3;
    double ntLower = ntm - nt.stdErr() * 3;
    for (int i = 0; i < ssl.size(); i++) {
      StatSummary ss = ssl.get(i);
      double m = ss.mean();
      double upper = m + 3 * ss.stdErr();
      double lower = m - 3 * ss.stdErr();
      System.out.format("[ %d, %.2f, %.2f, %.2f, %.2f, %.2f, %.2f, ],\n", (1 + i), m, lower, upper, ntm, ntLower, ntUpper);
    }
  }


  public static StatSummary runTrials(EvoAlg ea, int nTrials, int nResamples) {
    // summary of fitness stats
    StatSummary ss = new StatSummary();

    // summary of optima found
    StatSummary nTrueOpt = new StatSummary("N True Opt Hits");

    for (int i = 0; i < nTrials; i++) {
      ss.add(runTrial(ea, nTrueOpt));
    }

    System.out.format("N Resamples: \t %2d ;\t n True Opt %s: %.2f\n",nResamples, useFirstHit ? "hits" : "returns", nTrueOpt.n() * 100.0 / nTrials);
    return nTrueOpt;
  }


  static double runTrial(EvoAlg ea, StatSummary nTrueOpt) {

    // grab from static var for now
    NoisySolutionEvaluator evaluator = solutionEvaluator;
    evaluator.reset();

    int[] solution = ea.runTrial(evaluator, nFitnessEvals);

    //  horrible mess at the moment - changing to a different evaluator
//        if (useFirstHit && evaluator.logger().firstHit != null) {
//            // System.out.println("Optimal first hit?: " + evaluator.logger().firstHit);
//            nTrueOpt.add(evaluator.logger().firstHit);
//        } else if (trueEvaluator.evaluate(solution) == 1.0) {
//            nTrueOpt.add(1);
//        }

    if (useFirstHit && evaluator.logger().firstHit != null) {
      // System.out.println("Optimal first hit?: " + evaluator.logger().firstHit);
      nTrueOpt.add(evaluator.logger().firstHit);
    } else if (evaluator.isOptimal(solution)) {
      nTrueOpt.add(1);
    }

    return solutionEvaluator.trueFitness(solution);

  }
}
