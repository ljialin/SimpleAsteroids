package spinbattle;

import ggi.AbstractGameState;

public class SpinGameState implements AbstractGameState {
    @Override
    public AbstractGameState copy() {
        return null;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        return null;
    }

    @Override
    public int nActions() {
        return 0;
    }

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }
}
