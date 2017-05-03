package ga;

import evodef.SolutionEvaluator;
import utilities.BarChart;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

public class OneMaxTest {

    // Random mutation hill climber for testing one-max
    static Random random = new Random();

    static int nVirtualSamples = 1;

    static double noiseStdDev = 0; // 0.78 / Math.sqrt(nVirtualSamples);
    static int maxEvals = 200000;

    int[] bestYet;

    SolutionEvaluator evaluator = null;


    public static void main(String[] args) {
        int minDim = 10;
        int maxDim = 10;
        int step = 10;
        int nReps = 100;
        ElapsedTimer t = new ElapsedTimer();
        ArrayList<Double> means = new ArrayList<>();
        System.out.println("N Samples per Fitness Eval = " + nVirtualSamples);
        for (int i=minDim; i<=maxDim; i+=step) {
            // see how well actual evals matches prediction
            StatSummary ss = new StatSummary();
            for (int j=0; j<nReps; j++) {
                Integer nEvals = runTest(i);
                if (nEvals != null) ss.add(nEvals * nVirtualSamples);

            }
            System.out.println(ss);
            means.add(ss.mean());
            System.out.println(i + "\t " + ss.mean() + "\t " + predictNEvals(i));
        }
        System.out.println(t);
        BarChart.display(means, "OneMax Scaling");
    }

    public static Integer runTest(int n) {
        return new OneMaxTest(n).run(maxEvals);
    }

    static double predictNEvals(int n) {

        // the exercise is to work out a proper calculation of the predicted hitting time
        // as a function of n

        // this is just a placeholder
         return n;
    }


    public OneMaxTest(int n) {
        this(randVec(n));
    }

    public OneMaxTest(int[] bestYet) {
        this.bestYet = bestYet;
    }

    public Integer run(int nEvals) {
        for (int i=0; i<=nEvals; i+=2) {
            // randomly mutate the best yet
            int[] mut = randMut(bestYet);
            double noise = random.nextGaussian() * noiseStdDev;
            // if it's better then adopt the mutation as the new best
            // add in the noise to the decision
            if (countOnes(mut) >= countOnes(bestYet) + noise) {
                bestYet = mut;
            }
            // System.out.println(Arrays.toString(bestYet));
            if (countOnes(bestYet) == bestYet.length) {
                // return how many evals it took to reach perfection
                // multiply by 2 to because we evaluated
                // both the bestYet and the mutated copy each time
                return i;
            }
        }
        // failed to find a solution
        return null;
    }

    public int countOnes(int[] x) {
        int tot = 0;
        for (int i : x) tot += i;
        return tot;
    }

    static int[] randVec(int n) {
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            x[i] = random.nextInt(2);
        }
        return x;
    }

    static int[] randMut(int[] v) {
        int n = v.length;
        int[] x = new int[n];
        int ix = random.nextInt(n);
        for (int i=0; i<n; i++) {
            x[i] = i == ix ? 1 - v[i] : v[i];
        }
        return x;
    }
    static int[] randMutAll(int[] v) {
        int n = v.length;
        double mutProb = 1.0 / v.length;
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            x[i] = random.nextDouble() < mutProb ? 1 - v[i] : v[i];
        }
        return x;
    }
}
