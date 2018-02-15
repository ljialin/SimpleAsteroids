package hyperopt;

import evodef.*;
import ga.GridSearch;
import ga.SimpleGASearchSpace;
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

    public void runTrials(EvoAlg evoAlg, AnnotatedFitnessSpace annotatedFitnessSpace) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Overall results: " + evoAlg.getClass().getSimpleName());
        for (int i = 0; i < nTrials; i++) {
            try {
                ss.add(runTrial(evoAlg, annotatedFitnessSpace));
                plotFitnessEvolution(annotatedFitnessSpace.logger(), annotatedFitnessSpace, plotChecks);
                // annotatedFitnessSpace.logger()
//                 ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(new EvoAgentSearchSpaceAsteroids().getParams());
                // ((NTupleSystem) ((NTupleBanditEA) evoAlg).banditLandscapeModel).printDetailedReport(annotatedFitnessSpace.getParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        lineChart.addLineGroup(sampleEvolution);
        if (plotChecks > 0) lineChart.addLineGroup(bestGuess);
        new JEasyFrame(lineChart, "Sample Evolution");
        System.out.println("nEvals per run: " + nEvals);
        System.out.println(ss);
        System.out.println("Total time for experiment: " + timer);
    }

    private void plotFitnessEvolution(EvolutionLogger logger, AnnotatedFitnessSpace eval, int plotChecks) {
        // the idea is to also add a new line group showing how the actual fitness evolved
        // versus the solutions that were sampled
        ArrayList<Double> data = new ArrayList<>();
        data.addAll(logger.fa);
        System.out.println(data);
        System.out.println(data.size());
        sampleEvolution.add(data);

        ArrayList<Double> bests = new ArrayList<>();
        // now this will be slow, but for now just sample every now and then ...
        for (int[] solution : logger.bestYetSolutions) {
            StatSummary ss = new StatSummary();
            for (int i=0; i<plotChecks; i++)
                ss.add(eval.evaluate(solution));
            bests.add(ss.mean());
        }
        if (plotChecks > 0) {
            System.out.println("Bests: " + bests);
            bestGuess.add(bests);
        }

        // lineChart.addLine(new LinePlot().setData(data).setRandomColor());
    }

    public double runTrial(EvoAlg evoAlg, AnnotatedFitnessSpace eval) {

        // NoisySolutionEvaluator eval = new EvoAgentSearchSpace();
        // NoisySolutionEvaluator eval = new EvoAgentSearchSpaceAsteroids();

        // eval = new EvoAgentSearchSpace();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        eval.reset();
        int[] solution = evoAlg.runTrial(eval, nEvals);

        System.out.println("Checking fitness");
        return runChecks(eval, solution, nChecks);

    }

    public double runChecks(AnnotatedFitnessSpace eval, int[] solution, int nChecks) {
        ElapsedTimer timer = new ElapsedTimer();
        StatSummary ss = new StatSummary("Mean fitness");
        System.out.println("Running checks: " + nChecks);
        for (int i = 0; i < nChecks; i++) {
            ss.add(eval.evaluate(solution));
        }
        System.out.println(ss);
        System.out.println("Checks complete: " + timer.toString());
        System.out.println("Solution: " + Arrays.toString(solution));
        System.out.println();
        // System.out.println(eval.report(solution));

        // but also find out the details
        return ss.mean();

    }
}
