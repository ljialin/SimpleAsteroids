package evodef.test;

import evodef.*;
import ga.SimpleRMHC;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.ArrayList;

public class MutationStrengthTest {

    static int nDims = 20;
    static int mValues = 10;
    static int nEvals = 200;

    public static void main(String[] args) {
        double[] ma = new double[]{0, 1, 2, 3, 4, 5, 10, 20};
        int nRuns = 100;
        for (double x : ma) {
            runTrials(x, nRuns);
        }
    }

    static void runTrials(double x, int n) {
        StatSummary ss = new StatSummary("Mut = " + x);
        double pointProb = x; //  / nDims;
        LineChart lineChart = new LineChart();
        for (int i=0; i<n; i++) {
            EvalMaxM eval = new EvalMaxM(nDims, mValues);
            eval.strict = false;
            DefaultMutator mutator = new DefaultMutator(eval.searchSpace());
            mutator.flipAtLeastOneValue = true;
            mutator.pointProb = pointProb;
            mutator.totalRandomChaosMutation = false;
            EvoAlg evoAlg = new SimpleRMHC().setMutator(mutator);
            evoAlg.runTrial(eval, nEvals);

            LinePlot linePlot = new LinePlot().setData(maxFitnessArray(evoAlg.getLogger().getFitnessArray()));

            linePlot.setColor(LinePlot.randColor());
            lineChart.addLine(linePlot);
//            System.out.println(linePlot.getData());
//            System.out.println(linePlot.getData().size() + " : " + evoAlg.getLogger().getFitnessArray().size());
            // find the bnest yet assuming no noise
            double maxFit = new StatSummary().add(evoAlg.getLogger().getFitnessArray()).max();
            ss.add(maxFit);
        }
        lineChart.setXLabel("evaluation");
        lineChart.setYLabel("fitness");
        lineChart.xAxis = new LineChartAxis(new double[]{0, nEvals/2, nEvals});
        double max = (mValues-1) * nDims;
        lineChart.yAxis = new LineChartAxis(new double[]{0, max/2, max});

        String title = String.format("Mutation strength = %.2f", pointProb);
        lineChart.setTitle(title);
        new JEasyFrame(lineChart, "Convergence runs");
        System.out.println(ss);
        System.out.println();
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
