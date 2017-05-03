package evogame;

/**
 * Created by simonmarklucas on 22/05/2016.
 *
 *  This one just minimises some error values - just to test the Evolutionary Algorithm
 *
 */
public class TestEvaluator implements GameEvaluator {
    @Override
    public double evaluate(ParamValues evoParams) {

        double perfectTTL = 90;
        double err = perfectTTL - evoParams.getMissileTTL();

        // return the absolute difference between the two values
        return Math.abs(err);

    }

    @Override
    public boolean betterThanOrEquals(double x, double y) {
        // set this up to be a minimising problem
        return x <= y;
    }


}
