package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import math.Vector2d;
import planetwar.AbstractGameState;

import java.awt.*;

import static asteroids.Constants.*;

public class GameState implements AbstractGameState {

    public int initialLives = 3;
    public int initialLevel = 3;
    public ForwardModel forwardModel;

    State state, nextState;
    String message;
    int wait;
    // Font font;

    public GameParameters params;

    static ActionAdapter actionAdapter = new ActionAdapter();

    public GameState() {
        // setParams(new GameParameters().injectValues(new DefaultParams()));
    }

    public int nActions() {
        return actionAdapter.actions.length;
    }

    public GameState setParams(GameParameters params) {
        this.params = params;
        return this;
    }

    public GameState initForwardModel() {
        forwardModel = new ForwardModel().setGameState(this);
        forwardModel.level = initialLevel;
        forwardModel.nLives = initialLives;
        forwardModel.makeAsteroids();
        makeShip();
        state = State.WaitingToStart;
        return this;
    }

    public GameState copy() {
        GameState gs = new GameState();
        gs.state = state;
        gs.nextState = nextState;
        gs.message = message;
        gs.wait = wait;

        // no need to copy the params
        gs.params = params;  // .copy();

        gs.forwardModel = forwardModel.copy();

        return gs;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        update(actionAdapter.getAction(actions[0]));
        return this;
    }


    // update needs to take an action, or more generally a list of actions to
    // advance it to the next state

    public void update(Action action) {
        // System.out.println("GameState.update() " + state + " : " + action);
        switch (state) {
            case WaitingToStart: {
                if (action.shoot)
                    state = State.Playing;
                break;
            }
            case Playing: {
                forwardModel.update(action);
                // System.out.println(list.nAsteroids());
                if (forwardModel.ship.dead()) {
                    // System.out.println("Dead ship!!!");
                    state = State.LifeLost;
                }
                if (forwardModel.nAsteroids() == 0)
                    state = State.LevelCleared;
                break;
            }
            case LifeLost: {
                wait = 100;
                forwardModel.nLives--;
                forwardModel.incScore(-200);
                if (forwardModel.nLives <= 0) {
                    nextState = State.GameOver;
                }
                else {
                    nextState = State.ReEntry;
                }
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
//                if (forwardModel.ship.dead()) {
//                    makeShip();
//                }
                makeShip();
                // forwardModel.moveAsteroids(this);
                forwardModel.makeSafe(this);
                if (forwardModel.isShipSafe()) {
                    state = State.Playing;
                }
                break;
            }
            case LevelCleared: {
                wait = 100;
                forwardModel.level++;
                forwardModel.clearObjects();
                makeShip();
                forwardModel.makeAsteroids(forwardModel.level);
                // System.out.println("nAsteroids: " + list.nAsteroids());
                nextState = State.Playing;
                state = State.Waiting;
                message = "Well done; prepare for next level!: " + forwardModel.level;
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
        // gameState state tie in with what is drawn!
        // simple but not wholly satisfying is to use
        // another switch statement
        // System.out.println("GameState.draw()");
        switch (state) {
            case WaitingToStart: {
                View.messageScreen(g, "Hit Space Bar To Start");
                break;
            }

            case Playing: case ReEntry : {
                forwardModel.draw(g);
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

//    private void makeAsteroids(int nAsteroids) {
//        // System.out.println("Making nAsteroids: " + nAsteroids);
//        Vector2d centre = new Vector2d(width / 2, height / 2);
//        while (forwardModel.nAsteroids() < nAsteroids) {
//            // choose a random position and velocity
//            Vector2d s = new Vector2d(rand.nextDouble() * width,
//                    rand.nextDouble() * height);
//            Vector2d v = new Vector2d(rand.nextGaussian(), rand.nextGaussian());
//            if (s.dist(centre) > safeRadius && v.mag() > 0.5) {
//                // Asteroid a = new Asteroid(this, s, v, 0);
//
//                // these move in interesting ways ...
//                // Asteroid a = new LissajousAsteroid(this, s, v, 0);
//                double r = params.radii[0];
//                Asteroid a = new Asteroid(s, v, 0, r);
//                forwardModel.addAsteroid(a);
//            }
//        }
//    }
//
//    private void makeColumns(int nColumns) {
//        Vector2d centre = new Vector2d(width / 2, height / 2);
//        // assumes that the gameState object list is currently empty
//        for (int i=0; i<nColumns; i++) {
//            list.objects.add(new Column(this, i));
//        }
//    }

    public boolean gameOn() {
        return state != State.GameOver;
    }



    public void makeShip() {
        Ship ship = new Ship(this,
                new Vector2d(width / 2, height / 2),
                new Vector2d(),
                new Vector2d(0, -1));
        forwardModel.setShip(ship);
    }

    public void shipDeath() {
        // trigger some explosions
    }

    // public asteroidDeath()

    public double getScore() {
        return forwardModel.score;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

}
