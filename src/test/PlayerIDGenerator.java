package test;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by sml on 25/05/2016.
 */
public class PlayerIDGenerator {
    public static void main(String[] args) {
        int n = 50;
        Random rand = new Random();

        int start = 50000;
        int range = 10000;

        TreeSet<Integer> check = new TreeSet<>();

        for (int i=0; i<n; i++) {
            int num = rand.nextInt(range);

            if (!check.contains(num)) {
                check.add(num);
                System.out.println(start + num);
            } else {
                System.out.println("Collision: " + num);
            }
        }
    }
}
