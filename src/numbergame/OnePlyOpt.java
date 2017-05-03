package numbergame;

import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

import java.util.ArrayList;

/**
 * Nash optimal play
 *
 */
public class OnePlyOpt extends AbstractMultiPlayer {

    public static void main(String[] args) {
        // run a simple test
        DiffGame dg = new DiffGame();
        dg.i1 = 2;
        dg.i2 = 8;
        OnePlyOpt player = new OnePlyOpt(1);
        player.act(dg, null);

    }

    int action;
    int myId, oppId;

    public OnePlyOpt(int myId) {
        this.myId = myId;
        oppId = 1 - myId;
    }

    public Types.ACTIONS act(StateObservationMulti obs, ElapsedCpuTimer elapsedCpuTimer) {
        ArrayList<Types.ACTIONS> actions = obs.getAvailableActions();

        for (int i=0; i<actions.size(); i++) {
            for (int j=0; j<actions.size(); j++) {
                Types.ACTIONS[] acts = new Types.ACTIONS[2];
                acts[myId] = actions.get(i);
                acts[oppId] = actions.get(j);
                StateObservationMulti tmp = obs.copy();
                tmp.advance(acts);
                double score = tmp.getGameScore(myId);

                System.out.format("%d\t %d\t %.1f\n", i, j, score);
            }
        }

        return obs.getAvailableActions().get(action);
    }
}
