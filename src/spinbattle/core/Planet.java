package spinbattle.core;

import levelgen.MarioReader;
import math.Vector2d;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;


public class Planet {
    public Vector2d position;
    public double rotation;
    public double rotationRate;

    public double growthRate;

    public double shipCount;
    public int ownedBy;
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

    Planet update() {
        if (ownedBy != Constants.neutralPlayer) {
            shipCount += growthRate;
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

    Planet setOwnership(int ownedBy) {
        this.ownedBy = ownedBy;
        // also set initial ships
        shipCount = params.minInitialShips +
                params.getRandom().nextInt(params.maxInitialShips - params.minInitialShips);
        return this;
    }

    public Planet copy() {
        Planet planet = new Planet();
        planet.position = position.copy();
        planet.rotation = rotation;
        planet.rotationRate = rotationRate;
        return planet;
    }

    public Planet setRandomGrowthRate() {
        growthRate = params.getRandom().nextDouble() * (params.maxGrowth - params.minGrowth) + params.minGrowth;
        return this;
    }

    public int getRadius() {
        return (int) (Constants.growthRateToRadius * growthRate);
    }

    public String toString() {
        return position + " : " + ownedBy + " : " + getRadius();
    }

}
