package caveswing.controllers;


import caveswing.core.Constants;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyController extends KeyAdapter implements SimplePlayerInterface {

    int selectedAction = Constants.actionRelease;

    public void keyPressed(KeyEvent e) {
        // System.out.println(e);
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            selectedAction = Constants.actionAttach;
        }
    }

    public void keyReleased(KeyEvent e) {
        selectedAction = Constants.actionRelease;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        return selectedAction;
    }

    @Override
    public SimplePlayerInterface reset() {
        return this;
    }
}
