package test;

import utilities.StatSummary;

public class SumSeriesTest {
    public static void main(String[] args) {
        int n = 1000;

        StatSummary stats = new StatSummary("Totals");
        int tot = 0;
        for (int i=1; i<=n; i++) {
            int x = odd(i) ? - i : i;
            tot += x;
            System.out.println(i + "\t" + x + "\t" + tot);
            stats.add(tot);
        }

        System.out.println(stats);
    }

    public static boolean odd(int x) { return x % 2 == 1; }
}
