package evodef;

import plot.LineChart;
import plot.LinePlot;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// import static evodef.TestEASimple.*;

/**
 * Created by simonmarklucas on 10/07/2017.
 */

public class TestEAGraph {

    // give the lines a default colour
    Color color = Color.blue;
    StatSummary nTrueOpt, trueFit;

    NoisySolutionEvaluator evaluator;
    int nFitnessEvals;
    List<LinePlot> linePlots;

    public TestEAGraph(NoisySolutionEvaluator evaluator, int nFitnessEvals) {
        this.evaluator = evaluator;
        this.nFitnessEvals = nFitnessEvals;
        resetStats();
    }


    public TestEAGraph setColor(Color color) {
        this.color = color;
        return this;
    }

    public void resetStats() {
        nTrueOpt = new StatSummary("n true optima");
        trueFit = new StatSummary("true fitness stats");
        linePlots = new ArrayList<>();
    }

    public TestEvoResults runTrials(EvoAlg ea, int nTrials) {
        resetStats();
        for (int i=0; i<nTrials; i++) {
            runTrial(ea);
        }
        return new TestEvoResults(ea.toString(), trueFit, linePlots);
    }

    public ArrayList<Double> runTrial(EvoAlg ea) {

        evaluator.reset();

        int[] solution = ea.runTrial(evaluator, nFitnessEvals);
        // System.out.println("Solution: " + Arrays.toString(solution) + " : " + solutionEvaluator.trueFitness(solution));
        trueFit.add(evaluator.trueFitness(solution));

        // linePlots.add(new LinePlot().setData(solutionEvaluator.logger().fa).setColor(lineColor));

        // ok, instead look at the true fitnesses of the evaluated solutions

        ArrayList<Double> noiseFree = new ArrayList<>();
        // System.out.println("Best yet solutions length: " + solutionEvaluator.logger().bestYetSolutions.size());
        for (int[] p : evaluator.logger().bestYetSolutions) {
            noiseFree.add(evaluator.trueFitness(p));
        }

        linePlots.add(new LinePlot().setData(noiseFree).setColor(color));

        if (evaluator.isOptimal(solution)) {
            nTrueOpt.add(1);
        }
        return noiseFree;
    }

    public LineChart getLineChart() {
        LineChart lineChart = new LineChart();
        for (LinePlot linePlot : linePlots) lineChart.addLine(linePlot);
        return lineChart;
    }


    public void report() {
        System.out.println(nTrueOpt);
        System.out.println();
        System.out.println(trueFit);
        System.out.println();
    }
}

