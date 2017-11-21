package planetwar;


// for multi-player game

import java.util.List;

public interface AbstractGameState {

    AbstractGameState copy();

    AbstractGameState next(int[] actions);

    int nActions();

    double getScore();

    boolean isTerminal();

    // List<Object> getGameState();

}

