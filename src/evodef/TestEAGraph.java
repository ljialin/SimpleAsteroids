package evodef;

import utilities.LinePlot;
import utilities.StatSummary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import static evodef.TestEASimple.*;

/**
 * Created by simonmarklucas on 10/07/2017.
 */

public class TestEAGraph {

    StatSummary nTrueOpt, trueFit;

    NoisySolutionEvaluator evaluator;
    int nFitnessEvals;

    public TestEAGraph(NoisySolutionEvaluator evaluator, int nFitnessEvals) {
        this.evaluator = evaluator;
        this.nFitnessEvals = nFitnessEvals;
        resetStats();
    }

    public void resetStats() {
        nTrueOpt = new StatSummary();
        trueFit = new StatSummary();
    }

    public double runTrial(EvoAlg ea) {

        // grab from static var for now
        NoisySolutionEvaluator evaluator = solutionEvaluator;
        evaluator.reset();

        int[] solution = ea.runTrial(evaluator, nFitnessEvals);
        // System.out.println("Solution: " + Arrays.toString(solution) + " : " + solutionEvaluator.trueFitness(solution));
        trueFit.add(solutionEvaluator.trueFitness(solution));

        // linePlots.add(new LinePlot().setData(solutionEvaluator.logger().fa).setColor(lineColor));

        // ok, instead look at the true fitnesses of the evaluated solutions

        ArrayList<Double> noiseFree = new ArrayList<>();
        // System.out.println("Best yet solutions length: " + solutionEvaluator.logger().bestYetSolutions.size());
        for (int[] p : solutionEvaluator.logger().bestYetSolutions) {
            noiseFree.add(solutionEvaluator.trueFitness(p));
        }

        linePlots.add(new LinePlot().setData(noiseFree).setColor(lineColor));


        if (useFirstHit && evaluator.logger().firstHit != null) {
            // System.out.println("Optimal first hit?: " + evaluator.logger().firstHit);
            nTrueOpt.add(evaluator.logger().firstHit);
        } else if (evaluator.isOptimal(solution)) {
            nTrueOpt.add(1);
        }

        return solutionEvaluator.trueFitness(solution);

    }



}
