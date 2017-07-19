package ntuple;

import java.util.Random;

/**
 *  Created by simonmarklucas on 18/07/2017.
 *
 *  The purpose of this class is to estimate the posterior probability of
 *  a particular bit value (zero or one) leading to a win, based on the date.
 *
 *  By using Bayes' theorem (hence inverse probabilities)
 *  we avoid the problem of over-confident values that
 *  would arise from using Maximum Likelihood Estimation.
 *
 *
 *
 */

public class SlidingBayes {

    public static void main(String[] args) {
        SlidingBayes sb = new SlidingBayes();
        for (int i=0; i<100; i++) {
            System.out.println(i + "\t " + sb.pOne());
            sb.add(0);
        }
        for (int i=0; i<200; i++) {
            System.out.println(i + "\t " + sb.pOne());
            sb.add(1);
        }
    }

    int[] nWins;
    double tot;


    static Random random = new Random();
    // for now
    int mValues = 2;
    public SlidingBayes() {
        nWins = new int[mValues];
    }

    public String toString() {
        return nWins[0] + "\t" + nWins[1];
    }

    public double pOne() {

        // double winRate = nWins[1] / tot;
        return (0.5 + nWins[1]) / (tot + 1);
    }

    public void add(int winner) {
        nWins[winner] ++;
        tot++;
    }

    public void remove(int winner) {
        nWins[winner]--;
        tot--;
    }

    public int argmax() {
        if (nWins[0] == nWins[1])
            return Math.random() < 0.5 ? 0 : 1;

        return nWins[0] > nWins[1] ? 0 : 1;
    }



    // public void remove(int)

}
