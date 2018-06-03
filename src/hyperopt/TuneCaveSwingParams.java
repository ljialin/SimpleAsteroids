package hyperopt;

import caveswing.design.CaveSwingGameSearchSpapce;
import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
// import ntuple.NTupleBanditEA;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import planetwar.EvoAgentSearchSpacePlanetWars;
import planetwar.GameState;
import utilities.ElapsedTimer;

public class TuneCaveSwingParams {
    public static void main(String[] args) {
        int nEvals = 200;
        System.out.println("Optimization budget: " + nEvals);
        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(10);
        ntbea.logBestYet = true;
        NTupleSystem model = new NTupleSystem();
        // set up a non-standard tuple pattern
        model.use1Tuple = true;
        model.use2Tuple = true;
        model.useNTuple = true;

        ntbea.setModel(model);

        int nChecks = 100;
        int nTrials = 5;

        ElapsedTimer timer = new ElapsedTimer();

        HyperParamTuneRunner runner = new HyperParamTuneRunner();
        runner.verbose = true;
//            runner.setLineChart(lineChart);
        runner.nChecks = nChecks;
        runner.nTrials = nTrials;
        runner.nEvals = nEvals;
        runner.plotChecks = 5;
        AnnotatedFitnessSpace caveSwingSpapce = new CaveSwingGameSearchSpapce();
        System.out.println("Testing: " + ntbea);
        runner.runTrials(ntbea, caveSwingSpapce);
        System.out.println("Finished testing: " + ntbea);
        System.out.println("Time for all experiments: " + timer);
    }

}
