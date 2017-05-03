package asteroids;

import math.Vector2d;

/**
 * Created by simonlucas on 06/06/15.
 */

import static asteroids.Constants.*;


public class LissajousAsteroid extends Asteroid {


    public LissajousAsteroid(GameState game, Vector2d s, Vector2d v, int index) {
        super(game, s, v, index);
    }

    public boolean wrappable() { return true; }

    double t = rand.nextDouble();

    public void update() {
        t += 0.005;
        setPosition(t, s);
        // System.out.println("t = " + t);
    }

    void setPosition(double t, Vector2d s) {
        double x = Math.sin(t+Math.PI/6) +
                Math.sin(2.0 * t + Math.PI) + 0.5* Math.sin(4*t);
        double y = 1.0*Math.sin(1.0 * t) + Math.sin(Math.PI/5 + 2*t) +
                0.5 * Math.sin(3*t + Math.PI/8);
        s.x = 0.5 * x * width + width/2;
        s.y = 0.5 * y * height + height/2;
    }
}



