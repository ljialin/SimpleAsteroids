package ntuple.tests;

import evodef.DefaultMutator;
import ga.SimplestRMHC;
import ntuple.ConvNTuple;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LineGroup;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;

import static ntuple.tests.EvolveMarioLevelTest.getAndShowLevel;
import static ntuple.tests.EvolveMarioLevelTest.inputFile1;

public class MarioEvolutionPlots {

    public static void main(String[] args) throws Exception {

        int[][] level = getAndShowLevel(true, inputFile1);

        EvolveMarioLevelTest.filterWidth = 2;
        EvolveMarioLevelTest.filterWidth = 2;
        EvolveMarioLevelTest.useConvMutator = false;

        ArrayList<Double> f2reg = runEvo(level);
        EvolveMarioLevelTest.useConvMutator = true;
        ArrayList<Double> f2conv = runEvo(level);

        EvolveMarioLevelTest.filterWidth = 4;
        EvolveMarioLevelTest.filterHeight = 4;
        EvolveMarioLevelTest.useConvMutator = false;

        ArrayList<Double> f4reg = runEvo(level);
        EvolveMarioLevelTest.useConvMutator = true;
        ArrayList<Double> f4conv = runEvo(level);

        LineGroup lg1 = new LineGroup().setColor(Color.blue).setName("2 x 2 Reg");
        lg1.add(f2reg);

        LineGroup lg2 = new LineGroup().setColor(Color.green).setName("2 x 2 Cnv");
        lg2.add(f2conv);

        LineGroup lg3 = new LineGroup().setColor(Color.red).setName("4 x 4 Reg");
        lg3.add(f4reg);

        LineGroup lg4 = new LineGroup().setColor(Color.black).setName("4 x 4 Cnv");
        lg4.add(f4conv);

        LineChart lineChart = new LineChart().addLineGroup(lg1);
        lineChart.addLineGroup(lg2).addLineGroup(lg3).addLineGroup(lg4);
        lineChart.setBG(Color.white);

        lineChart.yAxis = new LineChartAxis(new double[]{-15, -10, -5, 0});
        lineChart.xAxis = new LineChartAxis(new double[]{0, 5000, 10000});

        lineChart.title = "Evolution Traces (Filter Size, Mutation Type";

        new JEasyFrame(lineChart, "Evolution Traces (Filter Size, Mutation Type");

    }

    static ArrayList<Double> runEvo(int[][] level) {
        EvolveMarioLevelTest evolver = new EvolveMarioLevelTest();
        ConvNTuple.w = 0.5;
        int nEvals = 10001;

        DefaultMutator mutator = new DefaultMutator(null);
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 2;
        // mutator.totalRandomChaosMutation = true;

        mutator.setSwap(false);
        System.out.println("Trial: " + 1);
        ElapsedTimer timer = new ElapsedTimer();
        SimplestRMHC simpleRMHC = new SimplestRMHC();
        simpleRMHC.setMutator(mutator);
        evolver.runTrial(simpleRMHC, nEvals, level, 0);
        // System.out.println(simpleRMHC.getLogger().fa);
        System.out.println(timer);
        System.out.println();
        return evolver.evoTrace;
    }


}
