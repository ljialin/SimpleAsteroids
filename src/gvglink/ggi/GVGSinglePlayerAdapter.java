package gvglink.ggi;

import core.game.StateObservation;
import core.game.StateObservationMulti;
import ggi.core.AbstractGameState;
import ontology.Types;

import java.util.ArrayList;

public class GVGSinglePlayerAdapter implements AbstractGameState {


    int losePenalty = -10;
    StateObservation gvgState;
    // public Types.ACTIONS[] actions;


    public GVGSinglePlayerAdapter setState(StateObservation gvgState) {
        this.gvgState = gvgState;
        return this;
    }

    @Override
    public AbstractGameState copy() {
        GVGSinglePlayerAdapter copy = new GVGSinglePlayerAdapter();
        copy.gvgState = gvgState.copy();
        return copy;
    }

    @Override
    public AbstractGameState next(int[] actions) {

        // if the game is over then do nothing
        if (gvgState.isGameOver()) return this;

        // single player game so just get the first action
        int a = actions[0];
        ArrayList<Types.ACTIONS> gvgActions = gvgState.getAvailableActions();

        // avoid throwing an index out of bounds exception
        if (a >= gvgActions.size()) {
            System.out.println("Warning: action index out of bounds: " + a + " : " + gvgActions.size());
            return this;
        }

        // okay, we're good to go
        Types.ACTIONS action = gvgState.getAvailableActions().get(a);
        gvgState.advance(action);
        return this;
    }

    public AbstractGameState oldNext(int[] actions) {
        // this bit has to map each int to each actions type
        ArrayList<Types.ACTIONS> gvgActions = gvgState.getAvailableActions();
        System.out.println(gvgActions);
        System.out.println(gvgState.isGameOver());

        System.out.println("Taking action: " + actions[0]);
        // assume a single player game: the array will have just one element
        Types.ACTIONS action = gvgState.getAvailableActions().get(actions[0]);
        System.out.println(action);
        System.out.println();

        gvgState.advance(action);
        return this;
    }

    @Override
    public int nActions() {
        return gvgState.getAvailableActions().size();
    }

    @Override
    public double getScore() {
        if (!gvgState.isAvatarAlive()) return losePenalty + gvgState.getGameScore();
        return gvgState.getGameScore();
    }

    @Override
    public boolean isTerminal() {
        return gvgState.isGameOver();
    }
}

