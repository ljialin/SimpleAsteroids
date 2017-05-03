package ga;

import evodef.SolutionEvaluator;
import evomaze.MazeView;
import evomaze.ShortestPathTest;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

public class RMHCMazeTest {

    // Random mutation hill climber for testing one-max
    static Random random = new Random();

    static int maxEvals = 20000;

    int[] bestYet;

    SolutionEvaluator evaluator = new ShortestPathTest();

    public static void main(String[] args) {
        int dim = 225;
        int nReps = 30;
        StatSummary ss = new StatSummary();
        ElapsedTimer t = new ElapsedTimer();
        for (int i=0; i<nReps; i++) {
            double score = runTest(dim);
            System.out.println(i + "\t " + score);
            ss.add(score);
        }
        // BarChart.display(means, "OneMax Scaling");
        System.out.println(t);
        System.out.println(ss);
    }

    public static double runTest(int n) {
        return new RMHCMazeTest(n).run(maxEvals);
    }

    static double predictNEvals(int n) {

        // the exercise is to work out a proper calculation of the predicted hitting time
        // as a function of n

        // this is just a placeholder
         return n;
    }


    public RMHCMazeTest(int n) {
        this(randVec(n));
    }

    public RMHCMazeTest(int[] bestYet) {
        this.bestYet = bestYet;
    }

    public double run(int nEvals) {
        for (int i=0; i<=nEvals; i+=2) {
            // randomly mutate the best yet
            // int[] mut = randMut(bestYet);
            // int[] mut = randMutAll(bestYet);
            int[] mut = randAll(bestYet);
            if (eval(mut) >= eval(bestYet)) {
                bestYet = mut;
            }
            double score = eval(bestYet);
            // System.out.println(i + " : " + score);
            // System.out.println(Arrays.toString(bestYet));
            if ( score == bestYet.length ) {
                // return how many evals it took to reach perfection
                // multiply by 2 to because we evaluated
                // both the bestYet and the mutated copy each time
                return eval(bestYet);
            }
        }
        // failed to find a solution

        String title = "RMHC: " + eval(bestYet);
        MazeView.showMaze(bestYet, title);

        return eval(bestYet);
    }

    public double eval(int[] bits) {
        if (evaluator != null) return evaluator.evaluate(bits);
        else return countOnes(bits);
    }

    public int countOnes(int[] x) {
        int tot = 0;
        for (int i : x) tot += i;
        return tot;
    }

    static int[] randVec(int n) {
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            // x[i] = random.nextInt(2);
            x[i] = 0; // random.nextInt(2);
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
        double mutProb = 2.0 / v.length;
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            x[i] = random.nextDouble() < mutProb ? 1 - v[i] : v[i];
        }
        return x;
    }
    static int[] randAll(int[] v) {
        int n = v.length;
        int[] x = new int[n];
        for (int i=0; i<n; i++) {
            x[i] = random.nextInt(2); // random.nextDouble() < mutProb ? 1 - v[i] : v[i];
        }
        return x;
    }
}
