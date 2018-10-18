package ggi.core;


// for multi-player game

public interface AbstractGameState {

    AbstractGameState copy();

    // the ith entry of the actions array is the action for the ith player
    // next is used to advance the state of the game given the current
    // set of actions
    // this can either be for the 'real' game
    // or for a copy of the game to use in statistical forward planning, for example
    AbstractGameState next(int[] actions);

    // the number of actions available to a player in the current state
    int nActions();

    double getScore();

    boolean isTerminal();

    // double[] getFeatureFector();

    // int[][] getScreen();

    // List<Object> getGameState();

}

