package test;

public class NaalCompundTest {
    public static void main(String[] args) {
        double tot = 0;
        double rate = 0.97;
        double init = 3;
        for (int i=0; i<1000; i++) {
            tot += init;
            init *= rate;
            System.out.format("%d\t %.2f\n", i, tot);
        }
    }
}
