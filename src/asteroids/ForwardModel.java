package asteroids;

import math.Vector2d;

import java.awt.*;
import java.util.ArrayList;

import static asteroids.Constants.*;
import static asteroids.Constants.lifeThreshold;

public class ForwardModel {

    // Random Ran

    static int missileCost = -10;
    // limit on number of active missiles
    static int liveMissileLimit = 5;

    public static int totalTicks = 0;
    int nTicks = 0;

    AsteroidsGameState gameState;

    Ship ship;
    ArrayList<Missile> missiles;
    ArrayList<Asteroid> asteroids;
    int score;
    int nLives;
    int level;

    public ForwardModel() {
        // this.gameState = gameState;
        asteroids = new ArrayList<>();
        missiles = new ArrayList<>();
    }

    public ForwardModel setGameState(AsteroidsGameState gameState) {
        this.gameState = gameState;
        return this;
    }

    public ForwardModel setShip(Ship ship) {
        this.ship = ship;
        return this;
    }

    public ForwardModel copy() {
        ForwardModel fm = new ForwardModel();
        fm.nTicks = nTicks;
        fm.score = this.score;
        fm.nLives = this.nLives;
        fm.ship = this.ship.copy();
        fm.nLives = this.nLives;
        fm.level = this.level;
        fm.gameState = gameState;
        for (Asteroid asteroid : asteroids) {
            fm.asteroids.add(asteroid.copy());
        }
        for (Missile missile : missiles) {
            fm.missiles.add((missile.copy()));
        }
        return fm;
    }

    public ForwardModel next(Action action) {
        update(action);
        return this;
    }

    public void update(Action action) {
        updateShip(action);
        updateMissiles();
        updateAsteroids();
        nTicks++;
        totalTicks++;
//        System.out.println(nTicks + "\t " + totalTicks);

    }

    public void updateShip(Action action) {
        ship.update(action);
        wrap(ship);
        if (ship.pending != null) {
            tryAddMissile(ship.pending);
            ship.pending = null;
        }
        // now check to see whether it has been hit by a missile (optional)
        // not done for now

        // or has hit an asteroid
        for (int i=0; i<asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (!ship.dead()) {
                if (overlap(ship, asteroid)) {
                    shipDeath();
                    asteroidDeath(asteroid);
                }
            }
        }
    }


    private void tryAddMissile(Missile pending) {
        if (missiles.size() < liveMissileLimit) {
            missiles.add(pending);
            incScore(missileCost);
        }
    }

    public void updateAsteroids() {

        for (Asteroid asteroid : asteroids) {
            if (!asteroid.dead()) {
                asteroid.update();
                wrap(asteroid);
            }
        }
        // now keep only the live ones
        ArrayList<Asteroid> tmp = new ArrayList<>();
        for (Asteroid asteroid : asteroids)
            if (!asteroid.dead()) tmp.add(asteroid);
        asteroids = tmp;

    }

    public int nAsteroids() {
        return asteroids.size();
    }

    public void addAsteroid(Asteroid asteroid) {
        if (asteroids == null) {
            asteroids = new ArrayList<>();
        }
        asteroids.add(asteroid);
    }

    public void updateMissiles() {
        for (Missile missile : missiles) {
            if (!missile.dead()) {
                missile.update();
                wrap(missile);
                // check each asteroid
                for (int i=0; i<asteroids.size(); i++) {

                    Asteroid asteroid = asteroids.get(i);

                    if (!missile.dead() && !asteroid.dead() && overlap(missile, asteroid)) {
                        missile.hit();
                        asteroidDeath(asteroid);
                    }
                }
            }
        }
        // now keep only the live ones
        ArrayList<Missile> tmp = new ArrayList<>();
        for (Missile missile : missiles)
            if (!missile.dead()) tmp.add(missile);
        missiles = tmp;
    }

    public void deleteMissiles() {

    }

    private boolean overlap(GameObject actor, GameObject ob) {
        if (actor.equals(ob)) {
            return false;
        }
        if (ob instanceof PolyContains) {
            // System.out.println("Checking containment");
            return ((PolyContains) ob).contains(actor.s);
        }
        // otherwise do the default check
        double dist = actor.s.dist(ob.s);
        boolean ret = dist < (actor.r() + ob.r());
        return ret;
    }

    public void wrap(GameObject ob) {
        // only wrap objects which are wrappable
        if (ob.wrappable()) {
            ob.s.x = (ob.s.x + width) % width;
            ob.s.y = (ob.s.y + height) % height;
        }
    }

    public void asteroidDeath(Asteroid a) {

        // if we still have smaller ones to
        // work through then do so
        // otherwise do nothing
        // score += asteroidScore;
        a.dead = true;
        if (a.index < gameState.params.radii.length - 1) {
            // add some new ones at this position
            for (int i=0; i<nSplits; i++) {
                Vector2d v1 = a.v.copy().add(
                        rand.nextGaussian(), rand.nextGaussian());

                double r = gameState.params.radii[a.index+1];
                Asteroid splitRock = new Asteroid(a.s.copy(), v1,a.index + 1, r);
                splitRock.dead = false;
                asteroids.add(splitRock);
            }
        }
        incScore(asteroidScore[a.index]);
    }

    // need a similar way to indicate the clearance of a column pipe

    public void incScore(int s) {
        score += s;
        if ( (score -s) % lifeThreshold > score % lifeThreshold ) {
            // nLives++;
        }
        // System.out.println(score + " : "  + nLives);
    }

    public void shipDeath() {
        // may not need to do anything...
        ship.dead = true;
    }


    public void makeAsteroids() {
        makeAsteroids(level);
    }

    static double rockSpeed = 2.0;

    public void makeAsteroids(int nAsteroids) {
        // System.out.println("Making nAsteroids: " + nAsteroids);
        // asteroids = new ArrayList<>();
        Vector2d centre = new Vector2d(width / 2, height / 2);
        while (nAsteroids() < nAsteroids) {
            // choose a random position and velocity
            Vector2d s = new Vector2d(rand.nextDouble() * width,
                    rand.nextDouble() * height);
            Vector2d v = new Vector2d(rand.nextGaussian(), rand.nextGaussian());
            if (s.dist(centre) > safeRadius && v.mag() > 0.5) {
                // Asteroid a = new Asteroid(this, s, v, 0);

                // these move in interesting ways ...
                // Asteroid a = new LissajousAsteroid(this, s, v, 0);
                v.mul(rockSpeed);
                double r = gameState.params.radii[0];
                Asteroid a = new Asteroid(s, v, 0, r);
                this.addAsteroid(a);
            }
        }
    }

    public void draw(Graphics2D g) {
        // System.out.println("In draw(): " + n);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);

        // now draw the gameState objects

        for (Asteroid asteroid : asteroids) asteroid.draw(g);
        for (Missile missile : missiles) missile.draw(g);
        ship.draw(g);

    }

    public boolean isShipSafe() {
        for (Asteroid asteroid : asteroids) {
            if (overlap(ship, asteroid))
                return false;
        }
        return true;
    }

    public void clearObjects() {
        asteroids.clear();
        missiles.clear();
    }

    public void moveAsteroids(AsteroidsGameState gameState) {
        for (Asteroid asteroid : asteroids) {
            asteroid.update(gameState);
        }
    }

    static double safeDistance = 100;
    public void makeSafe(AsteroidsGameState gameState) {
        for (Asteroid asteroid : asteroids) {
            while (ship.s.dist(asteroid.s) < safeDistance) {
                Vector2d s = new Vector2d(rand.nextDouble() * width,
                        rand.nextDouble() * height);

                asteroid.s = s;
            }
        }
    }
}


