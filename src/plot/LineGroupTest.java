package plot;

import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;

public class LineGroupTest {
    public static void main(String[] args) {
        int nTrials = 10;
        int nPoints = 20;
        LineGroup lineGroup = new LineGroup().setColor(Color.blue).setName("Rand Sine");
        LineChart lineChart = new LineChart().addLineGroup(lineGroup);
        lineChart.setBG(Color.white);
        lineChart.fg = Color.black;
        // lineChart.

        for (int i=0; i<nTrials; i++) {
            ArrayList<Double> data = new ArrayList<>();
            for (int j=0; j<=nPoints; j++) {
                double y = 1000 * (0.5 * Math.sin( j * 4 * Math.PI / nPoints) + Math.random() * 0.4 - 0.2);
                data.add(y);
            }
            // System.out.println(data);
            lineGroup.add(data);
            // lineChart.addLine(new LinePlot().setData(data).setRandomColor());
        }
        // lineChart.set
        // lineChart.xAxis = new LineChartAxis(new double[]{0, 5, 10, 15, 20});
        lineChart.xAxis = new LineChartAxis(new double[]{0, 5, 10, 15, 30});
        // lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2});

        new JEasyFrame(lineChart, "Line Group GVGAISimpleTest");


    }
}
