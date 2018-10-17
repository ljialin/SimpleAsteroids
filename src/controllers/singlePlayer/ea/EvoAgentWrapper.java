package controllers.singlePlayer.ea;

import agents.evo.EvoAgent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.core.SimplePlayerInterface;
import gvglink.ggi.GVGSinglePlayerAdapter;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class EvoAgentWrapper extends AbstractPlayer {

    public static boolean visual = false;
    SimplePlayerInterface evoAgent;

    // the static declaration is just to make it easy to get the timing stats
    // from the GVGAI Test class main method
    public static StatSummary ss;

    public EvoAgentWrapper(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        evoAgent = getEvoAgent();
        ss = new StatSummary();
    }

    public Types.ACTIONS act(StateObservation gvgState, ElapsedCpuTimer elapsedTimer) {

        ElapsedTimer t = new ElapsedTimer();
        GVGSinglePlayerAdapter adapter = new GVGSinglePlayerAdapter().setState(gvgState);

        int action = evoAgent.getAction(adapter, 0);
        Types.ACTIONS gvgAction = gvgState.getAvailableActions().get( action );

        ss.add(t.elapsed());
        return gvgAction;
    }

    static SimplePlayerInterface getEvoAgent() {
        //
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        // mutator.totalRandomChaosMutation = true;
        mutator.pointProb = 10;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        int nEvals = 10;
        int seqLength = 50;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        boolean useShiftBuffer = true;
        evoAgent.setUseShiftBuffer(useShiftBuffer);
        if (visual) evoAgent.setVisual();
        return evoAgent;
    }
}
