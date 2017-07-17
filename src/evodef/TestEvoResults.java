package evodef;

import plot.LineGroup;
import plot.LinePlot;
import utilities.StatSummary;

import java.util.List;

/**
 * Created by simonmarklucas on 10/07/2017.
 */
public class TestEvoResults {
    public String name;

    // this class is

    StatSummary trueOpt;
    public List<LinePlot> linePlots;

    public TestEvoResults(String name, StatSummary trueOpt, List<LinePlot> linePlots) {
        this.name = name;
        this.trueOpt = trueOpt;
        this.linePlots = linePlots;
    }

    public TestEvoResults(String name, StatSummary trueOpt) {
        this.name = name;
        this.trueOpt = trueOpt;
    }

    public LineGroup getLineGroup() {
        LineGroup lineGroup = new LineGroup();
        for (LinePlot linePlot : linePlots) {
            lineGroup.add(linePlot.getData());
        }
        return lineGroup;
    }

}
