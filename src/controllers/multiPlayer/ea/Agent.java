package controllers.multiPlayer.ea;

import core.game.StateObservation;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import evodef.GameActionSpaceAdapterMulti;
import evodef.SearchSpaceUtil;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;

/**
 * Created by sml on 23/01/2017.
 */

public class Agent extends AbstractMultiPlayer {

    public int num_actions;

    // these used to be static, but have made them instance variables
    // to allow experiments between multiple instances of the agent
    // with different values
    public int sequenceLength = 20;
    public boolean useShiftBuffer = true;

    GameActionSpaceAdapterMulti gameAdapter;

    int nEvals;

    public EvoAlg evoAlg;

    public static void main(String[] args) {
        System.out.println();
    }

    public int[] NUM_ACTIONS;
    public Types.ACTIONS[][] actions;
    public int id, oppID, no_players;

    StateObservationMulti stateObservationMulti;


    /**
     * Public constructor with state observation and time due.
     * @param stateObservationMulti state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public Agent(StateObservationMulti stateObservationMulti, ElapsedCpuTimer elapsedTimer, EvoAlg evoAlg, int playerID, int nEvals)
    {
        this.stateObservationMulti = stateObservationMulti;
        //get game information
        System.out.println("Making an Agent");

        this.evoAlg = evoAlg;

        no_players = stateObservationMulti.getNoPlayers();
        id = playerID;

        oppID = (id + 1) % stateObservationMulti.getNoPlayers();
        this.nEvals = nEvals;

        //Get the actions for all players in a static array.

        NUM_ACTIONS = new int[no_players];
        actions = new Types.ACTIONS[no_players][];
        for (int i = 0; i < no_players; i++) {

            ArrayList<Types.ACTIONS> act = stateObservationMulti.getAvailableActions(i);

            actions[i] = new Types.ACTIONS[act.size()];
            for (int j = 0; j < act.size(); ++j) {
                actions[i][j] = act.get(j);
            }
            NUM_ACTIONS[i] = actions[i].length;
        }
        gameAdapter = new GameActionSpaceAdapterMulti(stateObservationMulti, sequenceLength, id, oppID);

    }



    public Agent setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        gameAdapter = new GameActionSpaceAdapterMulti(stateObservationMulti, sequenceLength, id, oppID);
        return this;
    }

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */

    // int index;
    int[] solution;

    // will only recalculate after this number of steps
    // static int playoutLength = 1;


    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        if (true) throw new RuntimeException("Not yet implemented");

        //Set the state observation object as the new root of the tree.

        // we'll set up a game adapter and run the algorithm independently each
        // time at least to being with

        int action;
        GameActionSpaceAdapter game = new GameActionSpaceAdapter(stateObs, sequenceLength);

        if (solution != null) {
            solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, game);
            evoAlg.setInitialSeed(solution);
        }

        solution = evoAlg.runTrial(game, nEvals);

        // System.out.println(Arrays.toString(solution) + "\t " + game.evaluate(solution));

        action = solution[0];
        // already return the first element, so now set it to 1 ...

        if (!useShiftBuffer) solution = null;

        // index = 1;

        //... and return it.
        // return actions[action];

        return null; //
    }

    double discountFactor = 0.95;

    public Agent setDiscountFactor(double discountFactor) {
        this.discountFactor = discountFactor;
        return this;
    }

    public Agent setUseShiftBuffer(boolean useShiftBuffer) {
        this.useShiftBuffer = useShiftBuffer;
        return this;
    }

    @Override
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedCpuTimer) {
        //Set the state observation object as the new root of the tree.

        // we'll set up a game adapter and run the algorithm independently each
        // time at least to being with

        int action1;
        // GameActionSpaceAdapterMulti game = new GameActionSpaceAdapterMulti(stateObs, sequenceLength, id, oppID);
        gameAdapter.setState(stateObs);
        gameAdapter.discountFactor = this.discountFactor;
        gameAdapter.reset();



        if (solution != null) {
            solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, gameAdapter);
            evoAlg.setInitialSeed(solution);
        }

        solution = evoAlg.runTrial(gameAdapter, nEvals);


        // now if we're running verbose, then plot the solutions found by the algorithm
        // System.out.println("Calling gamePlot");
        gameAdapter.plot();


        // System.out.println(Arrays.toString(solution) + "\t " + game.evaluate(solution));

        action1 = solution[0];
        // already return the first element, so now set it to 1 ...

        if (!useShiftBuffer) solution = null;

        // index = 1;

        //... and return it.
        // return actions[action];



        return actions[id][action1];
    }


}
