package battle;

import evodef.EvoDoubleSet;
import evodef.EvoVectorSet;

import java.util.HashMap;

import static battle.BattleParamNames.*;

/**
 * Created by simonmarklucas on 24/10/2016.
 */

public class BattleGameParameters {

    // these just set up some manual defaults
    // although the values are stored in
    // a hashmap for convenient getting and setting (allowing iteration over the values)
    // but having them static enables faster access when used

    public static double damageRadius = 50;
    public static double damageCost = 50;
    public static double loss = 0.95;
    public static double shipSize = 40;
    public static double rotation = 1;
    public static double thrust = 1;

    public static HashMap<BattleParamNames, Double> params = new HashMap<>();

    // set up some useful defaults, but access will normally be via the inject method
    static {
        params.put(DAMAGE_COST, damageCost);
        params.put(DAMAGE_RADIUS, damageRadius);
        params.put(LOSS, loss);
        params.put(SHIP_SIZE, shipSize);
        params.put(ROTATION, rotation);
        params.put(THRUST, thrust);
    }

    /** this is a convenient way to set all the game parameters
     *
     *  Currently has an annoying bug!!!
     *
     *  we're using the ordinal value of a paremeter, but this is poor - it's not working
     *
     * */
    public static void inject(EvoVectorSet space, int[] p) {
        for (BattleParamNames key : BattleParamNames.values()) {
            // each key in the enum has an ordinal value that we can use
            // to index in to the array of int
            int ix = key.ordinal();
            EvoDoubleSet param = space.params.get(ix);
            double value = param.values[p[ix]];
            System.out.format("Setting key %s with name %s to %.2f at index %d\n", key, param.name, value, ix);
            params.put(key, value);
        }

        // unfortunately need to set all of the static variables one by one :(

        damageCost = params.get(DAMAGE_COST);
        damageRadius = params.get(DAMAGE_RADIUS);
        loss = params.get(LOSS);
        shipSize = params.get(SHIP_SIZE);
        rotation = params.get(ROTATION);
        thrust = params.get(THRUST);

//        System.out.println("Actual ship size = " + shipSize);
//        System.out.println("Actual damage radius = " + damageRadius);
    }
}
