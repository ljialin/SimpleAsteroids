package hyperopt;

import caveswing.design.CaveSwingGameSearchSpace;
import evodef.AnnotatedFitnessSpace;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import utilities.ElapsedTimer;

// import ntuple.NTupleBanditEA;

public class TuneCaveSwingAgent {
    public static void main(String[] args) {
        int nEvals = 200;
        System.out.println("Optimization budget: " + nEvals);
        NTupleBanditEA ntbea = new NTupleBanditEA().setKExplore(5000);
        ntbea.logBestYet = true;
        NTupleSystem model = new NTupleSystem();
        // set up a non-standard tuple pattern
        model.use1Tuple = true;
        model.use2Tuple = true;
        model.useNTuple = true;

        ntbea.setModel(model);

        int nChecks = 50;
        int nTrials = 1;

        ElapsedTimer timer = new ElapsedTimer();

        HyperParamTuneRunner runner = new HyperParamTuneRunner();
        runner.verbose = true;
//            runner.setLineChart(lineChart);
        runner.nChecks = nChecks;
        runner.nTrials = nTrials;
        runner.nEvals = nEvals;

        // this allows plotting of the independently assessed fitness of
        // the algorithm's best guesses during each run
        // set to zero for fastest performance, set to 5 or 10 to learn
        // more about the convergence of the algorithm
        runner.plotChecks = 0;
        AnnotatedFitnessSpace caveSwingSpace = new CaveSwingGameSearchSpace();
        // uncomment to run the skilful one
        // caveSwingSpace = new CaveSwingGameSkillSpace();
        System.out.println("Testing: " + ntbea);
        runner.runTrials(ntbea, caveSwingSpace);
        System.out.println("Finished testing: " + ntbea);
        System.out.println("Time for all experiments: " + timer);
    }

}
