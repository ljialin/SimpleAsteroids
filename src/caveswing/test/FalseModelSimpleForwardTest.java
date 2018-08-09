package caveswing.test;

import agents.evo.EvoAgent;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.DefaultMutator;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.core.SimplePlayerInterface;
import utilities.JEasyFrame;

import java.awt.*;

public class FalseModelSimpleForwardTest {

    static boolean showEvolution = true;

    public static void main(String[] args) throws Exception {
        CaveSwingParams params = CoolTestParams.getParams();
        params.nAnchors *=2;

        // params.hooke = 0;
        CaveSwingParams falseParams = params.copy();
        falseParams.gravity.y *= 2.0;
        falseParams.gravity.x = -0.1;
        falseParams.hooke *= 0.0;

        // falseParams.lossFactor = 0.0;

        // params.maxTicks = 1;
        System.out.println(params.gravity);
        System.out.println(falseParams.gravity);

        int[] actions = {0};



        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveGameState falseState = (CaveGameState) gameState.copy();
        falseState.setParams(falseParams);

        int nSteps = 10;

        for (int i=0; i< nSteps; i++) {
            gameState.next(actions);
            falseState.next(actions);
        }

        System.out.println(gameState.avatar.s + " : "  + gameState.getScore());
        System.out.println(falseState.avatar.s + " : " + falseState.getScore());

    }

}
