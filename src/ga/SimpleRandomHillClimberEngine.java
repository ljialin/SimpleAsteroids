package ga;

import java.util.Arrays;
import java.util.Random;

public class SimpleRandomHillClimberEngine {

    // Random mutation hill climber
    // using a relative (hence co-evolutionary)
    // fitness function
    static Random random = new Random();

    double[] bestYet;
    Eval2 eval;

    double stepFac = 0.1;

    // set stepAdjust to 1.0 to keep the stepFac fixed

    // on quadratic bowl 1.01 works much better than 1.0
    // which increases the step size every time a mutation
    // is successful and decreases it every time it fails
    // to improve
    // but be careful: could it make things worse in some cases?
    double stepAdjust = 1.01;

    // example ready for a minimal version of
    // co-evolution

    public static void main(String[] args) {
        int nEvals = 1000;
        int nDim = 10;
        double[] init = randVec(nDim);
        SimpleRandomHillClimberEngine evo = new SimpleRandomHillClimberEngine(init, new QuadraticBowl());

        evo.run(nEvals);

        System.out.println("Best Found: " + Arrays.toString(evo.bestYet));
        System.out.println("Fitness: " + mag2(evo.bestYet));

    }

    public SimpleRandomHillClimberEngine(double[] bestYet, Eval2 eval) {
        this.bestYet = bestYet;
        this.eval = eval;
    }

    public double[] run(int nEvals) {
        for (int i=0; i<nEvals; i++) {
            // randomly mutate the best yet
            double[] mut = randMut(bestYet, stepFac);
            double diff = eval.pointsDiff(bestYet, mut);

            // if it's better then adopt the mutation as the new best
            if (diff >= 0) {
                bestYet = mut;
                // try making it bigger - make even faster progress
            } else {
                // try making the step size smaller
                stepFac /= stepAdjust;
            }
            System.out.println(i + "\t " + mag2(bestYet));
        }

        return bestYet;
    }

    static double[] randVec(int n) {
        double[] x = new double[n];
        for (int i=0; i<n; i++) {
            x[i] = random.nextGaussian();
        }
        return x;
    }

    static double[] randMut(double[] v, double stepFac) {
        int n = v.length;
        double[] x = new double[n];
        for (int i=0; i<n; i++) {
            x[i] = v[i] + stepFac * random.nextGaussian();
        }
        return x;
    }

    static class QuadraticBowl implements Eval2 {

        @Override
        public double pointsDiff(double[] a, double[] b) {
            // simple example that evaluates quality as being
            // the minimum squared magnitude of a vector
            return mag2(a) - mag2(b);
        }
    }

    static double mag2(double[] v) {
        // square of the magnitude of the vector
        double tot = 0;
        for (double x : v) tot += x * x;
        return tot;
    }
}
