package agents.evo;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import caveswing.core.CaveGameState;
import evodef.EvoAlg;
import evodef.RegularSearchSpace;
import evodef.SearchSpaceUtil;
import evodef.SimpleGameAdapter;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import plot.NullPlayoutPlotter;
import plot.PlayoutPlotter;
import plot.PlayoutPlotterInterface;
import spinbattle.core.SpinGameState;

import java.awt.*;
import java.util.Arrays;

/**
 *  This is a simple evolutionary planning agent
 *  that combines a game state, and evolutionary algorithm and an action sequencer -
 *  taken together they provide a way to harness an evolutionary algorithm
 *  to play a game.
 *
 *  This functionality is already implemented in the gvglink package,
 *  but this is a simpler approach that enables
 */

public class EvoAgent implements SimplePlayerInterface {

    ActionSequencer actionSequencer;
    public EvoAlg evoAlg;
    public SimpleGameAdapter simpleGameAdapter;
    RegularSearchSpace searchSpace;

    // static int nActions = AsteroidsGameState.nActions;
    public int sequenceLength = 20;
    public boolean useShiftBuffer = true;
    public int[] solution;
    public int nEvals;

    PlayoutPlotterInterface playoutPlotter = new NullPlayoutPlotter();

    public SimplePlayerInterface reset() {
        solution = null;
        actionSequencer = new ActionSequencer();

        // todo: find the BUG!!!
        // System.out.println("Called reset!!!!");
        return this;
    }

    public EvoAgent setEvoAlg(EvoAlg evoAlg, int nEvals) {
        this.evoAlg = evoAlg;
        this.nEvals = nEvals;
        // set up the search space and other helpers at the same time
        actionSequencer = new ActionSequencer();
        return this;
    }

    public EvoAgent setUseShiftBuffer(boolean useShiftBuffer) {
        this.useShiftBuffer = useShiftBuffer;
        return this;
    }

    Dimension dimension = new Dimension(800, 600);

    public EvoAgent setDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    public EvoAgent setVisual() {
        playoutPlotter = new PlayoutPlotter().setDimension(dimension);
        playoutPlotter.startPlot(sequenceLength);
        return this;
    }

    public EvoAgent setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        return this;
    }

    // default to doing nothing, but random may be better
    SimplePlayerInterface opponent = new DoNothingAgent();

    public EvoAgent setOpponent(SimplePlayerInterface opponent) {
        this.opponent = opponent;
        return this;
    }

    public int[] getActions(AbstractGameState gameState, int playerId) {
        searchSpace = new RegularSearchSpace(sequenceLength, gameState.nActions());
        simpleGameAdapter = new SimpleGameAdapter().setEvaluator(actionSequencer).setSearchSpace(searchSpace);
        actionSequencer.setGameState(gameState.copy()).setPlayerId(playerId).setOpponent(opponent);
        actionSequencer.playoutPlotter = playoutPlotter;
        playoutPlotter.reset();
        if (solution != null) {
            solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, searchSpace);
            evoAlg.setInitialSeed(solution);
        }
        simpleGameAdapter.reset();
        solution = evoAlg.runTrial(simpleGameAdapter, nEvals);

        playoutPlotter.plotPlayout();
        // System.out.println(">> " + simpleGameAdapter.evaluate(solution) + "\t " + Arrays.toString(solution));
        // System.out.println(Arrays.toString(solution) + "\t " + game.evaluate(solution));
        int[] tmp = solution;
        // already return the first element, so now set it to 1 ...
        if (!useShiftBuffer) solution = null;
        // CaveGameState cgs = (CaveGameState) gameState;
        // System.out.println("Gravity check: " + cgs.params.gravity + " H: " + cgs.params.hooke);
        // System.out.println();
        return tmp;
    }

    public String toString() {
        return "EA: " + evoAlg.getClass().getSimpleName() + " : " + nEvals + " : " + sequenceLength;
    }

    public int getAction(AbstractGameState gameState, int playerId) {
        return getActions(gameState, playerId)[0];
    }

}

