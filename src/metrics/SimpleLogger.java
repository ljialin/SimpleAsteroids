package metrics;

import planetwar.AbstractGameState;
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
    public void logAction(AbstractGameState state, int[] actions, GameEvent[] events) {
        currentPlot.add(state.getScore());
    }


    static class LogEntry {
        int action;
        double score;
    }
}
