package ropegame;

import asteroids.GameObject;
import math.Vector2d;

import java.awt.*;

/**
 * Created by Simon Lucas on 21/05/2017.
 *
 * The MonkeyBall is the main avatar in the game.
 *
 * It will swing on a rope around an anchor point.
 */
public class MonkeyBall extends GameObject {

    static double gravity = 0.1; // units are in pixels per tick^2
    static double hooke = 0.15;

    public Vector2d anchor;

    public MonkeyBall(Vector2d s, Vector2d v) {
        super(s, v);
    }

    public void setAnchor(double x, double y) {
        anchor = new Vector2d(x,y);
    }

    public void removeAnchor() {
        anchor = null;
    }

    @Override
    public void update() {
        s.add(v);
        v.y += gravity;

        // now take account of the anchor
        if (anchor != null) {
            // introduce a tension force on the MonkeyBall towards the anchor
            Vector2d tension = new Vector2d(anchor);
            tension.subtract(s);
            tension.mul(hooke);
            v.add(tension);
        }
    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public boolean dead() {
        return false;
    }
}
