package gvglink;

import controllers.multiPlayer.ea.Agent;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import ga.SimpleRMHC;
import ontology.Types;
import planetwar.GameState;
import planetwar.PlanetWarView;
import tools.ElapsedCpuTimer;

import java.util.Random;

public class PlanetWarsLinkStateDebug {

    // currently the link state is not working properly
    // this class will help to debug it

    public static void main(String[] args) throws Exception {
        int seed = 4;

        // problem has now been identified
        //

        GameState gameState = new GameState().defaultState(seed);
        PlanetWarsLinkState linkState = new PlanetWarsLinkState(gameState);


        System.out.println(PlanetWarsLinkState.actions);

        int[] a1 = new int[]{0, 1, 2, 3, 2, 1, 3, 3, 2, 4, 2, 4};
        int[] a2 = new int[]{1, 0, 0, 4, 3, 1, 3, 3, 2, 4, 2, 4};
        for (int i = 0; i < a1.length; i++) {
            gameState.next(new int[]{a1[i], a2[i]});
            Types.ACTIONS[] la = new Types.ACTIONS[]{PlanetWarsLinkState.actions.get(a1[i]),
                    PlanetWarsLinkState.actions.get(a2[i])};
            // linkState.advance(la);
            System.out.println("Link state: "  + linkState.getGameScore());
            System.out.println("Game state: "  + gameState.getScore());
            System.out.println();
        }
    }
}
