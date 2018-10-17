package plot;

import utilities.JEasyFrame;

import java.awt.*;

public class LineChartTest {
    public static void main(String[] args) {
                LineChart lineChart = new LineChart();
        double[] y = new double[101];
        double[] yy = new double[101];
        for (int i = 0; i < y.length; i++) {
            y[i] = Math.abs(50 - i);
            yy[i] = i;
        }
        lineChart.addLine(LinePlot.randomLine());
        lineChart.addLine(LinePlot.randomLine());
        lineChart.addLine(new LinePlot().setData(y).setColor(Color.blue));
        lineChart.addLine(new LinePlot().setData(yy).setColor(Color.red));
        new JEasyFrame(lineChart, "Line Chart GVGAISimpleTest");
        lineChart.xAxis = new LineChartAxis(new double[]{0, 20, 40, 60, 80, 100});
        lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2, 3});


        lineChart = new LineChart().addLine(LinePlot.randomLine());
        // lineChart.yAxis = new LineChartAxis(new double[]{-2, 0, 2});
        lineChart.xAxis = new LineChartAxis(new double[]{0, 20, 40, 60, 80, 100});

        new JEasyFrame(lineChart, "Single Line GVGAISimpleTest");

    }
}
