package hyperopt;

import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import ntuple.CompactSlidingGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpace;
import planetwar.EvoAgentSearchSpaceAsteroids;
import planetwar.GameState;
import plot.LineChart;
import plot.LineChartAxis;
import utilities.ElapsedTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestHyperParamPlanetWars {
    public static void main(String[] args) {
        int budget = 288;
        String algo = "ntbea";

        /** Get arguments */
        Map<String, List<String>> params = new HashMap<>();
        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];
            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }
                options = new ArrayList<>();
                params.put(a.substring(1), options);
            } else if (options != null) {
                options.add(a);
            } else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }
        /** Update params */
        if (params.containsKey("budget")) {
            budget = Integer.parseInt(params.get("budget").get(0));
        }
        if (params.containsKey("algo")) {
            algo = params.get("algo").get(0);
        }

        System.out.println("Optimization budget: " + budget);

//        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(1);
        GameState.includeBuffersInScore = true;

        EvoAgentSearchSpace.tickBudget = 2000;

        EvoAlg evoAlg = null;
        switch (algo) {
            case "ga":
                evoAlg = new SimpleGA();
                break;
            case "ntbea":
                evoAlg = new NTupleBanditEA().setKExplore(1);
                break;
            case "csga":
                evoAlg = new CompactSlidingGA();
                break;
            case "seda":
                evoAlg = new SlidingMeanEDA();
                break;
            case "rmhc":
                evoAlg = new SimpleRMHC(1);
                break;
            default:
                System.err.println("Can't find the algorithm " + algo + " in this framework.");
                break;
        }
        System.out.println("Will run " + algo + " with budget " + budget);

        int nChecks = 100;
        int nTrials = 100;

        ElapsedTimer timer = new ElapsedTimer();

//            LineChart lineChart = new LineChart();
//            lineChart.yAxis = new LineChartAxis(new double[]{-2, -1, 0, 1, 2});
//            lineChart.setYLabel("Fitness");


        HyperParamTuneRunner runner = new HyperParamTuneRunner();
//            runner.setLineChart(lineChart);
        runner.nChecks = nChecks;
        runner.nTrials = nTrials;
        runner.nEvals = budget;
        runner.plotChecks = 0;
        AnnotatedFitnessSpace testPlanetWars = new EvoAgentSearchSpace();
        System.out.println("Testing: " + algo);
        runner.runTrials(evoAlg, testPlanetWars);
        System.out.println("Finished testing: " + algo);
        // note, this is a bit of a hack: it only reports the final solution
        // System.out.println(new EvoAgentSearchSpace().report(runner.solution));


        // System.out.println(ntbea.getModel().s);
//        System.out.println("Time for all experiments: " + timer);
    }
}

