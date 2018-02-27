package ntuple.tests;

public class KLDivergenceTest {

    public static void main(String[] args) {

        KLDivergenceTest div = new KLDivergenceTest();
        double p = 0.1;
        double q = 1.0;
        double fac = 0.5;
        for (int i=0; i<10; i++) {
            double d = div.div(p, q);
            // System.out.format("%.4f\t %.4f\t %.4f\n", p, q, d);
            q *= fac;
        }

        double[] pa = {0.0, 0.29, 0.71};
        double[] qa = {0.1, 0.30, 0.60};

        double pq = div(pa, qa);
        double qp = div(qa, pa);
        System.out.println();

        System.out.println(pq);
        System.out.println(qp);
    }

    static double div(double[] p, double[] q) {
        double tot = 0;
        double totp = 0;
        double totq = 0;
        for (int i=0; i<p.length; i++) {
            tot += div(p[i], q[i]);
            totp += p[i];
            totq += q[i];
        }
        // System.out.println("Check totals: " + totp + " : " + totq);
        return tot;
    }

    static double epsilon = 1e-20;

    static double div (double p, double q) {
        double kl = p * Math.log((p+epsilon)/(q + epsilon));
        return kl;
    }
}
