package controllers.multiPlayer.smlrand;

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
import java.util.Arrays;
import java.util.Random;

/**
 * Created by sml on 23/01/2017.
 */
public class Agent extends AbstractMultiPlayer {

    static Random random = new Random();


    public Agent()
    {
    }


    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        int act = random.nextInt(stateObs.getAvailableActions().size());
        //Set the state observation object as the new root of the tree.


        return stateObs.getAvailableActions().get(act);
    }

    @Override
    public Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedCpuTimer) {

        int act = random.nextInt(stateObs.getAvailableActions().size());

        return stateObs.getAvailableActions().get(act);
    }



}
