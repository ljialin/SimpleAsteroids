package ropegame;

import math.Vector2d;
import utilities.JEasyFrame;

import java.util.Random;

/**
 * Created by sml on 23/05/2017.
 */

public class RopeGameTestController {

    public static void main(String[] args) throws Exception {

        // make a game state
        // make a CaveView based on the state

        // figure out a way to update it
        // the idea is to provide a simple demo of the game

        RopeGameState gameState = new RopeGameState();
        RopeGameView view = new RopeGameView(gameState);
        JEasyFrame frame = new JEasyFrame(view, "Monkey Ball");
        Random random = new Random();

        // this is the delay per frame update, but also use it
        // as the basis of switching an anchor point on and off

        int delay = 40;

        Vector2d anchor = null;

        for (int i=0; i<5000; i++) {

//            // each time update
//            if ((i / 40) % 2 == 0) {
//                anchor = null;
//            } else {
//                // if it is null, make a new one at a Random location
//                // otherwise leave alone
//                int w = RopeGameState.width;
//                if (anchor == null) {
//                    anchor = new Vector2d(w/4 + random.nextInt(w/2), w/5 + random.nextInt(w/3));
//                }
//            }

            gameState.update(view.anchor);
            view.repaint();
            Thread.sleep(delay);

        }
    }
}
