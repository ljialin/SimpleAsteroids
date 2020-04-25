package test;

import java.util.Arrays;

public class HelloWorld {
    public static void main(String[] args) {
//        System.out.println("Hello Alex: " + args.length);
//        System.out.println("args: " + Arrays.toString(args));

        int foo = -61;

        for (String s : args) {
            System.out.println( s + " ");
            foo += s.length() * s.length();

        }

        System.out.println();
        System.out.println(foo);

    }
}

