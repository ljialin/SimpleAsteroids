package gvglink.ggi;

import core.game.StateObservation;
import core.game.StateObservationMulti;
import ggi.core.AbstractGameState;
import ontology.Types;

import java.util.ArrayList;

public class GVGSinglePlayerAdapter implements AbstractGameState {


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
        // this bit has to map each int to each actions type
        ArrayList<Types.ACTIONS> gvgActions = gvgState.getAvailableActions();

        // assume a single player game: the array will have just one element
        Types.ACTIONS action = gvgState.getAvailableActions().get(actions[0]);

        gvgState.advance(action);
        return this;
    }

    @Override
    public int nActions() {
        return gvgState.getAvailableActions().size();
    }

    @Override
    public double getScore() {
        return gvgState.getGameScore();
    }

    @Override
    public boolean isTerminal() {
        return gvgState.isGameOver();
    }
}

