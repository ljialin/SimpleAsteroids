package ntuple.tests;

import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.Arrays;

public class KLDivPlot {

    public static void main(String[] args) {

        KLDivPlot div = new KLDivPlot();
        double[] pa = {1.0, 0.0};
        double[] qa = new double[2];

        int nPoints = 101;
        double inc = 0.01;
        double p = 0;
        LinePlot plot1 = new LinePlot().setColor(Color.red);
        LinePlot plot2 = new LinePlot().setColor(Color.blue);
        epsilon = 1e-2;

        StatSummary ss = new StatSummary();

        double maxDiff = div(pa, new double[]{1-pa[0], 1-pa[1]});

        for (int i=0; i<nPoints; i++) {
            qa[0] = p; qa[1] = 1-p;
            double x1 = div(pa, qa) / maxDiff;
            double x2 = div(qa, pa) / maxDiff;
            plot1.add(x1);
            plot2.add(x2);
            p += inc;
            ss.add(x1).add(x2);
            System.out.println(Arrays.toString(pa) + " : " + Arrays.toString(qa));

            // System.out.println(i + );
        }

        LineChart lineChart = new LineChart();
        lineChart.plotBG = Color.getHSBColor(0.3f, 1, 1);
        lineChart.addLine(plot1).addLine(plot2);
        lineChart.setXLabel("Commonality");
        lineChart.setYLabel("Normalised KL-Div");

        lineChart.xAxis = new LineChartAxis(new double[]{0, nPoints/2, nPoints-1});
        lineChart.yAxis = new LineChartAxis(new double[]{ss.min(), ss.max()});

        new JEasyFrame(lineChart, "KL Div Plots");
        System.out.println(ss);

    }

    static double div(double[] p, double[] q) {
        double tot = 0;
        double totp = 0;
        double totq = 0;
        for (int i=0; i<p.length; i++) {
            tot += div(p[i], q[i]);
            totp += p[i];
            totq += q[i];
        }
        // System.out.println("Check totals: " + totp + " : " + totq);
        return tot;
    }

    static double epsilon = 1e-20;

    static double div (double p, double q) {
        if (p == 0) return 0;
        double kl = p * Math.log((p+epsilon)/(q + epsilon));
        return kl;
    }
}
