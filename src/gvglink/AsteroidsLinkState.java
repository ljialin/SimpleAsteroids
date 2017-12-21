package gvglink;

import altgame.SimpleStateObservationAdapter;
import asteroids.AsteroidsGameState;
import battle.SimpleBattleState;
import core.game.StateObservation;
import evogame.DefaultParams;
import evogame.GameParameters;
import ontology.Types;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */

public class AsteroidsLinkState
        extends SimpleStateObservationAdapter {

    static GameParameters params = new GameParameters().injectValues(new DefaultParams());

    static int maxTick = 1000;

    public static int totalGameTicks = 0;
    public static int totalGameCopies = 0;

    // this class is already 2-player ready
    AsteroidsGameState state;

    static ArrayList<Types.ACTIONS> actions = new ArrayList<>();

    static int actionLimit = new AsteroidsGameState().nActions();

    static {
//        for (Types.ACTIONS a : Types.ACTIONS.values()) {
//            if (actions.size() < actionLimit) {
//                actions.add(a);
//            }
//        }
        int nAvailable = Types.ACTIONS.values().length;
        for (int i=0; i<actionLimit; i++) {
            actions.add(Types.ACTIONS.values()[i % nAvailable]);
        }
        System.out.println("nActions = " + actions.size());
        System.out.println("Action limit: " + actionLimit);
    }

    public static void main(String[] args) {
        System.out.println(actions);
    }

    public AsteroidsLinkState() {
        this(new AsteroidsGameState().setParams(params).initForwardModel(), 0, 0);
    }

    int score;
    int tick;

    public AsteroidsLinkState(AsteroidsGameState state, int score, int tick) {
        this.state = state;
        this.score = score;
        this.tick = tick;
    }

    public StateObservation copy() {
        totalGameCopies++;
        return new AsteroidsLinkState(state.copy(), score, tick);
    }


    public void advance(Types.ACTIONS action) {
        int[] a = new int[]{action.ordinal(), random.nextInt(actions.size())};
        state.next(a);
        score = (int) state.getScore();
        tick++;
        totalGameTicks++;
    }

    static Random random = new Random();
    /**
     * Sets a new seed for the forward model's random generator (creates a new object)
     *
     * @param seed the new seed.
     */
    public void setNewSeed(int seed) {
        System.out.println("Not setting new seed...");
    }

    /**
     * Returns the actions that are available in this game for
     * the avatar.
     *
     * @return the available actions.
     */

    public ArrayList<Types.ACTIONS> getAvailableActions() {
        return actions;
    }

    /**
     * Returns the actions that are available in this game for
     * the avatar. If the parameter 'includeNIL' is true, the array contains the (always available)
     * NIL action. If it is false, this is equivalent to calling getAvailableActions().
     *
     * @param includeNIL true to include Types.ACTIONS.ACTION_NIL in the array of actions.
     * @return the available actions.
     */
    public ArrayList<Types.ACTIONS> getAvailableActions(boolean includeNIL) {
        return actions;
    }

    public int getNoPlayers() {
        return 1;
    }

    public double getGameScore() {
        return score;
    }

    public double getHeuristicScore() {
        // return state.simpleHeuristic();
        // return state.diskDifferenceHeur
        // istic();
        return state.getScore();
    }

    public int getGameTick() {
        return tick;
    }

    /**
     * Indicates if there is a game winner in the current observation.
     * Possible values are Types.WINNER.PLAYER_WINS, Types.WINNER.PLAYER_LOSES and
     * Types.WINNER.NO_WINNER.
     *
     * @return the winner of the game.
     */
    public Types.WINNER getGameWinner() {
        return Types.WINNER.NO_WINNER;
    }

    /**
     * Indicates if the game is over or if it hasn't finished yet.
     *
     * @return true if the game is over.
     */
    public boolean isGameOver() {
        return tick >= maxTick;
    }

}

