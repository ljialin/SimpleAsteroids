package evodef;

import utilities.LinePlot;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simonmarklucas on 10/07/2017.
 */
public class TestEvoResults {
    String name;

    // this class is

    StatSummary trueOpt;
    List<LinePlot> linePlots;

    public TestEvoResults(String name, StatSummary trueOpt, List<LinePlot> linePlots) {
        this.name = name;
        this.trueOpt = trueOpt;
        this.linePlots = linePlots;
    }

    public TestEvoResults(String name, StatSummary trueOpt) {
        this.name = name;
        this.trueOpt = trueOpt;
    }

}
