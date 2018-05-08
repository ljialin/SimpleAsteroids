package ggi;


/**
 * Objects of this class make game states
 */
public interface AbstractGameFactory {
    AbstractGameState newGame();
}
