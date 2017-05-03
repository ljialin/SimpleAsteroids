package test;

import java.util.Arrays;

/**
 * Created by sml on 09/05/2016.
 */
public class EntropyTest {

    public static void main(String[] args) {

        // System.out.println(Math.log(2));

        int n = 64;
        for (int i=2; i<=n; i++) {
            // System.out.println(i + "\t " + maxEnt(i));
            // System.out.println(i + "\t " + sumSeries(i) + "\t " + (Math.log(i) + 0.5772));
            // System.out.println(i + "\t " + Math.log(1.0 / i) / Math.log(2));
        }

        // System.out.println(1e-20 * Math.log(1e-20));


        // double[] x = {0, 10, 30, 20, 10, 0, 20, 10};
        double[] x = {1, 1, 1, 1, 1, 1, 1, 1};
        // double[] x = {100, 0, 0, 0, 0, 0, 0, 0};

        System.out.println(entropy(x));


    }

    public static double entropy(double[] x) {
        double tot = 0;
        // find the total of all the entries
        // by looping over each element of the vector and adding to the total
        for (int i = 0; i < x.length; i++) {
            tot += x[i];
        }
        // System.out.println("Tot = " + tot);

        // convert to probabilities by dividing
        // each element of the vector by the total
        // this will modify the contents of the
        // vector that was passed in
        // an alternative would be to do the division by the total on the fly in the next
        // loop and not modify the original vector at all
        for (int i = 0; i < x.length; i++) {
            x[i] /= tot;
        }

        // print out for a check
        System.out.println(Arrays.toString(x));
        // now sum, but remove non-zero entries

        // now we actually calculate the entropy
        // using the Shannon equation
        tot = 0;
        for (int i = 0; i < x.length; i++) {
            // only consider non-zero entries
            // to avoid NaN (Not a Number) errors
            if (x[i] != 0) {
                tot += x[i] * log2(x[i]);
             }
        }
        // return the negative of the total
        return -tot;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private static double maxEnt(int n) {
        double p = 1.0 / n;
        return -n * p * (Math.log(p) / Math.log(2));
    }

    private static double sumSeries(int n) {
        double tot = 1;
        for (int i=2; i<=n; i++) {
            tot += (1.0 / i);
        }
        return tot;
    }

}
