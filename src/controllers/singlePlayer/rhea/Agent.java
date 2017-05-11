package controllers.singlePlayer.rhea;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import evodef.SearchSpaceUtil;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jialin Liu on 11/05/2017.
 * CSEE, University of Essex, UK
 * Email: jialin.liu@essex.ac.uk
 * <p>
 * Respect to Google Java Style Guide:
 * https://google.github.io/styleguide/javaguide.html
 */
public class Agent extends AbstractPlayer {
  public static int SEQUENCE_LENGTH = 10; // prediction horizon
  public int numActions; // number of available actions
  public Types.ACTIONS[] actions; // available actions
  public EvoAlg evoAlg; // evolutionary optimiser
  public static boolean useShiftBuffer = true;  // use shift buffer or not
  int nEvals; // maximal number of evaluations as budget
  int[] solution; // current solution (action sequence)

  /**
   * Public constructor with state observation and time due.
   * @param so state observation of the current game.
   * @param elapsedTimer Timer for the controller creation.
   * @param nEvals Optimisation budget (number of game evaluations)
   */
  public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer, EvoAlg evoAlg, int nEvals)
  {
    // Get the actions in a static array
    ArrayList<Types.ACTIONS> act = so.getAvailableActions();
    actions = new Types.ACTIONS[act.size()];
    for(int i = 0; i < actions.length; ++i)
    {
      actions[i] = act.get(i);
    }
    numActions = actions.length;

    System.out.println(Arrays.toString(actions));

    // Create the player
    this.evoAlg = evoAlg;
    this.nEvals = nEvals;
  }


  /**
   * Picks an action. This function is called every game step to request an
   * action from the player.
   * @param stateObs Observation of the current state.
   * @param elapsedTimer Timer when the action returned is due.
   * @return An action for the current state
   */
  public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
    // Set the state observation object as the new root of the tree
    // we'll set up a game adapter and run the algorithm independently each
    // time at least to being with
    int action;
    GameActionSpaceAdapter game = new GameActionSpaceAdapter(stateObs, SEQUENCE_LENGTH);

    // Shift buffer
    if (solution != null) {
      solution = SearchSpaceUtil.shiftLeftAndRandomAppend(solution, game);
      evoAlg.setInitialSeed(solution);
    }
    // optimisation
    solution = evoAlg.runTrial(game, nEvals);

    // System.out.println(Arrays.toString(solution) + "\t " + game.evaluate(solution) + "\t " + useShiftBuffer);

    action = solution[0];

    // if not using shift buffer, reset the solution sequence
    if (!useShiftBuffer) solution = null;

    //... and return the action to make
    return actions[action];
  }
}
