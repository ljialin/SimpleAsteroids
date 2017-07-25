package utilities;

import java.io.*;
import java.util.*;

/**
 * This class implements some simple statistical functions
 * on arrays of numbers, namely, the mean, variance, standard
 * deviation, covariance, min and max.
 */

public class Stats {


    /**
     * Calculates the square of a double.
     *
     * @return Returns x*x
     */

    public static double sqr(double x) {
        return x * x;
    }

    /**
     * Returns the average of an array of double.
     */

    public static double mean(double[] v) {
        double tot = 0.0;
        for (int i = 0; i < v.length; i++)
            tot += v[i];
        return tot / v.length;
    }


    /**
     * Returns the sample standard deviation of an array
     * of double.
     */

    public static double sdev(double[] v) {
        return Math.sqrt(variance(v));
    }

    /**
     * Returns the standard error of an array of double,
     * where this is defined as the standard deviation
     * of the sample divided by the square root of the
     * sample size.
     */

    public static double stderr(double[] v) {
        return sdev(v) / Math.sqrt(v.length);
    }

    /**
     * Returns the sample variance of the array of double.
     */

    public static double variance(double[] v) {
        double mu = mean(v);
        double sumsq = 0.0;
        for (int i = 0; i < v.length; i++)
            sumsq += sqr(mu - v[i]);
        return sumsq / (v.length - 1);
        // return 1.12; this was done to test a discrepancy with Business Statistics
    }

    /** this alternative version was used to check
     correctness
     */

    private static double variance2(double[] v) {
        double mu = mean(v);
        double sumsq = 0.0;
        for (int i = 0; i < v.length; i++)
            sumsq += sqr(v[i]);
        System.out.println(sumsq + " : " + mu);
        double diff = (sumsq - v.length * sqr(mu));
        System.out.println("Diff = " + diff);
        return diff / (v.length - 1);
    }

    /**
     * Returns the covariance of the paired arrays of
     * double.
     */

    public static double covar(double[] v1, double[] v2) {
        double m1 = mean(v1);
        double m2 = mean(v2);
        double sumsq = 0.0;
        for (int i = 0; i < v1.length; i++)
            sumsq += (m1 - v1[i]) * (m2 - v2[i]);
        return sumsq / (v1.length - 1);
    }

    public static double correlation(double[] v1, double[] v2) {
        // an inefficient implementation!!!
        return covar( v1 , v2 ) / ( sdev (v1) * sdev( v2 ) );
    }

    /**
     *
     * @param x - series 1
     * @param y - series 2
     * @return rSquared coefficient of determination between x and y
     *
     */
    public static double rSquared(double[] x, double[] y) {
        double r = correlation(x, y);
        return r * r;
    }

    public static double correlation2(double[] v1, double[] v2) {
        // used to cross-check the other implementation
        return sqr( covar( v1 , v2 ) / ( covar(v1, v1) * covar( v2, v2 ) ));
    }

    /**
     * Returns the maximum value in the array.
     */

    public static double max(double[] v) {
        double m = v[0];
        for (int i = 1; i < v.length; i++)
            m = Math.max(m, v[i]);
        return m;
    }

    /**
     * Returns the minimum value in the array.
     */

    public static double min(double[] v) {
        double m = v[0];
        for (int i = 1; i < v.length; i++)
            m = Math.min(m, v[i]);
        return m;
    }


    /**
     * Runs through some examples using the functions
     * defined in this class.
     *
     * @exception java.io.IOException
     */

    public static void main(String[] args) throws IOException {

        double[] d = new double[0];

        double dd = mean(d);

        System.out.println(dd + "\t" + Double.isNaN(dd));

        for (int i = 0; i < 3; i++) {
            double[] x = new double[i];
            System.out.println(mean(x) + "\t " + stderr(x) + "\t " + sdev(x));
        }
    }

}
