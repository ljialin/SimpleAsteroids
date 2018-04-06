package spinbattle.core;

import ggi.AbstractGameState;
import spinbattle.params.SpinBattleParams;

public class SpinGameState implements AbstractGameState {

    // this tracks all calls to the next method
    // useful for calculating overall stats

    public static int totalTicks;

    // number of ticks made by this instance
    int nTicks;

    // may set a limit on the game length
    // this will be used in the isTerminal() method
    int maxTicks;
    private SpinBattleParams params;


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

    public SpinGameState setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    // todo - set up the planets based on the params that have been passed


}
