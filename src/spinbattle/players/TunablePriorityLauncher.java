package spinbattle.players;

import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;
import spinbattle.params.Constants;
import utilities.Picker;

public class TunablePriorityLauncher implements SimplePlayerInterface {
    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        if (true) throw new RuntimeException("Not implemented: use makeTransits()");
        return 0;
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }


    // int shipThreshold =

    // try to find the best source / target pair to launch an attack

    // distance factor controls preference for pairs which
    double distanceFactor = 1;

    // attack factor prefers attacking an enemy as opposed to a neutral
    double attackFactor = 1;

    //
    double neutralCost = 1;

    //
    double supportSelfCost = 1;

    // how much to value gaining ownership
    double gainFactor = 1;

    private Double sourceTargetScore(Planet source, Planet target, int playerId) {
        if (source.ownedBy != playerId) return null;

        double distance = source.position.dist(target.position);
        if (target.ownedBy == Constants.neutralPlayer) {

        }

        return distance * distanceFactor;
    }

    static class AttackPair {
        Planet source, target;

        public AttackPair(Planet source, Planet target) {
            this.source = source;
            this.target = target;
        }
    }

    public TunablePriorityLauncher makeTransits(SpinGameState gameState, int playerId) {

        // for each planet, check whether the transit is available


        for (Planet p : gameState.planets) {
            if (p.ownedBy == playerId && p.transitReady()) {

                // if above two conditions are met then find the
                // best source target pair
                Picker<AttackPair> picker = new Picker<AttackPair>();

                Transporter transit = p.getTransporter();
                Planet destination = getTarget(gameState, p, playerId);
                if (destination != null) {
                    transit.setPayload(p, destination.shipCount * inflationFactor);
                    transit.launch(p.position, destination.position, playerId, gameState);
                    transit.setTarget(destination.index);
                    gameState.notifyLaunch(transit);
                }
            }
        }
        return this;
    }

    int nAttempts = 100;
    double inflationFactor = 1.2;
    Planet getTarget(SpinGameState gameState, Planet source, int playerId) {
        for (int i=0; i<nAttempts; i++) {
            // pick a planet at Random
            int ix = gameState.params.getRandom().nextInt(gameState.planets.size());
            Planet p = gameState.planets.get(ix);
            if (p.ownedBy != playerId) {
                if (p.shipCount * inflationFactor < source.shipCount) {
                    return p;
                }
            }
        }
        return null;
    }
}
