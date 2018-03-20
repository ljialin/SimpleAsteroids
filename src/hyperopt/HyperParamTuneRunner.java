package hyperopt;

import evodef.*;
import ga.GridSearch;
import ga.SimpleGASearchSpace;
import ntbeaplot.Plotter;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.NTupleSystem;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpace;
import planetwar.EvoAgentSearchSpaceAsteroids;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LineGroup;
import plot.LinePlot;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HyperParamTuneRunner {


    //     static int nEvals = 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpaceAsteroids().searchSpace());
    public int nEvals = 100; // 1 * (int) SearchSpaceUtil.size(new EvoAgentSearchSpace().searchSpace());
    public int nChecks = 100;
    public int nTrials = 10;

    public int plotChecks = 5;

    public LineGroup sampleEvolution = new LineGroup().setName("Sample Evolution").setColor(Color.blue);
    public LineGroup bestGuess = new LineGroup().setName("Recommendation").setColor(Color.green);
    public LineChart lineChart = new LineChart();
    // lineChart.

    public HyperParamTuneRunner setLineChart(LineChart lineChart) {
        this.lineChart = lineChart;
        return this;
    }

    public boolean verbose = false;

    public void runTrials(EvoAlg evoAlg, AnnotatedFitnessSpace annotatedFitnessSpace) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        for (int i = 0; i < nTrials; i++) {
            System.out.println("Running trial: " + (i+1));
            try {
                ss.add(runTrial(evoAlg, annotatedFitnessSpace));
                if (verbose) {
                    plotFitnessEvolution(annotatedFitnessSpace.logger(), annotatedFitnessSpace, plotChecks);
                    // annotatedFitnessSpace.logger()
//                 ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(new EvoAgentSearchSpaceAsteroids().getParams());
                    NTupleSystem nTupleSystem = ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel);
                    nTupleSystem.printDetailedReport(annotatedFitnessSpace.getParams());
                    // new Plotter().setModel(nTupleSystem).defaultPlot().plot1Tuples();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (verbose) {
            lineChart.addLineGroup(sampleEvolution);
            if (plotChecks > 0) lineChart.addLineGroup(bestGuess);
            new JEasyFrame(lineChart, "Sample Evolution");
        }
        System.out.println("nEvals per run: " + nEvals);
        System.out.println(ss);
        System.out.println("Total time for experiment: " + timer);
    }

    private void plotFitnessEvolution(EvolutionLogger logger, AnnotatedFitnessSpace eval, int plotChecks) {
        // the idea is to also add a new line group showing how the actual fitness evolved
        // versus the solutions that were sampled
        ArrayList<Double> data = new ArrayList<>();
        data.addAll(logger.fa);
//        System.out.println(data);
//        System.out.println(data.size());
        sampleEvolution.add(data);

        ArrayList<Double> bests = new ArrayList<>();
        // now this will be slow, but for now just sample every now and then ...
        for (int[] solution : logger.bestYetSolutions) {
//            System.out.println(Arrays.toString(solution));
            StatSummary ss = new StatSummary();
            for (int i = 0; i < plotChecks; i++)
                ss.add(eval.evaluate(solution));
            bests.add(ss.mean());
        }
        if (plotChecks > 0) {
            System.out.println("Bests: " + bests);
            bestGuess.add(bests);
        }


        // lineChart.addLine(new LinePlot().setData(data).setRandomColor());
    }

    private void plotConvergence(EvolutionLogger logger, int[] solution) {
        LineChart lineChart = new LineChart().setBG(Color.gray);
        lineChart.plotBG = Color.white;

        // now the plots we will add are as follows

        ArrayList<Double> bestMatches = new ArrayList<>();
        ArrayList<Double> bestCumulative = new ArrayList<>();

        ArrayList<Double> sampleMatches = new ArrayList<>();
        ArrayList<Double> sampleCumulative = new ArrayList<>();

        StatSummary cumul = new StatSummary();
        for (int[] best : logger.bestYetSolutions) {
            double x = match(best, solution);
            bestMatches.add(x);
            cumul.add(x);
            bestCumulative.add(cumul.mean());
        }

        StatSummary sample = new StatSummary();
        for (int[] sol : logger.solutions) {
            double x = match(sol, solution);
            sampleMatches.add(x);
            sample.add(x);
            sampleCumulative.add(sample.mean());
        }


        // LinePlot matchPlot = new LinePlot().
        LineGroup lgMatch = new LineGroup().setName("Best Match").setColor(Color.red).add(bestMatches);
        LineGroup lgCumul = new LineGroup().setName("Best Cumul").setColor(Color.black).add(bestCumulative);

        lineChart.addLineGroup(lgMatch);
        lineChart.addLineGroup(lgCumul);

        LineGroup lgSampleMatch = new LineGroup().setName("Sample Match").setColor(Color.green).add(sampleMatches);
        LineGroup lgSampleCumul = new LineGroup().setName("Sample Cumul").setColor(Color.magenta).add(sampleCumulative);

        lineChart.addLineGroup(lgSampleMatch);
        lineChart.addLineGroup(lgSampleCumul);


        lineChart.setXLabel("Iteration");
        lineChart.setYLabel("Candidate == solution");
        lineChart.yAxis = new LineChartAxis(new double[]{0, 1});
        lineChart.xAxis = new LineChartAxis(new double[]{0, bestMatches.size() / 2, bestMatches.size()});

        new JEasyFrame(lineChart, "NTBEA Best Guess Convergence");
    }

    double match(int[] x, int[] y) {
        StatSummary match = new StatSummary();
        // count a one in each dimension of match, zero otherwise
        for (int i = 0; i < x.length; i++) {
            match.add(x[i] == y[i] ? 1 : 0);
        }
        return match.mean();
    }

    int[] solution;

    public double runTrial(EvoAlg evoAlg, AnnotatedFitnessSpace eval) {

        // NoisySolutionEvaluator eval = new EvoAgentSearchSpace();
        // NoisySolutionEvaluator eval = new EvoAgentSearchSpaceAsteroids();

        // eval = new EvoAgentSearchSpace();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        eval.reset();
        solution = evoAlg.runTrial(eval, nEvals);

        if (verbose) {
            plotConvergence(eval.logger(), solution);
        }
        System.out.println("Checking fitness");
        return runChecks(eval, solution, nChecks);

    }

    public double runChecks(AnnotatedFitnessSpace eval, int[] solution, int nChecks) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Mean fitness");
//        System.out.println("Running checks: " + nChecks);
        for (int i = 0; i < nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
//        System.out.println(ss);
        System.out.println("Checks complete: " + timer.toString());
        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println(ss);
        System.out.println();
        // System.out.println(eval.report(solution));

        // but also find out the details
        return ss.mean();

    }
}
