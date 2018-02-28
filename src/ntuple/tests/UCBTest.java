package ntuple.tests;

public class UCBTest {

    // purpose of this class is to print out the unweighted exploration values
    // based on N and ni.

    // this is to show what difference in the mean would be needed in order to flip
    // whether to exploit or explore

    public static void main(String[] args) {
        UCBTest ucb = new UCBTest();

        // for (int i=0; i<)


        for (int N=1; N<100; N++) {
            for (int ni = 0; ni<N; ni++) {
                System.out.format("%d\t %d\t %.3f\n", ni, N, ucb.explore(ni, N));
            }
            System.out.println();
        }

    }

    double epsilon = 1e-6;
    double k = 1;

    double explore(double ni, double N) {
        return Math.sqrt(Math.log((N+epsilon)) / (ni + epsilon));
    }

    double ucb(double mean, double ni, double N) {
        return mean + k * explore(ni, N);
    }

}
