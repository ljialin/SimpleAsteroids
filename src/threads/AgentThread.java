package threads;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

public class AgentThread extends Thread implements SimplePlayerInterface {

    public AgentThread() {
        start();
    }

    public void run() {
        // keep waiting for a new gameState to be passed in
        // this loop runs in this object's thread
        while (true) {
            if (thinking) {
                // call the getAction method
                action = player.getAction(gameState, playerId);
            }
        }
    }

    SimplePlayerInterface player;
    AbstractGameState gameState;
    Integer action;
    boolean thinking = false;
    int playerId;

    public AgentThread setPlayer(SimplePlayerInterface player) {
        this.player = player;
        return this;
    }

    public AgentThread requestAction(AbstractGameState gameState, int playerId,  int delay) {
        this.gameState = gameState;
        thinking = true;
        this.playerId = playerId;
        // action = null;
        return this;
    }

    public Integer getLatestAction() {
        // obviously this always return immediately
        return action;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        // this loop runs in the caller's thread
        this.gameState = gameState;
        action = null;

        while (action == null)  {

        }
        return 0;
    }

    @Override
    public SimplePlayerInterface reset() {
        player.reset();
        return this;
    }

}
