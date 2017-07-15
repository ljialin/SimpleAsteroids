package plot;

import utilities.StatSummary;

import java.util.List;

/**
 * Created by sml on 14/07/2017.
 */
public class LineChartAxis {
    List<String> labels;
    public StatSummary range;
    int nTicks = 0;
    double[] ticks;

    public LineChartAxis(double min, double max) {
        this(min, max, 0);
    }

    public LineChartAxis(double min, double max, int nTicks) {
        range = new StatSummary();
        range.add(min);
        range.add(max);
        ticks = new double[nTicks];
        double step = (max - min) / (nTicks - 1);
        double cur = min;
        for (int i=0; i<nTicks; i++, cur += step) {
            ticks[i] = cur;
        }
    }

    public LineChartAxis(double[] ticks) {
        this.ticks = ticks;
        nTicks = ticks.length;
        range = new StatSummary();
        range.add(ticks);
    }
}
