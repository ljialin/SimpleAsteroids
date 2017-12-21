package numbergame;

import core.game.StateObservationMulti;
import ontology.Types;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by simonmarklucas on 30/03/2017.
 */


public class KeyController extends KeyAdapter {
    static int nControllers = 0;

    int action = 1;

    public KeyController() {
        System.out.println("Made a new key controller: " + ++nControllers);
    }

    public Types.ACTIONS action(StateObservationMulti obs) {
        // this is defined to comply with the standard interface
        return obs.getAvailableActions().get(action);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            action = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            action = 2;
        }
        if (key == KeyEvent.VK_SPACE) {
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            action = 1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            action = 1;
        }
        if (key == KeyEvent.VK_SPACE) {
        }
    }
}
