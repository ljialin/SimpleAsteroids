package caveswing.core;

public interface Actuator {
    // transforms the action specified by the planning algorithm
    // into an action taken in the game
    CaveGameState actuate(int action, CaveGameState gameState);
    Actuator copy();
}
