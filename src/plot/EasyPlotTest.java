package plot;

import utilities.JEasyFrame;

import java.util.ArrayList;

public class EasyPlotTest {
    public static void main(String[] args) {
        int n = 100;
        ArrayList<Double> data = new ArrayList<>();
        for (int i=0; i<n; i++) data.add(Math.random()/ 10);
        LineChart lineChart = LineChart.easyPlot(data);
        lineChart.yAxis = new LineChartAxis(0, 1, 6);
        // lineChart.yAxis.ticks = new double[]{0, 0.1, 0.5, 1.0};
        new JEasyFrame(lineChart, "Easy Plot GVGAISimpleTest");
    }
}


