package evodef.test;

import evodef.DefaultMutator;
import evodef.EvalMaxM;
import evodef.EvoAlg;
import evodef.SearchSpaceUtil;
import ga.SimpleRMHC;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.ArrayList;

public class MutationStrengthFirstStepTest {

    static int nDims = 20;
    static int mValues = 20;
    static int nEvals = 200;

    public static void main(String[] args) {
        double[] ma = new double[]{0, 1, 2, 3, 4, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90};
        int nRuns = 100;
        ArrayList<StatSummary> stats = new ArrayList<>();
        for (double x : ma) {
            StatSummary ss = runTrials(x, nRuns);
            stats.add(ss);
        }
        // plotStats(stats);
    }

    static void plotStats(ArrayList<StatSummary> stats) {
        LineChart lineChart = new LineChart();
        for (StatSummary ss : stats) {
            LinePlot linePlot = new LinePlot();

            linePlot.setColor(LinePlot.randColor());
            lineChart.addLine(linePlot);
        }
        lineChart.setXLabel("evaluation");
        lineChart.setYLabel("fitness");
        lineChart.xAxis = new LineChartAxis(new double[]{0, nEvals / 2, nEvals});
        double max = (mValues - 1) * nDims;
        lineChart.yAxis = new LineChartAxis(new double[]{0, max / 2, max});

        String title = String.format("Initial Stepsf");
        lineChart.setTitle(title);
        new JEasyFrame(lineChart, "Convergence runs");
    }

    static StatSummary runTrials(double x, int n) {
        StatSummary ss = new StatSummary("Mut = " + x);
        double pointProb = x; //  / nDims;
        for (int i = 0; i < n; i++) {
            EvalMaxM eval = new EvalMaxM(nDims, mValues);
            eval.strict = false;
            DefaultMutator mutator = new DefaultMutator(eval.searchSpace());
            mutator.flipAtLeastOneValue = true;
            mutator.pointProb = pointProb;
            mutator.totalRandomChaosMutation = false;

            // pick a random point in the search space
            int[] p = SearchSpaceUtil.randomPoint(eval.searchSpace());
            double fitnessBefore = eval.evaluate(p);
            p = mutator.randMut(p);
            double fitnessAfter = eval.evaluate(p);

            // now we'll only accept the mutation if it improves or is neutral
            double improvemsnt = Math.max(0, (fitnessAfter - fitnessBefore));
            ss.add(improvemsnt);
        }
        System.out.println(ss);
        System.out.println();
        return ss;
    }

    static int[] nOnes (int j, int n) {
        int[] p = new int[n];
        for (int i=0; i<n; i++) {
            p[i] = i < j ? 1 : 0;
        }
        return p;
    }

    static ArrayList<Double> maxFitnessArray(ArrayList<Double> fa) {
        ArrayList<Double> mfa = new ArrayList<>();
        StatSummary ss = new StatSummary();
        for (double x : fa) {
            ss.add(x);
            mfa.add(ss.max());
        }
        return mfa;
    }
}
