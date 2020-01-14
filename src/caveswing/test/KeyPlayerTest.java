package caveswing.test;

import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.design.CaveSwingFitnessSpace;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import evodef.SearchSpaceUtil;
import utilities.JEasyFrame;

public class KeyPlayerTest {
    public static void main(String[] args) throws Exception {
        boolean favourite = true;
        favourite = false;

        CaveSwingParams params = new CaveSwingParams();

        params = getFavouriteParams();

        params = getSearchSpaceParams();

        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        // gameState.setSoundEnabled(true);
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        view.scrollView = true;
        view.scrollWidth = 1000;

        // gameState.playRelease();

        String title = "Key Player GVGAISimpleTest";

        JEasyFrame frame = new JEasyFrame(view, title);

        KeyController player = new KeyController();
        frame.addKeyListener(player);
        ViewUtil.waitUntilReady(view);

        // play forever
        while (true) {
            while (!gameState.isTerminal()) {
                // get the action from the player, update the game state, and show a view
                int action = player.getAction(gameState.copy(), 0);
                // recall the action array is needed for generality for n-player games
                int[] actions = new int[]{action};
                gameState.next(actions);
                CaveGameState viewState = (CaveGameState) gameState.copy();
                view.setGameState(viewState).repaint();
                frame.setTitle(title + " : " + gameState.nTicks + " : " + gameState.isTerminal());
                Thread.sleep(40);
            }
            System.out.println("Final score: " + (int) gameState.getScore());
            Thread.sleep(2000);
            gameState = new CaveGameState().setParams(params).setup();
        }
    }

    static CaveSwingParams getFavouriteParams() {
        CaveSwingParams params = new CaveSwingParams();

        // todo: adjust parameter setting and see how they affect game play for you
        params.maxTicks = 500;
        params.gravity.y = 1.2;
        params.hooke = 0.01;
        params.gravity.x = -0.00;
        params.width = 2500;
        params.height *= 1.8;
        params.nAnchors = 8;
        // params.nAnchors /= 2;
        params.meanAnchorHeight *= 2;
        params.costPerTick *= 50;

        params.hooke *= 2;

        return params;
    }

    static CaveSwingParams getSearchSpaceParams() {
        CaveSwingFitnessSpace fitnessSpace = new CaveSwingFitnessSpace();
        int[] point = SearchSpaceUtil.randomPoint(fitnessSpace);

        // or choose a specific one:
        // point = new int[]{0, 6, 4, 2, 3, 4, 2, 3};
        // point = new int[]{0, 4, 4, 0, 4, 0, 0, 3};


        // point = new int[]{2, 0, 4, 1, 3, 0, 0, 2};
        // point = new int[]{2, 5, 4, 2, 1, 2, 0, 3};


        // point = new int[]{2, 4, 3, 2, 3, 0, 0, 3};

        // point = new int[]{2, 2, 4, 0, 4, 0, 2, 3};




        // point = new int[]{1, 2, 1, 1, 4, 2, 3, 2};

        // this one is better
        point = new int[]{1, 5, 2, 0, 4, 1, 3, 2};

        point = new int[]{0, 6, 3, 3, 3, 3, 3, 3};


        point = new int[]{2, 6, 3, 3, 3, 0, 2, 3};

        // point = new int[]{1, 2, 2, 1, 4, 0, 0, 3};
        // point = new int[]{2, 1, 4, 1, 2, 0, 0, 3};


        return fitnessSpace.getParams(point);

    }

}
