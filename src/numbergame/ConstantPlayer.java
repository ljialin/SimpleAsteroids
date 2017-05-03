package numbergame;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import javax.swing.*;

/**
 * Created by simonmarklucas on 30/03/2017.
 *
 *  Simple player that always does exactly the same thing
 *
 */
public class ConstantPlayer extends AbstractMultiPlayer{

    int action;

    public ConstantPlayer(int action) {
        this.action = action;
    }

    public Types.ACTIONS act(StateObservationMulti obs, ElapsedCpuTimer elapsedCpuTimer) {
        return obs.getAvailableActions().get(action);
    }
}
