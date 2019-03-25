package test;

import plot.LineChart;
import plot.LineChartAxis;
import plot.LineGroup;
import utilities.JEasyFrame;
import utilities.Range;
import utilities.StatSummary;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class LearnedModelPlot {

    public static void main(String[] args) throws Exception {

        LineGroup lg = new LineGroup();

        // read in the stat summaries then plot them


        String dataFile = "data/ModelLearning/ExactLearner.txt";

        Scanner sc = new Scanner(new FileReader(dataFile));
        ArrayList<Double> xAxis = new ArrayList<>();
        ArrayList<StatSummary> data = new ArrayList<>();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            System.out.println(line);
            Scanner sl = new Scanner(line);
            if (!sl.hasNext()) break;
            int n = sl.nextInt();
            double mean = sl.nextDouble();
            double stdErr = sl.nextDouble();
            xAxis.add((double) n);
            data.add(new MySummary(mean, stdErr));
        }

        LineGroup lineGroup = new LineGroup();
        lineGroup.name = "Exact Learner";
        lineGroup.stats = data;

        lineGroup.color = Color.blue;
        // now what?

        double[] xa = new double[xAxis.size()];
        for (int i=0; i<xa.length; i++) xa[i] = xAxis.get(i);

        LineChart lineChart = new LineChart().addLineGroup(lineGroup);
        lineChart.yAxis = new LineChartAxis(new double[]{0, 50, 100, 150, 200, 250});
        lineChart.setXLabel("Training Samples");
        lineChart.setYLabel("Score Using Learned Model");
        // lineChart.xAxis = new LineChartAxis(xa);
        lineChart.bg = Color.white;
        // lineChart.xM

        // lineChart.s
        // lineChart.xAxis.range = new StatSummary().add(0).add(xa.length);

        new JEasyFrame(lineChart, "Learned Model Test");


    }

    static class MySummary extends StatSummary {
        double mean, stdErr;

        public MySummary(double mean, double stdErr) {
            this.mean = mean;
            this.stdErr = stdErr;
        }

        public double mean() {return mean;}
        public double stdErr() {return stdErr; }
    }

}
