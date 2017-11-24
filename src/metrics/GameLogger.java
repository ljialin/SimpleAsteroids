package metrics;

import planetwar.AbstractGameState;


/**
 *  Simple interface for recording gameplay logs.
 *  Different methods should probably be in different
 *  */
public interface GameLogger {




    public GameLogger logAction(AbstractGameState state,
                          int[] actions,
                          GameEvent[] events);


//    public void logAction(AbstractGameState state,
//                          InputEvent[] actions,
//                          GameEvent[] events);
}
