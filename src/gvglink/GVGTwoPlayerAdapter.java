package gvglink;

import core.player.AbstractMultiPlayer;
import planetwar.AbstractGameState;
import planetwar.SimplePlayerInterface;


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




}
