package ggi.core;

import ggi.core.AbstractGameState;

public interface SimplePlayerInterface {
    int getAction(AbstractGameState gameState, int playerId);
    SimplePlayerInterface reset();
}

