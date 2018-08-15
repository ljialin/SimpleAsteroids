package hyperopt;

import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
// import ntuple.NTupleBanditEA;
import ntbea.*;
import planetwar.EvoAgentSearchSpacePlanetWars;
import planetwar.GameState;
import utilities.ElapsedTimer;

public class TestHyperParamPlanetWars {
    public static void main(String[] args) {
        int nEvals = 288;

        if (args.length == 1) {
            nEvals = Integer.parseInt(args[0]);
        }

        System.out.println("Optimization budget: " + nEvals);

        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(2);

        NTupleSystem model = new NTupleSystem();
        // set up a non-standard tuple pattern
        model.use1Tuple = true;
        model.use2Tuple = true;
        model.useNTuple = false;

        ntbea.setModel(model);


        // ntbea.getModel().s
        GameState.includeBuffersInScore = false;

        EvoAgentSearchSpacePlanetWars.tickBudget = 2000;

        EvoAlg[] evoAlgs = {
                // new GridSearch(),
                // new CompactSlidingGA(),
                // new SlidingMeanEDA(),
//                new SimpleGA(),
//                new SimpleRMHC(1),
//                new SimpleRMHC(2),
//                new SimpleRMHC(5),
                ntbea,
        };

        int nChecks = 30;
        int nTrials = 10;

        ElapsedTimer timer = new ElapsedTimer();

        for (EvoAlg evoAlg : evoAlgs) {
//            LineChart lineChart = new LineChart();
//            lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2});
//            lineChart.setYLabel("Fitness");


            HyperParamTuneRunner runner = new HyperParamTuneRunner();
            // runner.verbose = true;
//            runner.setLineChart(lineChart);
            runner.nChecks = nChecks;
            runner.nTrials = nTrials;
            runner.nEvals = nEvals;
            runner.plotChecks = 0;
            AnnotatedFitnessSpace testPlanetWars = new EvoAgentSearchSpacePlanetWars();
            System.out.println("Testing: " + evoAlg);
            runner.runTrials(evoAlg, testPlanetWars);
            System.out.println("Finished testing: " + evoAlg);
            // note, this is a bit of a hack: it only reports the final solution
            // System.out.println(new EvoAgentSearchSpacePlanetWars().report(runner.solution));

        }

        // System.out.println(ntbea.getModel().s);
        System.out.println("Time for all experiments: " + timer);
    }
}

