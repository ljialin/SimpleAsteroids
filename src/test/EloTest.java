package test;

/**
 * Created by simonmarklucas on 16/05/2016.
 */
public class EloTest {

    // equations from WikiPedia article:
    // https://en.wikipedia.org/wiki/Elo_rating_system#Mathematical_details

    // although the standard Elo model assumes a Normal distribution,
    // the code below is based on the more frequently used Logistic distribution
    // the only difference this makes is in the implementation of the
    // pWin method below


    // this constant K controls how rapidly
    // a player's rating changes in the face of new results


    // Exercise: model three agents with an intransitivity cycle
    // e.g. P(A beats B) = 0.9, P(B beats C) = 0.9 and P(C beats A) = 0.9
    // give them an initial different spread of Elo ratings and
    // see what values the Elo ratings coverge to over time
    // by extended example below which only models two players A and B
    // and only updates the Elo rating of player A


    static double K = 30;

    public static void main(String[] args) {

        // this is the score from player A's viewpoint
        // so a score of zero means player A lost

        // re-run the program with values of 0, 0.5 and 1.0
        double score = 1.0;

        double rA = 1600;

        // see how the predicted win probability and the next rating changes
        // as a function of the rating of player B
        for (double rB = 1000; rB<=2000; rB+=100) {

            // this line calculates the probability that player A will beat player B
            System.out.println(rB + "\t pWin =  " + pWin(rA, rB));

            // this line is the new Elo rating for player A given the current
            // ratings of A and B, and the score of this game
            System.out.println(rB + "\t eloA = " + update(rA, rB, score));
            System.out.println();

        }

        // now see how quickly the value of B changes when it
        // B beats A with a probability of 0.9
        double rB = 1000;

        // question: what value should rB converge to?
        for (int i=0; i<200; i++) {
            score = Math.random() < 0.9 ? 1 : 0;
            System.out.println("Winner is: " + (score == 1 ? "B" : "A"));
            rB = update(rB, rA, score);
            System.out.println(i + "\t " + rB);
            System.out.println();
        }
    }

    public static double update(double rA, double rB, double score) {
        return rA + K * (score - pWin(rA, rB));
    }

    public static double pWin(double rA, double rB) {
        return 1.0 / (1.0 + Math.pow(10, (rB - rA) / 400));
    }
}

