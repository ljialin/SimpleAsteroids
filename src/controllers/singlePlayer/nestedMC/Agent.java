package controllers.singlePlayer.nestedMC;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import evodef.SearchSpaceUtil;
import gvglink.SpaceBattleLinkState;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 *
 *
 */
public class Agent extends AbstractPlayer {

    public static int MCTS_ITERATIONS = 100;
    public static double REWARD_DISCOUNT = 1.00;
    public int num_actions;
    public Types.ACTIONS[] actions;
    public static int SEQUENCE_LENGTH = 25;

    int nEvals;

    public EvoAlg evoAlg;

    public static void main(String[] args) {
        System.out.println();
    }

    /**
     * Public constructor with state observation and time due.
     *
     * @param so           state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        //Get the actions in a static array.
        ArrayList<Types.ACTIONS> act = so.getAvailableActions();
        actions = new Types.ACTIONS[act.size()];
        for (int i = 0; i < actions.length; ++i) {
            actions[i] = act.get(i);
        }
        num_actions = actions.length;

        System.out.println(Arrays.toString(actions));

        //Create the player.

        this.evoAlg = evoAlg;
        this.nEvals = nEvals;
        index = 0;
    }


    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     *
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */

    int index;
    int[] solution;

    // will only recalculate after this number of steps
    static int playoutLength = 1;

    public static boolean useShiftBuffer = true;


    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        //Set the state observation object as the new root of the tree.

        // we'll set up a game adapter and run the algorithm independently each
        // time at least to being with

        int action;
        GameActionSpaceAdapter game = new GameActionSpaceAdapter(stateObs, SEQUENCE_LENGTH);


        StateObservation obs = stateObs.copy();

        Types.ACTIONS[] moveSeq = new Types.ACTIONS[maxRolloutLength];

        bestRollout = new Types.ACTIONS[maxNestingDepth][maxRolloutLength];
        lengthBestRollout = new int[maxNestingDepth];
        scoreBestRollout = new double[maxNestingDepth];

        //nested(obs, nestDepth, moveSeq, 0);
        double bestScore = Double.MIN_VALUE;
        Types.ACTIONS bestAction = actions [0];
        for (int i = 0; i < maxLegalMoves; i++) {
            StateObservation state = obs.copy();
            Types.ACTIONS[] moveSeqCopy = new Types.ACTIONS[maxRolloutLength];
            int nActionsPlayed = 0;
            state.advance(actions[i]);
            moveSeqCopy[nActionsPlayed] = actions[i];
            nActionsPlayed++;
            nested(state, nestDepth, moveSeqCopy, nActionsPlayed);
            double score = state.getGameScore();
            if (score > bestScore) {
                bestScore = score;
                bestAction = actions[i];
            }
        }
        return bestAction;
    }


    public static int nestDepth = 4;
    public static int maxNestingDepth = 10;
    public static int maxRolloutLength = 10;

    int[] lengthBestRollout;
    double[] scoreBestRollout;
    Types.ACTIONS[][] bestRollout;
    static int maxLegalMoves = Types.ACTIONS.values().length;

    double bestScoreNested = Double.MIN_VALUE;
    // Board bestBoard;


    static Random random = new Random();
    void playout(StateObservation stateObservation, Types.ACTIONS[] moveSeq, int nActionsPlayed) {
        // Types.ACTIONS[] moveSeq = new Types.ACTIONS[maxRolloutLength];
        while (!stateObservation.isGameOver() && nActionsPlayed < maxRolloutLength) {
            int move = random.nextInt(maxLegalMoves);
            stateObservation.advance(actions[move]);
            moveSeq[nActionsPlayed] = actions[move];
            nActionsPlayed++;
        }
    }

    void nested(StateObservation stateObservation, int nestingLevel, Types.ACTIONS[] moveSeq, int nActionsPlayed) {
        int nbMoves = 0;
        // Types.ACTIONS[] moves = new Types.ACTIONS[maxLegalMoves];

        lengthBestRollout[nestingLevel] = -1;
        scoreBestRollout[nestingLevel] = Double.MIN_VALUE;
        float res;
        while (true) {
            if (stateObservation.isGameOver())
                return;
            if (nActionsPlayed >= maxRolloutLength)
                return;
            //return board.score ();
            nbMoves = maxLegalMoves;
            for (int i = 0; i < maxLegalMoves; i++) {
                StateObservation state = stateObservation.copy();
                Types.ACTIONS[] moveSeqCopy = new Types.ACTIONS[maxRolloutLength];
                int nActionsCopy = nActionsPlayed;
                for (int j = 0; j < nActionsPlayed; j++)
                    moveSeqCopy [j] = moveSeq [j];
                if (nestingLevel == 1) {
                    state.advance(actions[i]);
                    moveSeqCopy [nActionsCopy] = actions [i];
                    nActionsCopy++;
                    playout(state, moveSeqCopy, nActionsCopy);
                } else {
                    state.advance(actions[i]);
                    moveSeqCopy [nActionsCopy] = actions [i];
                    nActionsCopy++;
                    nested(state, nestingLevel - 1, moveSeqCopy, nActionsCopy);
                }
                double score = state.getGameScore();
                if (score > scoreBestRollout[nestingLevel]) {
                    //System.out.println ("level " + nestingLevel + "score " + score);
                    scoreBestRollout[nestingLevel] = score;
                    lengthBestRollout[nestingLevel] = maxRolloutLength;
                    for (int j = 0; j < maxRolloutLength; j++)
                       bestRollout[nestingLevel] [j] = moveSeqCopy [j];
                }
            }
            stateObservation.advance(bestRollout[nestingLevel][nActionsPlayed]);
            moveSeq[nActionsPlayed] = bestRollout[nestingLevel][nActionsPlayed];
            nActionsPlayed++;
        }
    }
}
