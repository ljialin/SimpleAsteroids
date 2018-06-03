package caveswing.core;

import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;

public class CaveGameFactory implements AbstractGameFactory {
    @Override
    public AbstractGameState newGame() {
        return new CaveGameState().setParams(params).setup();
    }

    CaveSwingParams params = new CaveSwingParams();

    public CaveGameFactory setParams(CaveSwingParams params) {
        this.params = params;
        return this;
    }
}
