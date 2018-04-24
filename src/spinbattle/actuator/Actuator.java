package spinbattle.actuator;

import ggi.AbstractGameState;
import spinbattle.core.SpinGameState;

public interface Actuator {
    SpinGameState actuate(int action, SpinGameState gameState);
    Actuator copy();
}
