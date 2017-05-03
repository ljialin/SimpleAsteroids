package evogame;

import evogame.evotypes.EvoDouble;

/**
 * Created by simonmarklucas on 22/05/2016.
 */
public class EvolvableParams implements ParamValues {



    // use intuition to choose uniform or mid-point random
    // values
    EvoDouble bigRad = new EvoDouble(20, 50).initMid();
    EvoDouble midRad = new EvoDouble(10, 30).initMid();
    EvoDouble smallRad = new EvoDouble(5, 20).initMid();

    EvoDouble minSpeed = new EvoDouble(1, 5).initMid();
    EvoDouble missileRadius = new EvoDouble(1, 20).initMid();
    EvoDouble ttl = new EvoDouble(10, 100).initUniform();

    EvoDouble shipSize = new EvoDouble(2, 30).initUniform();

    EvoDouble shipLoss = new EvoDouble(0.9, 1.0).initUniform();
    EvoDouble shipSteer = new EvoDouble(1, 22.5).initUniform();

    @Override
    public int getBigRadius() {
        return bigRad.intValue();
    }

    @Override
    public int getMidRadius() {
        return midRad.intValue();
    }

    @Override
    public int getSmallRadius() {
        return smallRad.intValue();
    }

    @Override
    public double getMissileMinVelocity() {
        return minSpeed.doubleValue();
    }

    @Override
    public int getMissileRadius() {
        return missileRadius.intValue();
    }

    @Override
    public int getMissileTTL() {
        return ttl.intValue();
    }

    public int getShipSize() { return shipSize.intValue(); }

    @Override
    public double getShipLoss() {
        return shipLoss.doubleValue();
    }

    @Override
    public double getShipSteerStep() {
        return shipSteer.doubleValue();
    }

    public ParamValues mutatedCopy() {

        // would be much better to put all of these in
        // to a data structure (e.g. a Map) then simply make a copy
        // of the map and mutate all it's values!
        EvolvableParams copy = new EvolvableParams();
        copy.bigRad = bigRad.copy().mutate();
        copy.midRad = midRad.copy().mutate();
        copy.smallRad = smallRad.copy().mutate();
        copy.minSpeed = minSpeed.copy().mutate();
        copy.missileRadius = missileRadius.copy().mutate();
        copy.ttl = ttl.copy().mutate();
        copy.shipSize = shipSize.copy().mutate();
        copy.shipLoss = shipLoss.copy().mutate();
        copy.shipSteer = shipSteer.copy().mutate();

        // System.out.println(copy.ttl);
        return copy();
    }

    public ParamValues copy() {

        // would be much better to put all of these in
        // to a data structure (e.g. a Map) then simply make a copy
        // of the map and mutate all it's values!
        EvolvableParams copy = new EvolvableParams();
        copy.bigRad = bigRad.copy();
        copy.midRad = midRad.copy();
        copy.smallRad = smallRad.copy();
        copy.minSpeed = minSpeed.copy();
        copy.missileRadius = missileRadius.copy();
        copy.ttl = ttl.copy();
        copy.shipSize = shipSize.copy();
        copy.shipLoss = shipLoss.copy();
        copy.shipSteer = shipSteer.copy();

        return copy;
    }

    // could replace this with a JSON version (using the gson library)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Radii: [%d, %d, %d]\n", bigRad.intValue(), midRad.intValue(), smallRad.intValue()));
        sb.append("Missile speed:  " + minSpeed + "\n");
        sb.append("Missile radius: " + missileRadius.intValue() + "\n");
        sb.append("Missile TTL:    " + ttl.intValue() + "\n");
        sb.append("Ship size:      " + shipSize.intValue() + "\n");
        sb.append("Ship loss:      " + shipLoss + "\n");
        sb.append("Ship steer:      " + shipSteer + "\n");
        return sb.toString();
    }
}
