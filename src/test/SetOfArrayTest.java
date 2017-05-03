package test;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sml on 09/01/2017.
 */
public class SetOfArrayTest {

    // simply demonstrates that both a and b get stored in
    // the HashSet, despite them having identical values;
    // in other words, only the reference is checked,
    // so c does not get stored

    public static void main(String[] args) {

        int[] a = new int[]{1, 2};
        int[] b = new int[]{1, 2};
        int[] c = a;

        Set<int[]> set = new HashSet<>();

        set.add(a);
        set.add(b);
        set.add(c);

        System.out.println(set);



    }
}
