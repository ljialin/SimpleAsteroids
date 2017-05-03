package test;

import utilities.Picker;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sml on 09/01/2017.
 */
public class PickerWithArraysTest {

    // simply demonstrates that both a and b get stored in
    // the HashSet, despite them having identical values;
    // in other words, only the reference is checked,
    // so c does not get stored

    public static void main(String[] args) {

        int[] a = new int[]{1, 2};
        int[] b = new int[]{1, 2};
        int[] c = a;

        Picker<int[]> picker = new Picker<>();
        picker.add(1.0, a);
        picker.add(2.0, c);

        System.out.println(picker.getBest());

        System.out.println(picker.nItems);



    }
}
