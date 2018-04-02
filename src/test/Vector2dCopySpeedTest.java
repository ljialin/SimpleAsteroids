package test;

import math.Vector2d;
import utilities.ElapsedTimer;

public class Vector2dCopySpeedTest {

    // copies an array of 1000 vectors 1000 times in around 50ms
    // on an average Macbook

    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        int nTrials = 1000;
        int nDim = 1000;

        Vector2d[] a = new Vector2d[nDim];
        for (int i=0; i<nDim; i++) a[i] = new Vector2d(Math.random(), Math.random());

        for (int i=0; i<nTrials; i++) {
            // Vector2d[] x = a.clone();
            Vector2d[] x = arrayCopy(a);
            // double[] x = copyArray(a);
            // if (tot(x) != tot(a)) System.out.println("Error");
        }

        System.out.println(t);
    }

    static Vector2d[] arrayCopy(Vector2d[] a) {
        Vector2d[] b = new Vector2d[a.length];
        for (int i=0; i<a.length; i++)
            b[i] = a[i].copy();
        return b;
    }


}
