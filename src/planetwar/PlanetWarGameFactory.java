package planetwar;

import ggi.AbstractGameFactory;
import ggi.AbstractGameState;

public class PlanetWarGameFactory implements AbstractGameFactory{
    @Override
    public AbstractGameState newGame() {
        return new GameState().defaultState();
    }
}
