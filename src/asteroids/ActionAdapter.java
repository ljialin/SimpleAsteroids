package asteroids;

import evogame.GameParameters;

import java.util.Arrays;

public class ActionAdapter {
    Action[] actions;

    public static double thrust = 2;
    public static double turn = 5;

    public ActionAdapter() {
        // currently the options to set parameters is not used
        setParams(null);
    }

    public ActionAdapter setParams(GameParameters params) {
        boolean[] fires = {false, true};
        double[] thrusts = {0, thrust};
        double[] turns = {-turn, 0, turn};
        int n = fires.length * thrusts.length * turns.length;
        int ix = 0;
        actions = new Action[n];
        System.out.println("Action adapter: " + n);
        for (boolean fire : fires) {
            for (double thrust : thrusts) {
                for (double turn : turns) {
                    actions[ix++] = new Action(thrust, turn, fire);
                }
            }
        }
        System.out.println(Arrays.toString(actions));
        return this;
    }

    public Action getAction(int i) {
        return actions[i];
    }
}
