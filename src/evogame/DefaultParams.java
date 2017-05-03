package evogame;

import evogame.ParamValues;

/**
 * Created by simonmarklucas on 22/05/2016.
 */
public class DefaultParams implements ParamValues {



    public int bigRad = 30;
    public int midRad = 20;
    public int smallRad = 10;
    public double missileSpeed = 4;
    public int missileRad = 7;
    public int missileTTL = 50;
    public int shipSize = 5;
    public double shipLoss = 0.99;
    public double shipSteer = 3;



    @Override
    public int getBigRadius() {
        return bigRad;
    }

    @Override
    public int getMidRadius() {
        return midRad;
    }

    @Override
    public int getSmallRadius() {
        return smallRad;
    }

    @Override
    public double getMissileMinVelocity() {
        return missileSpeed;
    }

    @Override
    public int getMissileRadius() {
        return missileRad;
    }

    @Override
    public int getMissileTTL() {
        return missileTTL;
    }

    @Override
    public int getShipSize() {
        return shipSize;
    }

    @Override
    public double getShipLoss() {
        return shipLoss;
    }

    @Override
    public double getShipSteerStep() {
        return shipSteer;
    }

    public ParamValues copy() {
        // for now just return this, but should really make a proper copy
        return this;
    }

    @Override
    public ParamValues mutatedCopy() {
        return null;
    }


}
