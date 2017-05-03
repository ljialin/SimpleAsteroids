package test;

/**
 * Created by simonmarklucas on 02/06/2016.
 */
public class OptionalityTest {

    public static void main(String[] args) {
        System.out.println(nChooseK(15, 5));

        System.out.println(optionSlot(10, 5));
    }

    static double nChooseK(int n, int k) {
        double top = 1;
        for (int i=n; i > n-k; i--) {
            top *= i;
        }
        double bot = 1;
        for (int i=2; i<=k; i++) {
            bot *= i;
        }
        System.out.println(top + " : "+ bot);
        return top / bot;
    }

    static double fac(int n) {
        double f = 1;
        for (int i=2; i<=n; i++) {
            f *= i;
        }
        return f;
    }

    static double optionSlot(int nOptions, int nSlots) {
        return Math.pow(nOptions, nSlots) / fac(nSlots);
    }

    // static

}
