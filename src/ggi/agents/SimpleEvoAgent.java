package ggi.agents;

import agents.dummy.RandomAgent;
import evodef.*;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

public class SimpleEvoAgent implements SimplePlayerInterface {

    double mutationRate = 10;
    int sequenceLength = 100;
    int nEvals = 20;
    boolean useShiftBuffer = true;
    int[] solution;

    public SimplePlayerInterface reset() {
        solution = null;
        return this;
    }

    public SimpleEvoAgent setUseShiftBuffer(boolean useShiftBuffer) {
        this.useShiftBuffer = useShiftBuffer;
        return this;
    }

    public SimpleEvoAgent setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        return this;
    }

    SimplePlayerInterface opponent = new RandomAgent();

    public SimpleEvoAgent setOpponent(SimplePlayerInterface opponent) {
        this.opponent = opponent;
        return this;
    }

    public int[] getActions(AbstractGameState gameState, int playerId) {
        SearchSpace searchSpace = new RegularSearchSpace(sequenceLength, gameState.nActions());
        if (useShiftBuffer && solution != null) {
            solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, searchSpace);
        } else {
            solution = SearchSpaceUtil.randomPoint(searchSpace);
        }

        // we now need to step the model forward at random
        Mutator mutator = new DefaultMutator(searchSpace);
        ((DefaultMutator) mutator).pointProb = mutationRate;

        // now make the iterations
        for (int i=0; i<nEvals; i++) {
            // evaluate the current one
            int[] mut = mutator.randMut(solution);
            // now evaluate the change in fitness
        }



        int[] tmp = solution;
        // nullify if not using a shift buffer
        if (!useShiftBuffer) solution = null;
        return tmp;
    }

    // double delta

    public String toString() {
        return "SimpleEvoAgent: " + " : " + nEvals + " : " + sequenceLength;
    }

    public int getAction(AbstractGameState gameState, int playerId) {
        return getActions(gameState, playerId)[0];
    }


}
