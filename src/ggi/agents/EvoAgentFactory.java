package ggi.agents;

import agents.evo.EvoAgent;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;

public class EvoAgentFactory {

    public double mutationRate = 10;
    public boolean totalRandomMutation = false;
    public boolean useShiftBuffer = true;

    public int nEvals = 20;
    public int seqLength = 200;

    public EvoAgent getAgent() {
        //
        // todo Add in the code t make this
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        mutator.pointProb = mutationRate;
        mutator.totalRandomChaosMutation = totalRandomMutation;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        // evoAlg = new SlidingMeanEDA();

        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setUseShiftBuffer(useShiftBuffer);
        // evoAgent.setVisual();

        return evoAgent;
    }
}
