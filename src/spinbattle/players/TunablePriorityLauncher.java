package spinbattle.players;

import agents.dummy.RandomAgent;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.core.Transporter;
import spinbattle.params.Constants;
import utilities.Picker;

import java.util.Random;

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

    // distance factor controls preference for pairs which are closer
    double distanceFactor = 0.1;

    // attack factor prefers attacking an enemy as opposed to a neutral
    double attackFactor = 1;

    //
    double neutralCost = -0.1;

    // make the random value this ratio of the score
    double randomFactor = 0.1;

    static Random random = new Random();

    //
    double supportSelfCost = 1;

    // how much to value gaining ownership
    double gainFactor = 1;

    private Double sourceTargetScore(Planet source, Planet target, int playerId) {

        // don't own it so cannot do anything
        if (source.ownedBy != playerId) return null;

        // don't send planets to the current planet
        if (source.index == target.index) return  null;

        // more extreme: never support one of our own planets
        if (target.ownedBy == playerId) return null;

        double desirability = 0;

        double distance = source.position.dist(target.position);

        if (target.ownedBy == Constants.neutralPlayer) {
            desirability -= neutralCost * target.shipCount;
        } else {
            if (target.ownedBy != playerId && target.shipCount * inflationFactor < source.shipCount) {
                desirability += target.shipCount;
            }

        }
        // can we gain ownership?


        // prefer planets which are close
        desirability -= distance * distanceFactor;

        desirability *= (1 + randomFactor * random.nextDouble());

        return desirability;
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
        for (Planet source : gameState.planets) {
            if (source.ownedBy == playerId && source.transitReady()) {

                // if above two conditions are met then find the
                // best source target pair
                Picker<AttackPair> picker = new Picker<AttackPair>(Picker.MAX_FIRST);

                // iterate over the possible target planets

                for (Planet target : gameState.planets) {

                    Double pairScore = sourceTargetScore(source, target, playerId);
                    if (pairScore != null) {
                        picker.add(pairScore, new AttackPair(source, target));
                    }
                }

                // now get the pair with the best Heuristic score
                // checkling that there is one available

                AttackPair ap = picker.getBest();

                if (ap != null) {
                    Transporter transit = ap.source.getTransporter();
                    transit.setTarget(ap.target.index);
                    transit.setPayload(ap.source, ap.target.shipCount * inflationFactor);
                    transit.launch(ap.source.position, ap.target.position, playerId, gameState);
                    gameState.notifyLaunch(transit);
                    if (verbose) {
                        System.out.println(transit);
                        System.out.println("Target ship count : paylod: " + ap.target.shipCount + " : " + transit.payload);
                        System.out.println("Desirability: " + picker.getBestScore());
                        System.out.println();
                    }
                }
            }
        }
        return this;
    }

    boolean verbose = false;

    double inflationFactor = 2.0;

}
