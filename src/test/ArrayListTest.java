package test;

import java.util.ArrayList;

/**
 * Created by sml on 07/06/2015.
 */
public class ArrayListTest {
    public static void main(String[] args) {
        ArrayList<Integer> al = new ArrayList<>();
        for (int i=0; i<10; i++) al.add(i);

        // now break it:

        for (Integer x : al) {

            System.out.println(x);

            // This throws a concurrent modification exception
            // if (Math.random() < 0.5) al.add(x);

        }

        // but this works fine:

        int size = al.size();

        for (int i=0; i<size; i++) {
            Integer x = al.get(i);
            System.out.println(x);
            if (Math.random() < 0.5) al.add(x);
        }

        for (Integer x : al) {

            System.out.println(x);

            // This throws a concurrent modification exception
            // if (Math.random() < 0.5) al.add(x);

        }
    }
}
