package metrics;

import ggi.core.AbstractGameState;
import plot.LineChart;
import plot.LinePlot;

public class SimpleLogger implements GameLogger {


    LinePlot currentPlot;
    LineChart lineChart;

    public SimpleLogger initialise() {
        lineChart = new LineChart();
        return this;
    }

    public SimpleLogger startEpisode() {

        currentPlot = new LinePlot();

        return this;

    }

    @Override
    public GameLogger logAction(AbstractGameState state, int[] actions, GameEvent[] events) {
        currentPlot.add(state.getScore());
        return this;
    }


    static class LogEntry {
        int action;
        double score;
    }
}
