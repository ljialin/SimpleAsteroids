package planetwar;

import evodef.EvoAlg;
import evodef.RegularSearchSpace;
import evodef.SearchSpaceUtil;
import evodef.SimpleGameAdapter;

/**
 *  This is a simple evolutionary planning agent
 *  that combines a game state, and evolutionary algorithm and an action sequencer -
 *  taken together they provide a way to harness an evolutionary algorithm
 *  to play a game.
 *
 *  This functionality is already implemented in the gvglink package,
 *  but this is a simpler approach that enables
 */

public class EvoAgent {

    ActionSequencer actionSequencer;
    EvoAlg evoAlg;
    SimpleGameAdapter simpleGameAdapter;
    RegularSearchSpace searchSpace;

    static int nActions = GameState.nActions;
    int sequenceLength = 20;
    static boolean useShiftBuffer = true;
    int[] solution;
    int nEvals;

    public EvoAgent setEvoAlg(EvoAlg evoAlg, int nEvals) {
        this.evoAlg = evoAlg;
        this.nEvals = nEvals;
        // set up the search space and other helpers at the same time
        actionSequencer = new ActionSequencer();
        searchSpace = new RegularSearchSpace(sequenceLength, nActions);
        simpleGameAdapter = new SimpleGameAdapter().setEvaluator(actionSequencer).setSearchSpace(searchSpace);
        return this;
    }

    public EvoAgent setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        searchSpace = new RegularSearchSpace(sequenceLength, nActions);
        simpleGameAdapter = new SimpleGameAdapter().setEvaluator(actionSequencer).setSearchSpace(searchSpace);
        return this;
    }

    public int[] getActions(GameState gameState, int playerId) {
        actionSequencer.setGameState(gameState.copy()).setPlayerId(playerId);

        if (solution != null) {
            solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, searchSpace);
            evoAlg.setInitialSeed(solution);
        }

        simpleGameAdapter.reset();
        solution = evoAlg.runTrial(simpleGameAdapter, nEvals);

        // System.out.println(Arrays.toString(solution) + "\t " + game.evaluate(solution));

        int[] tmp = solution;
        // already return the first element, so now set it to 1 ...

        if (!useShiftBuffer) solution = null;

        // return first element
        return tmp;
    }

    public int getAction(GameState gameState, int playerId) {
        return getActions(gameState, playerId)[0];
    }

}
