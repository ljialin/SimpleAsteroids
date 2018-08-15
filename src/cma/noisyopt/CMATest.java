package cma.noisyopt;

import caveswing.design.EvoAgentSearchSpaceCaveSwing;
import evodef.AnnotatedFitnessSpace;
import hyperopt.HyperParamTuneRunner;
import planetwar.EvoAgentSearchSpacePlanetWars;
import planetwar.GameState;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class CMATest {
    public static void main(String[] args) {

        ElapsedTimer t = new ElapsedTimer();
        int nTrials = 100;
        StatSummary ss = new StatSummary("CMA Overall Results");
        for (int i=0; i<nTrials; i++) {
            ss.add(runTrial());
        }

        System.out.println(ss);

        System.out.println("Total time for experiments: " + t);
    }

    public static double runTrial() {
        EvoAgentSearchSpacePlanetWars.tickBudget = 2000;
        GameState.includeBuffersInScore = true;

        // AnnotatedFitnessSpace fitnessSpace = new EvoAgentSearchSpaceCaveSwing();
        AnnotatedFitnessSpace fitnessSpace = new EvoAgentSearchSpacePlanetWars();


        IObjectiveDiscreteAdapter adapter = new IObjectiveDiscreteAdapter().setFitnessSpace(fitnessSpace);

        int evals = 288;
        CMANoisySolver solver = new CMANoisySolver(adapter, adapter.nDimensions(), evals);

        double[] rawSolution = solver.run();

        int[] solution = adapter.discretise(rawSolution);

        // System.out.println(fitnessSpace.re
        int nChecks = 30;
        ElapsedTimer timer = new ElapsedTimer();
        double result  = new HyperParamTuneRunner().runChecks(fitnessSpace, solution, nChecks);
        System.out.println(timer);

        return result;

    }

}
