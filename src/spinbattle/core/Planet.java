package spinbattle.core;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;


public class Planet {
    Vector2d position;
    double rotation;
    double rotationRate;

    double growthRate;

    double shipCount;
    int ownedBy;
    SpinBattleParams params;

    Planet processIncoming(double incomingShips, int playerId) {
        if (ownedBy != playerId) {
            // this is an invasion
            // decrement the ships, then set to id of incoming player and invert sign if it has gone negative
            shipCount -= incomingShips;
            if (shipCount <= 0) {
                ownedBy = playerId;
                shipCount = Math.abs(shipCount);
            }
        } else {
            // must be owned by this player already, so add to the tally
            shipCount += incomingShips;
        }
        return this;
    }

    public Planet setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    Planet setRandomLocation(SpinBattleParams p) {
        position = new Vector2d(p.getRandom().nextDouble() * p.width, p.getRandom().nextDouble() * p.height);
        return this;
    }

    public Planet copy() {
        Planet planet = new Planet();
        planet.position = position.copy();
        planet.rotation = rotation;
        planet.rotationRate = rotationRate;
        return planet;
    }









}
