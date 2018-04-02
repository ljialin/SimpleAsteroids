package spinbattle;

import math.Vector2d;

public class Planet {
    Vector2d position;
    double rotation;

    Planet setRandomLocation(SpinBattleParams p) {
        position = new Vector2d(p.getRandom().nextDouble() * p.width, p.getRandom().nextDouble() * p.height);
        return this;
    }

    

}
