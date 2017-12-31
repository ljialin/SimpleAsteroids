package bandits;

import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 *
 *
 *  Idea is to keep track of which changes lead to an improvement
 *
 */
public class BanditGene {

    static Random random = new Random();

    // do binary version to begin with
    int nArms = 2;

    // double[] rewards = new double[nArms];
    double[] deltaRewards = new double[nArms];

    int x;

    // start all at one to avoid div zero
    int nMutations = 1;
    static double k = 10000;

    Integer xPrevious;


    public BanditGene() {
        randomize();
    }

    public void randomize() {
        // x = 0 ; // random.nextInt(nArms);
        x = random.nextInt(nArms);
    }

    public void mutate() {
        // for now cheat and assume binary
        xPrevious = x;
        x = 1 - x;
        nMutations++;
    }

    public double maxDelta() {
        StatSummary ss = new StatSummary();
        for (double d : deltaRewards) ss.add(d);
        return ss.max();
    }

    public double urgency(int nEvals) {
        return rescue() + explore(nEvals);
    }


    // in bandit terms this would normally be called the exploit term
    // but in an EA we need to use it in the opposite sense
    // since we need to stick with values that are already thought to be
    // good and instead modify ones that need to be rescued
    public double rescue() {
        return -maxDelta() / nMutations;
    }

    // standard UCB Explore term
    // consider modifying a value that's not been changed much yet
    public double explore(int nEvals) {
        return k * Math.sqrt(Math.log(nEvals + 1) / (nMutations));
    }


    public void applyReward(double delta) {
        deltaRewards[x] += delta;
        deltaRewards[xPrevious] -= delta;
    }

    public void revertOrKeep(double delta) {
        if (delta < 0) {
            x = xPrevious;
        }
    }

    public String statusString(int nEvals) {
        return String.format("%d\t rescue: %.2f\t explore: %.2f\t urgency: %.2f",
                x, rescue(), explore(nEvals), urgency(nEvals));
    }
}
