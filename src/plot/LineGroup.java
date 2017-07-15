package plot;

import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Simon Lucas on 15/07/2017.
 */
public class LineGroup {
    ArrayList<StatSummary> stats;
    public Color color = Color.white;

    public LineGroup() {
        stats = new ArrayList<>();
    }

    public LineGroup setColor(Color color) {
        this.color = color;
        return this;
    }

    public LineGroup add(double[] data) {
        return this;
    }

    public LineGroup add(ArrayList<Double> data) {
        for (int i=0; i<data.size(); i++) {
            add(data.get(i), i);
        }
        return this;
    }

    private void add(double x, int i) {
        // add enough new stat summaries
        while (stats.size() <= i) {
            stats.add(new StatSummary());
        }
        // now we can just add it
        stats.get(i).add(x);
    }

    public ArrayList<StatSummary> getLineStats() {
        return stats;
    }

}
