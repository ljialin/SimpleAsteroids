package evodef.test;

import evodef.EvalMaxM;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomWalkAwayFromOpt {

    public static void main(String[] args) {
        int nSteps = 5001;

        int nDims = 1000;
        int mValues = 2;
        ArrayList<Double> fa = new ArrayList<>();

        EvalMaxM evalMaxM = new EvalMaxM(nDims, mValues);

        int[] solution = new int[nDims];
        for (int i=0; i<nDims; i++) {
            solution[i] = 1;
        }
        Random random = new Random();

        // now walk away for a number of steps

        for (int i=0; i<nSteps; i++) {
            // pick a random point and flip it
            int ix = random.nextInt(nDims);
            solution[ix] = 1 - solution[ix];
            double fitness = evalMaxM.evaluate(solution);
            fa.add(fitness);
        }

        LineChart lineChart = LineChart.easyPlot(fa);
        lineChart.setXLabel("steps");
        lineChart.setYLabel("fitness");
        lineChart.yAxis = new LineChartAxis(new double[]{0, nDims/2, nDims});
        lineChart.plotBG = Color.getHSBColor(0.85f, 1, 1);
        // lineChart.g


        new JEasyFrame(lineChart, "Random mutations on a perfect solution");
    }
}
