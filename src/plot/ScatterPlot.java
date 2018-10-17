package plot;

import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 18/07/2017.
 */
public class ScatterPlot {
    String title;
    LineChartAxis xAxis, yAxis;

    StatSummary sx, sy;
    ArrayList<DataPoint> points;

    public static void main(String[] args) {
        Random random = new Random();
        ScatterPlot scatterPlot = new ScatterPlot().setTitle("Gaussian GVGAISimpleTest");
        for (int i=0; i<50; i++) {
            scatterPlot.addPoint(new DataPoint(String.format("%d",i), random.nextGaussian(), random.nextGaussian()));
        }
        LineChart lineChart = new LineChart(new Dimension(800, 800)).setTitle(scatterPlot.title);

        // now add the scatterPlot

        lineChart.xAxis = new LineChartAxis(new double[]{-4, -2, 0, 2, 4});
        // lineChart.yAxis = new LineChartAxis(new double[]{40, 50, 60, 70, 80, 90, 100});
        //
        lineChart.yAxis = new LineChartAxis(new double[]{-4, -2, 0, 2, 4});

        lineChart.setScatterPlot(scatterPlot);

        new JEasyFrame(lineChart, "Scatter GVGAISimpleTest");



    }

    public ScatterPlot() {
        sx = new StatSummary();
        sy = new StatSummary();
        points = new ArrayList<>();
    }

    public ScatterPlot setTitle(String title) {
        this.title = title;
        return this;
    }

    public ScatterPlot setXAxis(LineChartAxis xAxis) {
        this.xAxis = xAxis;
        return this;
    }

    public ScatterPlot setYAxis(LineChartAxis yAxis) {
        this.xAxis = xAxis;
        return this;
    }

    public void addPoint(DataPoint point) {
        points.add(point);
    }

}
