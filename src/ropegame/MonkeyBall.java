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

    static double loss = 0.99;
    static double gravity = 0.2; // units are in pixels per tick^2
    static double hooke = 0.005;

    static int radius = 20;

    static Color ropeColor = new Color(178,34,34);
    static Stroke rope = new BasicStroke(radius/4);

    public Vector2d anchor;

    public MonkeyBall() {
        super(new Vector2d(320,240), new Vector2d(0,0));
    }

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
        v.mul(loss);
    }

    @Override
    public void draw(Graphics2D g) {
        g.fillOval((int) s.x - radius/2, (int) s.y - radius/2, radius, radius);
        // use a temp reference in case a different thread sets the anchor to null
        Vector2d temp = anchor;
        g.setStroke(rope);
        if (temp != null) {
            g.setColor(ropeColor);
            g.drawLine((int) s.x, (int) s.y, (int) temp.x, (int) temp.y);
        }
    }

    @Override
    public boolean dead() {
        return false;
    }
}
