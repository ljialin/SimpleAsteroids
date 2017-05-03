package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import math.Vector2d;

import java.awt.*;

import static asteroids.Constants.*;

public class GameState {
    public int level;
    public int score;
    public int nLives = 3;
    LiveList list;

    Ship ship;
    State state, nextState;
    String message;
    int wait;
    // Font font;

    public GameParameters params;

    public GameState() {

        params = new GameParameters().injectValues(new DefaultParams());
    }

    public GameState(GameParameters params, int level, int nLives) {
        this.params = params;
        this.level = level;
        this.nLives = nLives;
        list = new LiveList();

        makeAsteroids(level);
        // makeColumns(5);
        makeShip();
        state = State.WaitingToStart;
    }

    public GameState(int level, int nLives) {
        this(new GameParameters().injectValues(new DefaultParams()), level, nLives);
//        this.level = level;
//        this.nLives = nLives;
//        list = new LiveList();
    }

    public GameState(int level) {
        this(level, 3);
    }

    public GameState copy() {
        GameState gs = new GameState();

        gs.level = level;
        gs.score = score;
        gs.nLives = nLives;
        gs.state = state;
        gs.nextState = nextState;
        gs.message = message;
        gs.wait = wait;
        gs.params = params.copy();


        // todo: need to make the handling of the object list more general
        // since currently we would be in danger of making two copies of the ship

        // the solution is to rewrite the livelist to store everything needed
        // in the list and then simply add the ship in afterwards


        gs.list = list.copy();

        gs.ship = ship.copy();

        // add the ship separately, we explicitly did not copy it before
        gs.list.add(gs.ship);

        return gs;
    }


    // update needs to take an action, or more generally a list of actions to
    // advance it to the next state

    public void update() {
        // System.out.println("GameState.update()");
        switch (state) {
            case WaitingToStart: {
                if (ship.action.shoot)
                    state = State.Playing;
                break;
            }
            case Playing: {
                list.update();
                // System.out.println(list.nAsteroids());
                if (ship.dead()) {
                    // System.out.println("Dead ship!!!");
                    state = State.LifeLost;
                }
                if (list.nAsteroids() == 0) state = State.LevelCleared;
                break;
            }
            case LifeLost: {
                wait = 100;
                nLives--;
                if (nLives <= 0) nextState = State.GameOver;
                else nextState = State.ReEntry;
                message = "Oops: be more careful!";
                // System.out.println("Life lost!");
                state = State.Waiting;
                break;
            }

            case GameOver: {
                message = "Game Over";
                // System.out.println("Game Over");
                state = State.GameOver;
                break;
            }

            case ReEntry : {
                // do nothing until it is isSafe
                if (ship.dead()) makeShip();
                list.moveAsteroids();
                if (list.isSafe(ship)) {
                    state = State.Playing;
                }
                break;
            }
            case LevelCleared: {
                wait = 100;
                level++;
                list.clear();
                makeShip();
                makeAsteroids(level);
                // System.out.println("nAsteroids: " + list.nAsteroids());
                nextState = State.Playing;
                state = State.Waiting;
                message = "Well done; prepare for next level!: " + level;
                break;
            }
            case Waiting: {
                if (wait <= 0) {
                    // System.out.println("nAsteroids: " + list.nAsteroids());
                    state = nextState;
                }
                wait--;
            }
        }
    }

    public void draw(Graphics2D g) {
        // interesting: the best way to make the
        // game state tie in with what is drawn!
        // simple but not wholly satisfying is to use
        // another switch statement
        // System.out.println("GameState.draw()");
        switch (state) {
            case WaitingToStart: {
                View.messageScreen(g, "Hit Space Bar To Start");
                break;
            }

            case Playing: case ReEntry : {
                list.draw(g);
                break;
            }

            case LevelCleared: {
                View.messageScreen(g, "Level cleared!");

                break;
            }

            case Waiting : {
                View.messageScreen(g, message);
                break;
            }
            default: {
                View.messageScreen(g, "Game Over");
            }
        }
    }

    private void makeAsteroids(int nAsteroids) {
        // System.out.println("Making nAsteroids: " + nAsteroids);
        Vector2d centre = new Vector2d(width / 2, height / 2);
        while (list.nAsteroids() < nAsteroids) {
            // choose a random position and velocity
            Vector2d s = new Vector2d(rand.nextDouble() * width,
                    rand.nextDouble() * height);
            Vector2d v = new Vector2d(rand.nextGaussian(), rand.nextGaussian());
            if (s.dist(centre) > safeRadius && v.mag() > 0.5) {
                // Asteroid a = new Asteroid(this, s, v, 0);

                // these move in interesting ways ...
                // Asteroid a = new LissajousAsteroid(this, s, v, 0);
                Asteroid a = new Asteroid(this, s, v, 0);
                list.objects.add(a);
            }
        }
        // System.out.println("Made " + list.objects.size());
//        System.out.println(list.objects);
    }

    private void makeColumns(int nColumns) {
        Vector2d centre = new Vector2d(width / 2, height / 2);
        // assumes that the game object list is currently empty
        for (int i=0; i<nColumns; i++) {
            list.objects.add(new Column(this, i));
        }
    }

    public boolean gameOn() {
        return state != State.GameOver;
    }

    public void add(GameObject ob) {
        list.add(ob);
    }

    public void makeShip() {
        ship = new Ship(this,
                new Vector2d(width / 2, height / 2),
                new Vector2d(),
                new Vector2d(0, -1));
        add(ship);
    }

    public void shipDeath() {
        // trigger some explosions
    }

    // public asteroidDeath()


    public void asteroidDeath(Asteroid a) {

        // if we still have smaller ones to
        // work through then do so
        // otherwise do nothing
        // score += asteroidScore;
        if (a.index < params.radii.length - 1) {
            // add some new ones at this position
            for (int i=0; i<nSplits; i++) {
                Vector2d v1 = a.v.copy().add(rand.nextGaussian(), rand.nextGaussian());
                Asteroid a1 = new Asteroid(this, a.s.copy(), v1, a.index + 1);
                list.add(a1);
            }
        }
        incScore(asteroidScore[a.index]);
    }


    // need a similar way to indicate the clearance of a column pipe

    private void incScore(int s) {
        score += s;
        if ( (score -s) % lifeThreshold > score % lifeThreshold ) {
            nLives++;
        }
    }
}
