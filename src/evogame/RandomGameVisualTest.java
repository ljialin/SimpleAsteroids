package evogame;

import asteroids.*;
import utilities.ElapsedTimer;

/**
 * Created by simonmarklucas on 22/05/2016.
 *
 * Aim is to make a simple random game and test it
 *
 *
 */
public class RandomGameVisualTest {

    public static void main(String[] args) {

        EvolvableParams evolvableParams = new EvolvableParams();

        System.out.println(evolvableParams);

        GameParameters params = new GameParameters().injectValues(evolvableParams);
        int startLevel = 1;
        int nLives = 5;
        boolean visible = true;
        int nTicks = 10000;
        GameState gameState = new GameState(params, startLevel, nLives);
        Game game = new Game(gameState, visible);

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);
        System.out.println(t);

        System.out.println(gameState.score);

    }

    public static void runVisually(ParamValues evoParams) {
        GameParameters params = new GameParameters().injectValues(evoParams);
        int startLevel = 1;
        int nLives = 5;
        boolean visible = true;
        int nTicks = 10000;
        GameState gameState = new GameState(params, startLevel, nLives);
        Game game = new Game(gameState, visible);

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);
        System.out.println(t);

        System.out.println(gameState.score);

    }

}
