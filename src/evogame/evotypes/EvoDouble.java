package evogame.evotypes;

/**
 * Created by simonmarklucas on 22/05/2016.
 */

import static asteroids.Constants.*;

public class EvoDouble {


    public static void main(String[] args) {
        // test it

        EvoDouble evo = new EvoDouble(10, 100, -50);
        System.out.println(evo);

    }

    double min;
    double max;

    // standard deviation of the noise
    double sd;

    // difference between min and max
    double range;

    // this number of sds between min and max values
    static double rangeFac = 50;

    double x;

    public EvoDouble(double min, double max) {
        this.min = min;
        this.max = max;
        range = max - min;
        sd = range / rangeFac;
    }

    public EvoDouble(double min, double max, double x) {
        this(min, max);
        this.x = x;
        forceRange();
    }

    public double doubleValue() { return x; }

    public int intValue() { return (int) x; }

    public EvoDouble initMid() {
        x = min + range/2 + rand.nextGaussian() * sd;
        forceRange();
        return this;
    }

    public EvoDouble initUniform() {
        x = min + range * rand.nextDouble();
        forceRange();
        return this;
    }

    public EvoDouble mutate() {
        x += sd * rand.nextGaussian();
        forceRange();
        return this;
    }

    public EvoDouble copy() {
        return new EvoDouble(min, max, x);
    }

    private void forceRange() {
        x = (x % range) + min;
    }

    public String toString() {
        return String.format("%.2f",x);
    }

    public EvoDouble initMin() {
        x = min;
        return this;
    }
}
