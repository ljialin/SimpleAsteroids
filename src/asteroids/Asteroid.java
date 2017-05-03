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
    GameState game;

    public Asteroid(GameState game, Vector2d s, Vector2d v, int index) {
        super(s, v);
        this.game = game;
        rotRate = (rand.nextDouble() - 0.5) * Math.PI / 20;
        rot = 0;
        this.index = index;
        r = game.params.radii[index];
        setPolygon();
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

    public void update() {
        s.add(v);
        rot += rotRate;
    }
    public String toString() {
        return s.toString();
    }

    public void hit() {
        dead = true;
        game.asteroidDeath(this);
    }
}
