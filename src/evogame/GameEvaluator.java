package evogame;

/**
 * Created by simonmarklucas on 22/05/2016.
 */


public interface GameEvaluator {
    // use this to evaluate the set of parameters for a game
    double evaluate(ParamValues params);

    // use this to decide whether we are minimising or maximising
    boolean betterThanOrEquals(double x, double y);

}
