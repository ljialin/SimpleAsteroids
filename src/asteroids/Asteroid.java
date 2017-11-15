package asteroids;

import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;


import static asteroids.Constants.*;

public class Asteroid extends GameObject {
    static int nPoints = 16;
    static double radialRange = 0.6;
    static Stroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    int[] px, py;
    double rotRate;
    double rot;
    boolean dead;
    int index;

    // ToDo get rid of all the references to the Game Object
    // they are not necessary, not really
    // can manage all the collisions at gameState level instead
    // and then tell the objects whether to die etc
    // GameState gameState;

    public Asteroid(Vector2d s, Vector2d v, int index, double r) {
        super(s, v);
        // this.gameState = gameState;
        rotRate = (rand.nextDouble() - 0.5) * Math.PI / 20;
        rot = 0;
        this.index = index;
        this.r = r;
        setPolygon();
    }

    public Asteroid(Vector2d s, Vector2d v) {
        super(s, v);
    }

    public Asteroid copy() {

        Asteroid asteroid = new Asteroid(s, v);
        asteroid.rotRate = rotRate;
        asteroid.rot = rot;
        asteroid.index = index;
        asteroid.r = r;
        asteroid.px = px;
        asteroid.py = py;
        return asteroid;

    }


    public boolean dead() {
        return dead;
    }

    public void setPolygon() {
        px = new int[nPoints];
        py = new int[nPoints];
        for (int i = 0; i < nPoints; i++) {
            // generate within certain ranges
            //  in polar coords (radians)
            // then transform to cartesian
            double theta = (Math.PI * 2 / nPoints)
                    * (i + rand.nextDouble());
            double rad = r * (1 - radialRange / 2
                    + rand.nextDouble() * radialRange);
            px[i] = (int) (rad * Math.cos(theta));
            py[i] = (int) (rad * Math.sin(theta));
        }
//        System.out.println(Arrays.toString(px));
//        System.out.println(Arrays.toString(py));
    }

    public void draw(Graphics2D g) {
        // store coordinate system
        AffineTransform at = g.getTransform();
        if (isTarget) {
            g.setColor(Color.yellow);
        } else {
            g.setColor(Color.magenta);
        }
        g.translate(s.x, s.y);
        // System.out.println("Drawing at " + s);
        g.rotate(rot);
        // g.fillPolygon(px, py, px.length);
        g.setColor(Color.white);
        g.setStroke(stroke);
        g.drawPolygon(px, py, px.length);
        // restore original coordinate system
        g.setTransform(at);
    }

    public void update(GameState gameState) {
        s.add(v);
        rot += rotRate;
//        if (dead) {
//            gameState.asteroidDeath(this);
//        }
    }

    public void update() {
        s.add(v);
        rot += rotRate;
//        if (dead) {
//            gameState.asteroidDeath(this);
//        }
    }

    public String toString() {
        return s.toString();
    }

    public void hit() {
        dead = true;
        // gameState.asteroidDeath(this);
    }

}
