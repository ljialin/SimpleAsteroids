package asteroids;

import evogame.GameParameters;

import java.util.Arrays;

public class ActionAdapter {
    Action[] actions;

    public ActionAdapter() {
        // currently the options to set parameters is not used
        setParams(null);
    }

    public ActionAdapter setParams(GameParameters params) {
        boolean[] fires = {false, true};
        double[] thrusts = {0, 2};
        double[] turns = {-7, 0, 7};
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
