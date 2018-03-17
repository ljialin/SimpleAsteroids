package ntuple.tests;

import plot.LineChart;
import plot.LineChartAxis;
import plot.LineGroup;
import plot.LinePlot;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SortedBernoulliTest {

    public static void main(String[] args) {
        LineChart lineChart = new LineChart();

        LinePlot linePlot = new LinePlot();

        LineGroup lineGroup = new LineGroup();

        int nTrials = 100;

        int nGroups = 288;

        double pWin = 0.5;

        ArrayList<StatSummary> stats = new ArrayList<>();
        for (int i=0; i<nGroups; i++) {
            StatSummary ss = new StatSummary();
            stats.add(ss);
            for (int j=0; j<nTrials; j++) {
                int result = Math.random() < pWin ? 1 : -1;
                ss.add(result);
            }
        }

        Collections.sort(stats);

        lineGroup.stats = stats;

        lineChart.addLineGroup(lineGroup);
        lineChart.bg = Color.gray;
        lineChart.plotBG = Color.white;

        lineChart.setTitle("Sorted Coin Toss Trials, n=100, p=0.5, error bars 1 std-err");
        lineChart.setYLabel("Fitness");
        lineChart.setXLabel("Trial");
        lineChart.stdErrs = 1.0;
        lineChart.xAxis = new LineChartAxis(new double[]{0, 144, 288});
        lineChart.yAxis = new LineChartAxis(new double[]{-1, 0, 0.8});

        new JEasyFrame(lineChart, "Coin toss");

    }


}
