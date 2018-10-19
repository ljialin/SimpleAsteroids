package ggi.agents;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import evodef.*;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

public class SimpleEvoAgent implements SimplePlayerInterface {

    double mutationRate = 20;
    int sequenceLength = 200;
    int nEvals = 40;
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

    // SimplePlayerInterface opponent = new RandomAgent();
    SimplePlayerInterface opponent = new DoNothingAgent();

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
        DefaultMutator mutator = new DefaultMutator(searchSpace);
        mutator.pointProb = mutationRate;
        mutator.flipAtLeastOneValue = true;

        // mutator.

        // double bestYet = evalSeq(gameState, solution, playerId);
        // now make the iterations
        for (int i = 0; i < nEvals; i++) {
            // evaluate the current one
            int[] mut = mutator.randMut(solution);
            double curScore = evalSeq(gameState.copy(), solution, playerId);
            double mutScore = evalSeq(gameState.copy(), mut, playerId);
            if (mutScore >= curScore) {
                solution = mut;
            }
        }

        int[] tmp = solution;
        // nullify if not using a shift buffer
        if (!useShiftBuffer) solution = null;
        return tmp;
    }

    double evalSeq(AbstractGameState gameState, int[] seq, int playerId) {
        double current = gameState.getScore();
        int[] actions = new int[2];
        for (int action : seq) {
            actions[playerId] = action;
            actions[1 - playerId] = opponent.getAction(gameState, 1 - playerId);
            gameState = gameState.next(actions);
        }
        double delta = gameState.getScore() - current;
        if (playerId == 0)
            return delta;
        else
            return -delta;
    }

    public String toString() {
        return "SimpleEvoAgent: " + " : " + nEvals + " : " + sequenceLength;
    }

    public int getAction(AbstractGameState gameState, int playerId) {
        return getActions(gameState, playerId)[0];
    }


}
