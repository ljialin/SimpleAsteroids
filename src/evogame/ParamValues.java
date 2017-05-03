package evogame;

/**
 * Created by simonmarklucas on 22/05/2016.
 */
public interface ParamValues {

    int getBigRadius();
    int getMidRadius();
    int getSmallRadius();

    double getMissileMinVelocity();
    int getMissileRadius();
    int getMissileTTL();

    int getShipSize();

    double getShipLoss();
    double getShipSteerStep();

    ParamValues copy();
    ParamValues mutatedCopy();

}
