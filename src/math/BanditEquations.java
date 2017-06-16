package math;

import java.util.Random;

/**
 * Created by sml on 16/06/2017.
 */
public class BanditEquations {

    public static Random rand = new Random();

    public static double randStrength = 1e-6;

    public static double defaultK = 1.0;
    public static double epsilon = 1e-1;

    public static double UCB(double mean, double nc, double N) {
        return UCB(mean, nc, N, defaultK);
    }

    // add a small and harmless random dither
    public static double UCB(double mean, double nc, double N, double k) {
        return mean + k * Math.sqrt(Math.log(N) / (epsilon + nc)) + rand.nextDouble() * randStrength;
    }

    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

}
