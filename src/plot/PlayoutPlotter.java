package plot;

import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class acts as a convenient wrapper around a LineChart
 * with a set of line plots.
 * <p>
 * It makes it easy to plot the rollout quality from any
 * statistical forward planning algorithm
 */

public class PlayoutPlotter implements PlayoutPlotterInterface {
    JEasyFrame frame;
    LineChart lineChart;
    LinePlot linePlot;
    ArrayList<LinePlot> linePlots;
    double currentScore;
    StatSummary scores;

    Dimension dimension = new Dimension(800, 600);

    public PlayoutPlotter setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    /*
    just call this to initialise it at the start of a plot routine
     */
    @Override
    public PlayoutPlotter startPlot(int sequenceLength) {
        lineChart = new LineChart(dimension).setBG(Color.gray);
        lineChart.xAxis = new LineChartAxis(new double[]{0, sequenceLength / 2, sequenceLength});
        lineChart.yAxis = new LineChartAxis(new double[]{-50, -25, 0, 25, 50});
        lineChart.setYLabel("Expected Score");
        lineChart.setXLabel("Rollout Depth");
        lineChart.plotBG = Color.white;


        frame = new JEasyFrame(lineChart, "Fitness versus depth");
        scores = new StatSummary();
        linePlots = new ArrayList<>();
        return this;
    }

    @Override
    public PlayoutPlotter startPlayout(double currentScore) {
        this.currentScore = currentScore;
        // scores.reset();
        linePlot = new LinePlot().setRandomColor();
        linePlot.add(currentScore);
        linePlots.add(linePlot);
        return this;
    }

    @Override
    public PlayoutPlotter addScore(double score) {
        linePlot.add(score);
        scores.add(score);
        return this;
    }

    @Override
    public PlayoutPlotter plotPlayout() {
        ArrayList<LinePlot> tmp = new ArrayList<>();
        for (LinePlot lp : linePlots) tmp.add(lp);
        lineChart.setLines(tmp);
        // immediately set a new ArrayList
        // avoid concurrent modification exceptions
        // linePlots = new ArrayList<>();
        int max = Math.max((int) scores.max(), 5);
        int min = (int) scores.min(); // Math.min((int) scores.min(), -5);
        lineChart.yAxis = new LineChartAxis(new double[]{min, max});
        lineChart.repaint();
        return this;
    }

    @Override
    public PlayoutPlotterInterface reset() {
//        lineChart.lines.clear();
//        lineChart.repaint();
        linePlots = new ArrayList<>();
        scores.reset();
        return this;
    }

}
