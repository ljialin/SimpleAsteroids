package hyperopt;

import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpace;
import planetwar.EvoAgentSearchSpaceAsteroids;
import planetwar.GameState;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.ElapsedTimer;

public class TestHyperParamPlanetWars {
    public static void main(String[] args) {

        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(1);
        GameState.includeBuffersInScore = true;

        EvoAgentSearchSpace.tickBudget = 2000;


        EvoAlg[] evoAlgs = {
                // new GridSearch(),
                // new CompactSlidingGA(),
                // new SlidingMeanEDA(),
                new SimpleGA(),
                new SimpleRMHC(1),
                new SimpleRMHC(2),
                new SimpleRMHC(3),
                ntbea,
        };

        int nChecks = 50;

        int nEvals = 288;

        int nTrials = 2;

        ElapsedTimer timer = new ElapsedTimer();

        for (EvoAlg evoAlg : evoAlgs) {
            LineChart lineChart = new LineChart();
            lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2});
            lineChart.setYLabel("Fitness");


            HyperParamTuneRunner runner = new HyperParamTuneRunner();
            runner.setLineChart(lineChart);
            runner.nChecks = nChecks;
            runner.nTrials = nTrials;
            runner.nEvals = nEvals;
            runner.plotChecks = 0;
            AnnotatedFitnessSpace testPlanetWars = new EvoAgentSearchSpace();
            System.out.println("Testing: " + evoAlg);
            runner.runTrials(evoAlg, testPlanetWars);
            System.out.println("Finished testing: " + evoAlg);
            // note, this is a bit of a hack: it only reports the final solution
            // System.out.println(new EvoAgentSearchSpace().report(runner.solution));

        }

        // System.out.println(ntbea.getModel().s);
        System.out.println("Time for all experiments: " + timer);
    }
}

