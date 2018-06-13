package gvglink.ggi;

import core.game.StateObservationMulti;
import ggi.core.AbstractGameState;
import ontology.Types;

import java.util.ArrayList;

public class GVGTwoPlayerAdapter implements AbstractGameState {


    StateObservationMulti gvgState;
    // public Types.ACTIONS[] actions;


    public GVGTwoPlayerAdapter setState(StateObservationMulti gvgState) {
        this.gvgState = gvgState;
        setActionMapping();
        return this;
    }

    public GVGTwoPlayerAdapter setActionMapping() {
        // actions = gvgState.getAvailableActions()
        return this;
    }

    @Override
    public AbstractGameState copy() {
        GVGTwoPlayerAdapter copy = new GVGTwoPlayerAdapter();
        copy.gvgState = gvgState.copy();
        copy.setActionMapping();
        return copy;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        // this bit has to map each int to each actions type
        ArrayList<Types.ACTIONS> gvgActions = gvgState.getAvailableActions();

        // actions.length should always be of length 2 for a 2-player game
        Types.ACTIONS[] acts = new Types.ACTIONS[actions.length];
        for (int i=0; i<actions.length; i++) {
            acts[i] = gvgActions.get(actions[i]);
        }
        gvgState.advance(acts);
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

