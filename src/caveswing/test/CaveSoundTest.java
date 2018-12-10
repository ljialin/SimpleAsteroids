package caveswing.test;

import caveswing.core.CaveGameState;

public class CaveSoundTest {
    // todo: find some sounds files that actually work properly
    // they currently sound nothing like when played by clicking on the files
    public static void main(String[] args) throws Exception {
        CaveGameState gameState = new CaveGameState();
        gameState.setSoundEnabled(true);
        gameState.playAttach();
        Thread.sleep(500);
        gameState.playRelease();
        gameState.playCrash();
        Thread.sleep(500);
        gameState.playWin();
        Thread.sleep(500);
    }
}
