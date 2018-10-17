package ga;

import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.Random;

public class OneMaxVisual {

    // Random mutation hill climber for testing one-max
    static Random random = new Random();
    static int maxEvals = 50;

    JEasyFrame frame;
    BitView view;

    int[] bestYet;

    public static void main(String[] args) {
        int dim = 25;
        double noise = 0;
        if (args.length > 0) {
            try {
                noise = Double.parseDouble(args[0]);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        new OneMaxVisual(dim).setNoise(noise).run(maxEvals);
    }

    public OneMaxVisual(int n) {
        this(randVec(n));
    }

    public OneMaxVisual setNoise(double noise) {
        this.noise = noise;
        return this;
    }

    public OneMaxVisual(int[] bestYet) {
        this.bestYet = bestYet;
    }

    public Integer run(int nEvals) {
        view = new BitView(bestYet);
        frame = new JEasyFrame(view, "One Max GVGAISimpleTest");
        for (int i=1; i<=nEvals; i++) {
            // randomly mutate the best yet
            int[] mut = randMut(bestYet);
            view.v = mut;
            int test = countOnes(mut);
            // frame.setTitle(i + " : " + countOnes(mut));
            view.repaint();
            delay();
            // if it's better then adopt the mutation as the new best
            if (countOnes(mut) + noise * random.nextGaussian() >= countOnes(bestYet) + noise * random.nextGaussian()) {
                bestYet = mut;
            }
            frame.setTitle(i + " : " + countOnes(bestYet) + " : " + test);
            // System.out.println(Arrays.toString(bestYet));
            if (countOnes(bestYet) == bestYet.length) {
                // return how many evals it took to reach perfection
                return i;
            }
            System.out.println(Arrays.toString(bestYet));
        }
        // failed to find a solution
        return null;
    }

    public void delay() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {}
    }


    double noise = 1.0;

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
