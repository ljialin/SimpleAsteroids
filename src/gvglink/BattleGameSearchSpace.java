package gvglink;

import battle.BattleGameParameters;
import evodef.EvoDoubleSet;
import evodef.EvoVectorSet;
import evodef.SearchSpaceUtil;

import static battle.BattleParamNames.*;

/**
 * Created by simonmarklucas on 24/10/2016.
 */
public class BattleGameSearchSpace {

    public static void main(String[] args) {
        EvoVectorSet space = getSearchSpace();
        System.out.println("Size of search space: " + SearchSpaceUtil.size(space));

        System.out.println("Picking a random point in the space...");

        int[] p = SearchSpaceUtil.randomPoint(space);
        space.printValues(p);

        BattleGameParameters.inject(space, p);

        System.out.println("Checking ...");

        System.out.println(BattleGameParameters.params);

    }

    public static int[] getRandomPoint() {
        EvoVectorSet space = getSearchSpace();
        System.out.println("Size of search space: " + SearchSpaceUtil.size(space));
        System.out.println("Picking a random point in the space...");
        return SearchSpaceUtil.randomPoint(space);
    }

    public static void inject(int[] p) {
        BattleGameParameters.inject(getSearchSpace(), p);
    }

    public static EvoVectorSet getSearchSpace() {
        EvoVectorSet params = new EvoVectorSet();

        params.params.add(new EvoDoubleSet(DAMAGE_RADIUS.toString(), new double[]{5, 20, 50, 100, 200}));
        params.params.add(new EvoDoubleSet(DAMAGE_COST.toString(), new double[]{1, 5, 20, 50}));
        params.params.add(new EvoDoubleSet(LOSS.toString(), new double[]{0.8, 0.9, 0.95, 0.98, 0.99, 1.0}));
        params.params.add(new EvoDoubleSet(SHIP_SIZE.toString(), new double[]{20, 30, 40}));
        params.params.add(new EvoDoubleSet(ROTATION.toString(), new double[]{1, 2, 3, 4, 5, 10}));
        params.params.add(new EvoDoubleSet(THRUST.toString(), new double[]{1, 2, 3, 4, 5, 10}));

        return params;
    }

}
