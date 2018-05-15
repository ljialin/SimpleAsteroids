package gvglink;

import core.player.AbstractMultiPlayer;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;


/**
 * Obviously unfinished...
 */
public class GVGTwoPlayerAdapter implements SimplePlayerInterface {

    AbstractMultiPlayer abstractMultiPlayer;

    public GVGTwoPlayerAdapter setPlayer(AbstractMultiPlayer abstractMultiPlayer) {

        return null;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        // PlanetWarsLinkState linkState = new PlanetWarsLinkState();
        // GeneralLinkState linkState = new GeneralLinkState().setState(gameState);

        // linkState.state = gameState;
        return 0;
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }


}
