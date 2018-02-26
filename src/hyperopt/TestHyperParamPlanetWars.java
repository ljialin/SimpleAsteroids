package hyperopt;

import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpace;
import planetwar.EvoAgentSearchSpaceAsteroids;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.ElapsedTimer;

public class TestHyperParamPlanetWars {
    public static void main(String[] args) {

        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(2);

        EvoAlg[] evoAlgs = {
                // new GridSearch(),
                // new CompactSlidingGA(),
                // new SlidingMeanEDA(),
                ntbea,
        };

        int nChecks = 50;
        int nEvals = 200;
        int nTrials = 1;

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
            runner.runTrials(evoAlg, testPlanetWars );
        }

        // System.out.println(ntbea.getModel().s);
        System.out.println("Time for all experiments: " + timer);
    }
}
