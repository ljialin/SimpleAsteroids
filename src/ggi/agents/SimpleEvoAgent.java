package ggi.agents;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

import java.util.Random;

public class SimpleEvoAgent implements SimplePlayerInterface {

    Random random = new Random();

    // these are all the parameters that control the agend
    public boolean flipAtLeastOneValue = true;
    public double expectedMutations = 20;
    public int sequenceLength = 200;
    public int nEvals = 20;
    public boolean useShiftBuffer = true;
    int[] solution;
    // SimplePlayerInterface opponent = new RandomAgent();
    SimplePlayerInterface opponent = new DoNothingAgent();

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

    public SimpleEvoAgent setOpponent(SimplePlayerInterface opponent) {
        this.opponent = opponent;
        return this;
    }

    public int[] getActions(AbstractGameState gameState, int playerId) {
        if (useShiftBuffer && solution != null) {
            solution = shiftLeftAndRandomAppend(solution, gameState.nActions());
        } else {
            // System.out.println("New random solution with nActions = " + gameState.nActions());
            solution = randomPoint(gameState.nActions());
        }

        for (int i = 0; i < nEvals; i++) {
            // evaluate the current one
            int[] mut = mutate(solution, expectedMutations, gameState.nActions());
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


    private int[] mutate(int[] v, double expectedMutations, int nActions) {
        int n = v.length;
        int[] x = new int[n];
        // pointwise probability of additional mutations
        double mutProb = expectedMutations / n;
        // choose element of vector to mutate
        int ix = random.nextInt(n);
        if (!flipAtLeastOneValue) {
            // setting this to -1 means it will never match the first clause in the if statement in the loop
            // leaving it at the randomly chosen value ensures that at least one bit (or more generally value) is always flipped
            ix = -1;
        }
        // copy all the values faithfully apart from the chosen one
        for (int i = 0; i < n; i++) {
            if (i == ix || random.nextDouble() < mutProb) {
                x[i] = mutateValue(v[i], nActions);
            } else {
                x[i] = v[i];
            }
        }
        return x;
    }

    private int mutateValue(int cur, int nPossible) {
        // the range is nPossible-1, since we
        // selecting the current value is not allowed
        // therefore we add 1 if the randomly chosen
        // value is greater than or equal to the current value
        if (nPossible <= 1) return cur;
        int rx = random.nextInt(nPossible - 1);
        return rx >= cur ? rx + 1 : rx;
    }

    private int[] randomPoint(int nValues) {
        int[] p = new int[sequenceLength];
        for (int i=0; i<p.length; i++) {
            p[i] = random.nextInt(nValues);
        }
        return p;
    }

    private int[] shiftLeftAndRandomAppend(int[] v, int nActions) {
        int[] p = new int[v.length];
        for (int i = 0; i < p.length - 1; i++) {
            p[i] = v[i + 1];
        }
        p[p.length - 1] = random.nextInt(nActions);
        return p;
    }

    Double discountFactor = null;

    private double evalSeq(AbstractGameState gameState, int[] seq, int playerId) {
        if (discountFactor == null) {
            return evalSeqNoDiscount(gameState, seq, playerId);
        } else {
            return evalSeqDiscounted(gameState, seq, playerId, discountFactor);
        }
    }

    private double evalSeqNoDiscount(AbstractGameState gameState, int[] seq, int playerId) {
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

    private double evalSeqDiscounted(AbstractGameState gameState, int[] seq, int playerId, double discountFactor) {
        double currentScore = gameState.getScore();
        double delta = 0;
        double discount = 1;
        int[] actions = new int[2];
        for (int action : seq) {
            actions[playerId] = action;
            actions[1 - playerId] = opponent.getAction(gameState, 1 - playerId);
            gameState = gameState.next(actions);
            double nextScore = gameState.getScore();
            double tickDelta = nextScore - currentScore;
            currentScore = nextScore;
            delta += tickDelta * discount;
            discount *= discountFactor;
        }
        if (playerId == 0)
            return delta;
        else
            return -delta;
    }

    public String toString() {
        return "SEA: " + nEvals + " : " + sequenceLength + " : " + opponent;
    }

    public int getAction(AbstractGameState gameState, int playerId) {
        return getActions(gameState, playerId)[0];
    }
}

