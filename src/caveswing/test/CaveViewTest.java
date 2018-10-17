package caveswing.test;

import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import caveswing.view.CaveView;
import utilities.JEasyFrame;

public class CaveViewTest {
    public static void main(String[] args) {
        CaveSwingParams params = new CaveSwingParams();
        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        CaveView view = new CaveView().setGameState(gameState).setParams(params);
        new JEasyFrame(view, "Cave View GVGAISimpleTest");
    }
}
