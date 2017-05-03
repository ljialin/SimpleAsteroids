package test;

import com.google.gson.Gson;
import utilities.ElapsedTimer;

import java.util.Random;

/**
 * Created by sml on 17/11/2016.
 */
public class GsonTest {

    float[] a;
    int n;

    public GsonTest(int n) {
        this.n = n;
        Random rand = new Random();
        a = new float[n];
        for (int i=0; i<n; i++) {
            a[i] = rand.nextFloat();
        }
    }

    public static void main(String[] args) {
        GsonTest gt = new GsonTest(10000);
        String out = new Gson().toJson(gt);

        ElapsedTimer t = new ElapsedTimer();
        out = new Gson().toJson(gt);
        System.out.println("Length of: " + out.length());
        System.out.println(t);
        String out2 = getString(gt.a);
        System.out.println("Length of: " + out2.length());
        System.out.println(t);
    }

    static String getString(float[] a) {
        StringBuilder sb = new StringBuilder();
        for (float f : a) sb.append(f + " ");
        return sb.toString();
    }


}
