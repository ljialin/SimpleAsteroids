package ropegame;

import math.Vector2d;

/**
 * Created by Simon Lucas on 21/05/2017.
 */
public class TestMonkeyBall {
    public static void main(String[] args) {

        // make a monkey ball
        MonkeyBall monkeyBall = new MonkeyBall(new Vector2d(0, 0), new Vector2d(0, 0));

        // see how the position varies without an anchor
        // it should fall (i.e. increasing y) with the square of the number of iterations

        int nTicks = 10;

        for (int i=0; i<nTicks; i++) {
            monkeyBall.update();
            System.out.println(i + "\t " + monkeyBall.s);
        }

        // now put an anchor in
        monkeyBall.setAnchor(10, 0);

        System.out.println("Anchor at: " + monkeyBall.anchor);
        for (int i=0; i<nTicks; i++) {
            monkeyBall.update();
            System.out.println(i + "\t " + monkeyBall.s);
        }
    }
}
