package hyperopt;

import evodef.AnnotatedFitnessSpace;
import evodef.EvoAlg;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import planetwar.EvoAgentSearchSpaceAsteroids;

public class TestHyperParamAsteroids {
    public static void main(String[] args) {
        AnnotatedFitnessSpace testAsteroids = new EvoAgentSearchSpaceAsteroids();

        EvoAlg[] evoAlgs = {
                new NTupleBanditEA().setKExplore(10000),
                // new GridSearch(),
                // new CompactSlidingGA(),
                new SlidingMeanEDA(),
        };

        int nChecks = 50;
        int nEvals = 50;
        int nTrials = 2;
        for (EvoAlg evoAlg : evoAlgs) {
            HyperParamTuneRunner runner = new HyperParamTuneRunner();
            runner.nChecks = nChecks;
            runner.nTrials = nTrials;
            runner.nEvals = nEvals;
            runner.runTrials(evoAlg, testAsteroids);
        }
    }
}
