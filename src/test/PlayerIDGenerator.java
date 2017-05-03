package test;

import java.util.Random;

/**
 * Created by sml on 25/05/2016.
 */
public class PlayerIDGenerator {
    public static void main(String[] args) {
        int n = 50;
        Random rand = new Random();

        int start = 30000;

        for (int i=0; i<n; i++) {
            System.out.println(start + rand.nextInt(10000));
        }

    }
}
