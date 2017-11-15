package evogame;

import asteroids.*;
import utilities.ElapsedTimer;
import utilities.StatSummary;

/**
 * Created by simonmarklucas on 22/05/2016.
 */

public class SimpleEvaluator implements GameEvaluator {

    static int defaultEvals = 1;

    int startLevel = 3;
    int nLives = 5;

    @Override
    public double evaluate(ParamValues evoParams) {

        return evaluate(evoParams, defaultEvals);

    }

    public double evaluate(ParamValues evoParams, int nEvals) {

        StatSummary ss = new StatSummary();

        for (int i=0; i<nEvals; i++) {
            ss.add(evaluateOnce(evoParams));
        }

        System.out.println(ss);

        return ss.mean();

    }


    public double evaluateOnce(ParamValues evoParams) {

        // this evaluator optimises for a high score
        // using a particular agent and given a number
        // of game ticks

        GameParameters params = new GameParameters().injectValues(evoParams);
        boolean visible = false;
        int nTicks = 1000;
        GameState gameState = new GameState().setParams(params).initForwardModel();
        Game game = new Game(gameState, visible);

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);
        System.out.println(t);

        System.out.println(gameState.getScore());

        return gameState.getScore();
    }


    @Override
    public boolean betterThanOrEquals(double x, double y) {
        return x >= y;
    }


}
