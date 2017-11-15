package asteroids;

import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static asteroids.Constants.*;

public class Ship extends GameObject {

    // define the shape of the ship
    static int[] xp = {-2, 0, 2, 0};
    static int[] yp = {2, -2, 2, 0};

    // this is the thrust poly that will be drawn when the ship
    // is thrusting
    static int[] xpThrust = {-2, 0, 2, 0};
    static int[] ypThrust = {2, 3, 2, 0};
    // public static double scale = 15;


    // set these from the gameState params
    // define how quickly the ship will rotate
    double steerStep; //  = 10 * Math.PI / 180;

    // this is the friction that makes the ship slow down over time
    double loss;

    double releaseVelocity = 0;
    double minVelocity = 2;
    public static double maxRelease = 10;
    Color color = Color.white;
    boolean thrusting = false;

    static double gravity = 0.01;

    public Action action;
    public GameState game;

    // a ship fires a missile this way
    public Missile pending;

    // position and velocity
    public Vector2d d;


    public Ship(GameState game, Vector2d s, Vector2d v, Vector2d d) {
        super(new Vector2d(s), new Vector2d(v));
        this.game = game;
        loss = game.params.shipLoss;
        steerStep = game.params.shipSteer * Math.PI / 180;
        this.d = new Vector2d(d);
    }

    public Ship copy() {
        Ship ship = new Ship(game, s, v, d);
        ship.action = new Action(action);
        ship.releaseVelocity = releaseVelocity;
        ship.pending = pending;
        ship.thrusting = thrusting;
        return ship;
    }

    public double r() {
        return game.params.shipSize * 2.4;
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

    public void update(GameState gameState) {
        // do nothing here, wait for the action call instead
        // update(action);
    }

    public Ship update(Action action) {

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
        if (action.shoot) {
            tryMissileLaunch();
        } else {
        }
        return this;
    }

    private void tryMissileLaunch() {
//        System.out.println("Trying a missile launch");
//        System.out.println("Release velocity: " + releaseVelocity);
        if (releaseVelocity > maxRelease) {
            // System.out.println("Missile fired!");
            releaseVelocity = Math.max(releaseVelocity, game.params.missileMinVelocity * 2);
            Missile m = new Missile(s, new Vector2d(0, 0), game.params.missileTTL, game.params.missileRadius);
            releaseVelocity = Math.min(releaseVelocity, maxRelease);
            m.v.add(d, releaseVelocity);
            // make it clear the ship
            m.s.add(m.v, (r() + game.params.missileRadius) * 1.5 / m.v.mag());
            releaseVelocity = 0;
            // got it! - it adds it to the old version of the gameState
            pending = m;

            // used to add the missile to the gameState here, but caused problems in
            // the way the reference was being copied, hence removed it
            // gameState.add(m);
            // System.out.println("Fired: " + m);
            // sounds.fire();
        } else {
//            System.out.println("Failed!");
        }
    }

    public String toString() {
        return s + "\t " + v;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        double rot = Math.atan2(d.y, d.x) + Math.PI / 2;
        g.rotate(rot);
        int scale = game.params.shipSize;
        g.scale(scale, scale);
        g.setColor(color);
        g.fillPolygon(xp, yp, xp.length);
        if (thrusting) {
            g.setColor(Color.red);
            g.fillPolygon(xpThrust, ypThrust, xpThrust.length);
        }
        g.setTransform(at);
    }

    public void hit() {
        // super.hit();
        // System.out.println("Ship destroyed");
        dead = true;
        game.shipDeath();
        // sounds.play(sounds.bangLarge);
    }

    public boolean dead() {
        return dead;
    }


}
