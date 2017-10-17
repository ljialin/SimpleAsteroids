package planetwar;

public class DoNothingAgent implements SimplePlayerInterface {

    @Override
    public int getAction(GameState gameState, int playerId) {
        return GameState.doNothing;
    }

}
