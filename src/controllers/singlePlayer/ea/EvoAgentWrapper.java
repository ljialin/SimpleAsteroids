package controllers.singlePlayer.ea;

import agents.evo.EvoAgent;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.agents.SimpleEvoAgent;
import ggi.core.SimplePlayerInterface;
import gvglink.ggi.GVGSinglePlayerAdapter;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class EvoAgentWrapper extends AbstractPlayer {

    public static boolean visual = true;
    SimplePlayerInterface evoAgent;

    // the static declaration is just to make it easy to get the timing stats
    // from the GVGAI Test class main method
    public static StatSummary ss;

    public EvoAgentWrapper(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        evoAgent = getEvoAgent();
        ss = new StatSummary();
    }

    public Types.ACTIONS act(StateObservation gvgState, ElapsedCpuTimer elapsedTimer) {


        GVGSinglePlayerAdapter adapter = new GVGSinglePlayerAdapter().setState(gvgState);

        ElapsedTimer t = new ElapsedTimer();
        int action = evoAgent.getAction(adapter, 0);

        Types.ACTIONS gvgAction = gvgState.getAvailableActions().get(action);

        long elapsed = t.elapsed();

        System.out.println(adapter.getScore() + " \t: " + elapsed + "\t " + gvgState.isGameOver());

        ss.add(elapsed);

        return gvgAction;
    }


    static boolean useSimpleAgent = false;

    static SimplePlayerInterface getEvoAgent() {
        if (useSimpleAgent) {
            return getSimpleEvoAgent();
        } else {
            return getEvoAgentOld();
        }
    }

    static SimplePlayerInterface getSimpleEvoAgent() {
        //


        SimpleEvoAgent evoAgent = new SimpleEvoAgent();
        // evoAgent.
        evoAgent.sequenceLength = 40;
        evoAgent.nEvals = 10;
        evoAgent.flipAtLeastOneValue = true;
        evoAgent.expectedMutations = 3;
        return evoAgent;
    }

    static SimplePlayerInterface getEvoAgentOld() {
        //
        int nResamples = 5;

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
