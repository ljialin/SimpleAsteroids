package evogame;

import asteroids.Game;
import asteroids.GameState;
import utilities.ElapsedTimer;

/**
 * Created by simonmarklucas on 22/05/2016.
 *
 * Aim is to try different parameter settings to see if they differ significantly
 *
 *
 */
public class ParameterSettingsTest {

    public static void main(String[] args) {

        DefaultParams defParams = new DefaultParams();
        defParams.shipSteer = 1;

        // GameParameters params = new GameParameters().injectValues(evolvableParams);
        GameParameters params = new GameParameters().injectValues(defParams);


        SimpleEvaluator eval = new SimpleEvaluator();

        int nTrials = 30;
        eval.evaluate(defParams, nTrials);

        // exit if we're done
        // comment this out to proceed to visual test
        // System.exit(0);

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
