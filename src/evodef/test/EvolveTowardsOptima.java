package evodef.test;

import evodef.DefaultMutator;
import evodef.EvalMaxM;
import ga.SimpleRMHC;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class EvolveTowardsOptima {

    public static void main(String[] args) {
        int nSteps = 10001;

        int nDims = 1000;
        int mValues = 2;

        EvalMaxM evalMaxM = new EvalMaxM(nDims, mValues);

        SimpleRMHC rmhc = new SimpleRMHC();
        DefaultMutator mutator = new DefaultMutator(evalMaxM.searchSpace());
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 0;
        rmhc.setMutator(mutator);

        rmhc.runTrial(evalMaxM, nSteps);

        ArrayList<Double> fa = rmhc.getLogger().fa;
                LineChart lineChart = LineChart.easyPlot(fa);
        lineChart.setXLabel("steps");
        lineChart.setYLabel("fitness");
        lineChart.yAxis = new LineChartAxis(new double[]{0, nDims/2, nDims});
        lineChart.plotBG = Color.getHSBColor(0.85f, 1, 1);

        new JEasyFrame(lineChart, "Evolving towards a perfect solution");
        System.out.println("Final fitness: " + fa.get(fa.size()-1));
    }
}
