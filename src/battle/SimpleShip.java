package battle;

import asteroids.Action;
import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static asteroids.Constants.*;

import static battle.BattleGameParameters.*;


public class SimpleShip extends GameObject {

    // define the shape of the ship
    static int[] xp = {-2, 0, 2, 0};
    static int[] yp = {2, -2, 2, 0};

    // this is the thrust poly that will be drawn when the ship
    // is thrusting
    static int[] xpThrust = {-2, 0, 2, 0};
    static int[] ypThrust = {2, 3, 2, 0};
    // public static double scale = 15;


    // set these from the game params
    // define how quickly the ship will rotate
    double steerStep = rotation * Math.PI / 180;

    // this is the friction that makes the ship slow down over time
    // loss;

    double releaseVelocity = 0;
    double minVelocity = 2;
    public static double maxRelease = 10;

    // leave the game to set the color
    // Color color = Color.white;
    boolean thrusting = false;

    static double gravity = 0.0;

    // double shipSize = BattleGameParameters.shipSize;

    public Action action;

    // position and velocity
    public Vector2d d;


    public SimpleShip(Vector2d s, Vector2d v, Vector2d d) {
        super(new Vector2d(s), new Vector2d(v));
        this.d = new Vector2d(d);
    }

    public SimpleShip copy() {
        SimpleShip ship = new SimpleShip(s, v, d);
        // not sure whether we need actions to be stored
        if (action != null)
            ship.action = new Action(action);
        ship.releaseVelocity = releaseVelocity;
        return ship;
    }

    public double r() {
        return shipSize * 2.4;
    }

//    public Ship() {
//        super(new Vector2d(), new Vector2d());
//        d = new Vector2d(0, -1);
//    }
//

    public void reset() {
        s.set(width / 2, height / 2);
        v.zero();
        d.set(0, -1);
        dead = false;
        // System.out.println("Reset the ship ");
    }

    public void update() {
        update(action);
    }

    public SimpleShip update(Action action) {

        this.action = action;
        // what if this is always on?

        // action has fields to specify thrust, turn and shooting

        // action.thrust = 1;

        if (action.thrust > 0) {
            thrusting = true;
        } else {
            thrusting = false;
        }


        d.rotate(action.turn * steerStep);
        v.add(d, action.thrust * t * 0.3 / 2);
        v.y += gravity;
        // v.x = 0.5;
        v.mul(loss);
        s.add(v);
        // now create a missile if necessary
        // if the release velocity is zero
        releaseVelocity += 1.0;
        return this;
    }

    public String toString() {
        return s + "\t " + v;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        double rot = Math.atan2(d.y, d.x) + Math.PI / 2;
        g.rotate(rot);
        int scale = (int) shipSize;
        g.scale(scale, scale);
        // g.setColor(color);
        g.fillPolygon(xp, yp, xp.length);
        if (thrusting) {
            g.setColor(Color.red);
            g.fillPolygon(xpThrust, ypThrust, xpThrust.length);
        }
        g.setTransform(at);
    }

    public void hit() {
        super.hit();
    }

    public boolean dead() {
        return dead;
    }

}
