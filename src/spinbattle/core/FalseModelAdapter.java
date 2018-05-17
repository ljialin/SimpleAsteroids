package spinbattle.core;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import spinbattle.params.SpinBattleParams;

public class FalseModelAdapter implements SimplePlayerInterface {


    SpinBattleParams params;
    SimplePlayerInterface player;

    public FalseModelAdapter setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    public FalseModelAdapter setPlayer(SimplePlayerInterface player) {
        this.player = player;
        return this;
    }

    @Override
    public int getAction(AbstractGameState generalGameState, int playerId) {
        SpinGameState gameState = (SpinGameState) generalGameState;
        gameState.setParams(params);
        // System.out.println("Transit Speed: " + params.transitSpeed);
        return player.getAction(gameState, playerId);
    }

    @Override
    public SimplePlayerInterface reset() {
        System.out.println("Doing nothing!");
        return null;
    }

    public String toString() {
        return "False Model: " + String.format("%.3f, %.3f", params.gravitationalFieldConstant, params.transitSpeed);
    }
}
