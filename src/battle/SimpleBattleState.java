package battle;

import asteroids.Action;
import math.Vector2d;

import static battle.BattleParamNames.*;

//import static asteroids.Constants.height;
//import static asteroids.Constants.width;

import static battle.BattleGameParameters.*;

/**
 * Created by simonmarklucas on 04/10/2016.
 */

public class SimpleBattleState {

    public static void main(String[] args) {
        System.out.println(new SimpleBattleState().nActions());
    }

    // project the damage spot this many pixels away from the ship

    static double projection = 100;
//    static double damageRadius = BattleGameParameters.damageRadius;
//    static double damageCost = BattleGameParameters.damageCost;

    public static int width = 640;
    public static int height = 480;

    public int[] score;

    static int nPlayers = 2;
    public SimpleShip[] ships;
    public int nTicks;

    static double thrust = BattleGameParameters.thrust;
    static double rotate = 3;  // rotate is controlled in the ship ...
    public static Action[] actions = {
            new Action(0, 0, false),  // do nothing
            new Action(0, rotate, false),
            new Action(0, -rotate, false),
            new Action(thrust, rotate, false),
            new Action(thrust, -rotate, false),
            new Action(-thrust, 0, false),
    };

    // for now make all the action costs zero
    // static int[] actionCosts = {0, 1, 1, 2, 2};
    static int[] actionCosts = {0, 0, 0, 0, 0, 0};

    public SimpleBattleState() {
        ships = new SimpleShip[nPlayers];
        ships[0] = new SimpleShip(new Vector2d(160, 240), new Vector2d(0, 0), new Vector2d(0, 1));
        ships[1] = new SimpleShip(new Vector2d(480, 240), new Vector2d(0, 0), new Vector2d(0, -1));
        score = new int[nPlayers];
        nTicks = 0;
    }

    // the array of actions is over the set of players
    // so for a 2-player game should always be of length 2
    public SimpleBattleState next(int[] a) {
        // update the ships
        for (int i = 0; i < a.length; i++) {
            ships[i].update(actions[a[i]]);
            wrap(ships[i]);
        }
        // now update the scores
        // for now take a really dirty approach by simply assuming we
        // have a two player game

        score[0] += damageFunction(ships[0], ships[1]);
        score[1] += damageFunction(ships[1], ships[0]);

        // also factor in the action cost - by giving the points to the opponent
        // player 0 is the positive player, and therefore the action costs
        // of player 1 are added to the score while the action costs of
        // player 0 are subtracted from the score

        score[0] -= actionCosts[a[0]];
        score[1] -= actionCosts[a[1]];

        nTicks++;
        return this;
    }

    public SimpleBattleState copyState() {
        // could be made more efficient by defining a new constructor
        // that took an array of SimpleShip
        SimpleBattleState sbs = new SimpleBattleState();
        for (int i=0; i<ships.length; i++) {
            sbs.ships[i] = ships[i].copy();
        }
        sbs.score = new int[nPlayers];
        for (int i=0; i<nPlayers; i++) sbs.score[i] = score[i];
        return sbs;
    }

    private void wrap(SimpleShip ship) {
        ship.s.x = (ship.s.x + width) % width;
        ship.s.y = (ship.s.y + height) % height;

    }

    public int nActions() {
        return actions.length;
    }


    // use a static temporary vector to save creating garbage
    static Vector2d tmp = new Vector2d();

    public double damageFunction(SimpleShip attacker, SimpleShip defender) {

        tmp.set(attacker.s);
        tmp.add(attacker.d, projection);

        // now return a damage

        if (defender.s.dist(tmp) < damageRadius) {
            return damageCost;
        } else {
            return 0;
        }
    }

    // should mostly be less than an actual score, so that
    // the heuristic could be added in to the score as a slight bias
    public static double heuristicScaling = 0.2;

    public double diskDifferenceHeuristic() {
        return diskDifferenceHeuristic(ships[0], ships[1]);
    }

    public double simpleHeuristic() {
        // this one simple encourages some interaction!
        return -ships[0].s.dist(ships[1].s);
    }

    public double diskDifferenceHeuristic(SimpleShip attacker, SimpleShip defender) {
        double d1 = attackDiskDifference(attacker, defender);
        double d2 = attackDiskDifference(defender, attacker);
        // we want d1 to be small (i.e. defender in range of attack disk
        // while d2 is large
        return (d2 - 2 * d1) * heuristicScaling;
    }

    public double attackDiskDifference(SimpleShip attacker, SimpleShip defender) {
        tmp.set(attacker.s);
        tmp.add(attacker.d, projection);
        return defender.s.dist(tmp);
    }

}
