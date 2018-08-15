package cma.noisyopt;

public class DiscreteRangeSanityCheck {
    public static void main(String[] args) {
        int nValues = 3;
        for (double x = 0; x < 1.0; x+= 0.01) {
            int ix = (int) (x * nValues);
            System.out.println(ix + "\t " + x);
        }
    }
}
