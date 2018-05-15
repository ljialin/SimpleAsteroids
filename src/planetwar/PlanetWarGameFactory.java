package planetwar;

import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;

public class PlanetWarGameFactory implements AbstractGameFactory{
    @Override
    public AbstractGameState newGame() {
        return new GameState().defaultState();
    }
}
