package test;

/**
 * Created by sml on 15/05/2016.
 */
public class Temp {

    static double predictNEvals(int n) {

        double tot = 0;
        for (int i=1; i<=n/2; i++) {
            tot += 1.0 / i;
        }
        return n * tot;

    }

}
