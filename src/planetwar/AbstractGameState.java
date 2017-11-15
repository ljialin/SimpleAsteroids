package planetwar;


// for multi-player game

public interface AbstractGameState {

    AbstractGameState copy();

    AbstractGameState next(int[] actions);

    int nActions();

    double getScore();

    boolean isTerminal();

}

