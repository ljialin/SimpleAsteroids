package gvglink;

import altgame.SimpleStateObservationAdapter;
import battle.SimpleBattleState;
import core.game.StateObservation;
import ontology.Types;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sml on 24/10/2016.
 */
public class SpaceBattleLinkState
        extends SimpleStateObservationAdapter {

    static int maxTick = 500;

    // this class is already 2-player ready
    SimpleBattleState state;

    static ArrayList<Types.ACTIONS> actions = new ArrayList<>();

    static int actionLimit = SimpleBattleState.actions.length;

    static {
        for (Types.ACTIONS a : Types.ACTIONS.values()) {
            if (actions.size() < actionLimit) {
                actions.add(a);
            }
        }
        System.out.println("nActions = " + actions.size());
    }

    public static void main(String[] args) {
        System.out.println(Types.ACTIONS.values().length);
        for (Types.ACTIONS a : Types.ACTIONS.values()) {
            System.out.println(a + "\t " + a.ordinal());
        }
    }

    public SpaceBattleLinkState() {
        this(new SimpleBattleState(), 0, 0);
    }

    int score;
    int tick;

    public SpaceBattleLinkState(SimpleBattleState state, int score, int tick) {
        this.state = state;
        this.score = score;
        this.tick = tick;
    }

    public StateObservation copy() {
        return new SpaceBattleLinkState(state.copyState(), score, tick);
    }

    // static int[] scores = new int[Types.ACTIONS]

    public void advance(Types.ACTIONS action) {
        int[] a = new int[]{action.ordinal(), random.nextInt(actions.size())};
        state.next(a);
        score = state.score[0] - state.score[1];
        tick++;
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
        return state.simpleHeuristic();
        // return state.diskDifferenceHeuristic();
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

