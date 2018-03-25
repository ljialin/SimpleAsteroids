package planetwar;

public class DoNothingAgent implements SimplePlayerInterface {

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        return GameState.doNothing;
    }

    public String toString() {
        return "Do Nothing Agent";
    }


}
