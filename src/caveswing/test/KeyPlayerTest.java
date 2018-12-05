package caveswing.test;

import caveswing.controllers.KeyController;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.util.ViewUtil;
import caveswing.view.CaveView;
import utilities.JEasyFrame;

public class KeyPlayerTest {
    public static void main(String[] args) throws Exception {
        CaveSwingParams params = new CaveSwingParams();

        // todo: adjust parameter setting and see how they affect game play for you
        params.maxTicks = 500;
        params.gravity.y = 0.5;
        params.hooke = 0.01;
        params.gravity.x = -0.00;
        params.width = 1500;
        params.height *= 1.5;


        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        view.scrollView = true;
        view.scrollWidth = 800;

        String title = "Key Player GVGAISimpleTest";

        JEasyFrame frame = new JEasyFrame(view, title);


        KeyController player = new KeyController();
        frame.addKeyListener(player);
        ViewUtil.waitUntilReady(view);

        while (!gameState.isTerminal()) {
            // get the action from the player, update the game state, and show a view
            int action = player.getAction(gameState.copy(), 0);
            // recall the action array is needed for generality for n-player games
            int[] actions = new int[]{action};
            gameState.next(actions);
            CaveGameState viewState = (CaveGameState) gameState.copy();
            view.setGameState(viewState).repaint();
            frame.setTitle(title + " : " + gameState.nTicks + " : " +gameState.isTerminal());
            Thread.sleep(40);
        }
        System.out.println("Final score: " + gameState.getScore());
    }
}
