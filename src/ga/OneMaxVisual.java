package ga;

import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.Random;

public class OneMaxVisual {

    // Random mutation hill climber for testing one-max
    static Random random = new Random();
    static int maxEvals = 500;

    JEasyFrame frame;
    BitView view;

    int[] bestYet;

    public static void main(String[] args) {
        int dim = 100;
        new OneMaxVisual(dim).run(maxEvals);
    }

    public OneMaxVisual(int n) {
        this(randVec(n));
    }

    public OneMaxVisual(int[] bestYet) {
        this.bestYet = bestYet;
    }

    public Integer run(int nEvals) {
        view = new BitView(bestYet);
        frame = new JEasyFrame(view, "One Max Test");
        for (int i=1; i<=nEvals; i++) {
            // randomly mutate the best yet
            int[] mut = randMut(bestYet);
            view.v = mut;
            frame.setTitle(i + " : " + countOnes(mut));
            view.repaint();
            delay();
            // if it's better then adopt the mutation as the new best
            if (countOnes(mut) >= countOnes(bestYet)) {
                bestYet = mut;
            }
            // System.out.println(Arrays.toString(bestYet));
            if (countOnes(bestYet) == bestYet.length) {
                // return how many evals it took to reach perfection
                return i;
            }
        }
        // failed to find a solution
        return null;
    }

    public void delay() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
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
