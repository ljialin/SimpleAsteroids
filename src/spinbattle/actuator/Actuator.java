package spinbattle.actuator;

import spinbattle.core.SpinGameState;

public interface Actuator {
    SpinGameState actuate(int action, SpinGameState gameState);
    Actuator copy();
}
