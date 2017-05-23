package ropegame;

import math.Vector2d;

/**
 * Created by Simon Lucas on 21/05/2017.
 *
 *  Ready for an interesting and fast to compute game
 *  involving and agent using rope to swing from place
 *  to place - may then end up with PTSP-like variants etc.
 *
 */
public class RopeGameState {

    public static int width = 640;
    public static int height = 480;

    MonkeyBall monkeyBall = new MonkeyBall();

    public void update(Vector2d anchor) {
        monkeyBall.anchor = anchor;
        monkeyBall.update();
    }

}
