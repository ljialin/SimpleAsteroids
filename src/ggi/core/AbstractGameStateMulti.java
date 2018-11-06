package ggi.core;


// for multi-player game

public interface AbstractGameStateMulti {

    AbstractGameStateMulti copy();

    // the ith entry of the actions array is the action for the ith player
    // next is used to advance the state of the game given the current
    // set of actions
    // this can either be for the 'real' game
    // or for a copy of the game to use in statistical forward planning, for example
    AbstractGameStateMulti next(int[][] actions);


    // the number of actions available to a player in the current state
    // for each unit or aspect of a unit under its control
    int[] nActions(int playerId);


    // different players may have entirely different non zero-sum objectives
    // this gets a single-objective score from the point of view of each player
    // using player id to index the array
    Double[] getScore();

    boolean isTerminal();

    // double[] getFeatureFector();

    // int[][] getScreen();

    // List<Object> getGameState();

}

