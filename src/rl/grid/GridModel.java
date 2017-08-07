package rl.grid;

import altgame.SimpleStateObservationAdapter;
import core.game.StateObservation;
import ontology.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GridModel extends SimpleStateObservationAdapter {

    public static void main(String[] args) {
        GridModel model = new GridModel();
    }

    public int nSteps = 0;

    public int size = 15;

    public static double atGoal = 1;
    public static double notAtGoal = 0;

    static Random r = new Random();

    static int defaultSize = 15;

    public int[] state;
    public int[] goal;

    //    int[] state;
    int[][] actions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};


    public GridModel() {
        this(defaultSize);
    }

    public String toString() {
        return Arrays.toString(state) + " : " + nSteps + " : " + getGameScore();
    }

    public double maxScore() {
        return size * size + 2 * size;
    }

    public double getGameScore() {
        if (atGoal(state)) {
            return maxScore() - nSteps;
        } else {
            return 0;
        }
    }

    public int maxSteps() {
        return size * size;
    }

    public GridModel(int size) {
        this.size = size;
        state = new int[2];
        goal = new int[]{size / 2, size / 2};
    }

    public GridModel copy() {
        GridModel model = new GridModel(size);
        model.state[0] = this.state[0];
        model.state[1] = this.state[1];
        model.nSteps = this.nSteps;
        return model;
    }

    public boolean atGoal(int[] s) {
        return Arrays.equals(s, goal);
    }

    public boolean isGameOver() {
        return atGoal(state) || nSteps >= maxSteps();
    }

    public double value(int[] s) {
        if (atGoal(s)) {
            return atGoal;
        } else {
            return notAtGoal;
        }
    }

    public int getGameTick() {
        return nSteps;
    }

    public Types.WINNER getGameWinner() {
        return Types.WINNER.NO_WINNER;
    }



    public GridModel act(int action) {
        return act(actions[action]);
    }

    public void advance(Types.ACTIONS action) {
        act(action.ordinal());
    }

    static ArrayList<Types.ACTIONS> actionList;

    static {
        int limit = 4;
        actionList = new ArrayList<>();
        for (Types.ACTIONS a : Types.ACTIONS.values()) {
            if (limit-- > 0) {
                actionList.add(a);
                System.out.println(a.ordinal());
            }
        }
    }

    public ArrayList<Types.ACTIONS> getAvailableActions() {
        return actionList;
    }

    // in this method the action is specified as a discrete 2d movement vector coded as an array of int
    public GridModel act(int[] a) {

        for (int i = 0; i < state.length; i++) {
            state[i] = ((state[i] + a[i]) + size) % size;
        }
        nSteps++;

        return this;
    }

}
