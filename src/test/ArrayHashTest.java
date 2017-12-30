package test;

import java.util.Arrays;

public class ArrayHashTest {
    public static void main(String[] args) {
        // these have different hashcodes
        int[] a1 = new int[]{1, 2, 3, 4};
        int[] a2 = new int[]{1, 2, 3, 4};

        System.out.println(a1.hashCode() + " : " + a2.hashCode());

        // however, we can convert to Strings, at which point their hashcodes
        // will be the same

        String s1 = Arrays.toString(a1);
        String s2 = Arrays.toString(a2);
        System.out.println(s1.hashCode() + " : " + s2.hashCode());
    }
}
