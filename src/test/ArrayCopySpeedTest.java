package test;

import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;

public class ArrayCopySpeedTest {
    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        int nTrials = 1000;
        int nDim = 1000;

        double[] a = new double[nDim];
        for (int i=0; i<nDim; i++) a[i] = Math.random();

        for (int i=0; i<nTrials; i++) {
            double[] x = a.clone();
            // double[] x = copyArray(a);
            // if (tot(x) != tot(a)) System.out.println("Error");
        }

        System.out.println(t);
    }

    static double tot(double[] a) {
        double tot = 0;
        for (double x : a) tot += x;
        return tot;
    }

    static double[] copyArray(double[] a) {
        double[] b = new double[a.length];
        for (int i=0; i<a.length; i++)
            b[i] = a[i];
        return b;
    }
}
