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
    Transporter transit;

    Planet processIncoming(double incomingShips, int playerId) {
        if (ownedBy != playerId) {
            // this is an invasion
            // decrement the ships, then set to id of incoming player and invert sign if it has gone negative
            shipCount -= incomingShips;
            if (shipCount <= 0) {
                ownedBy = playerId;
                shipCount = Math.abs(shipCount);
                // and should make a transporter
                transit = getTransporter();
            }
        } else {
            // must be owned by this player already, so add to the tally
            shipCount += incomingShips;
        }
        return this;
    }

    public Planet update() {
        if (ownedBy != Constants.neutralPlayer) {
            shipCount += growthRate;
        }
        if (transit != null && transit.inTransit()) {
            transit.next();
            // check to see whether it has arrived and if return the target
            Planet destination = params.getCollider().getPlanetInRange(transit);

            if (destination != null) {
                // process the inbound
                destination.processIncoming(transit.payload, transit.ownedBy);
                transit.terminateJourney();
                // transit
                // System.out.println("Terminated Journey: " + transit.inTransit());
            }
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

    public boolean transitReady() {
        return getTransporter() != null && !getTransporter().inTransit();
    }


    public Transporter getTransporter() {
        // neutral planets cannot release transporters
        if (ownedBy == Constants.neutralPlayer) return null;
        // if transit is null then make a new one
        if (transit == null)
            transit = new Transporter().setParent(this);
        return transit;
    }

    public int getScore() {
        if (ownedBy == Constants.neutralPlayer) return 0;
        int score = 0;
        if (ownedBy == Constants.playerOne) score = (int) shipCount;
        if (ownedBy == Constants.playerTwo) score = (int) -shipCount;
        return score;
    }
}
