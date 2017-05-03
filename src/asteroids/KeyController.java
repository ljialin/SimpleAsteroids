package asteroids;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static asteroids.Constants.*;

/**
 * Created by Simon M. Lucas
 * sml@essex.ac.uk
 * Date: 26/12/11
 * Time: 16:00
 */
public class KeyController extends KeyAdapter
        implements Controller {
    Action action;
    static int nControllers = 0;

    public KeyController() {
        System.out.println("Made a new key controller: " + ++nControllers);
        action = new Action();
    }

    public Action action(GameState game) {
        // this is defined to comply with the standard interface
        return action;
    }

    public void setVehicle(Ship ship) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            action.thrust = 2;
        }
        if (key == KeyEvent.VK_LEFT) {
            action.turn = -1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            action.turn = 1;
        }
        if (key == KeyEvent.VK_SPACE) {
            action.shoot = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            action.thrust = 0;
        }
        if (key == KeyEvent.VK_LEFT) {
            action.turn = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            action.turn = 0;
        }
        if (key == KeyEvent.VK_SPACE) {
            action.shoot = false;
        }
    }
}
