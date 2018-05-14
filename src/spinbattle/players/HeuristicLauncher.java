package spinbattle.players;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;

public class HeuristicLauncher implements SimplePlayerInterface {
    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        return 0;
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }


    // int shipThreshold =

    public HeuristicLauncher makeTransits(SpinGameState gameState, int playerId) {

        // for each planet, check whether the transit is available

        for (Planet p : gameState.planets) {
            if (p.ownedBy == playerId && p.transitReady()) {
                Transporter transit = p.getTransporter();
                Planet destination = getTarget(gameState, p, playerId);
                if (destination != null) {
                    transit.setPayload(p, destination.shipCount);
                    transit.launch(p.position, destination.position, playerId, gameState);
                    transit.setTarget(destination.index);
                    gameState.notifyLaunch(transit);
                }
            }
        }
        return this;
    }

    int nAttempts = 5;
    Planet getTarget(SpinGameState gameState, Planet source, int playerId) {
        for (int i=0; i<nAttempts; i++) {
            // pick a planet at Random
            int ix = gameState.params.getRandom().nextInt(gameState.planets.size());
            Planet p = gameState.planets.get(ix);
            if (p.ownedBy != playerId) {
                if (p.shipCount < source.shipCount) {
                    return p;
                }
            }
        }
        return null;
    }
}
