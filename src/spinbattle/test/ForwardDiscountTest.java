package spinbattle.test;


    /*
    A simple test class to play with the effects of varying a discount factor
     */

public class ForwardDiscountTest {


    public static void main(String[] args) {
        int nSteps = 10;
        int score = 0;
        int scorePerStep = 10;
        double discount = 1.0;
        double discountFactor = 2;

        double tot = 0;
        double denom = 1.0;


        double value = 0;

        for (int i=0; i<nSteps; i++) {
            score += scorePerStep;
            value = score*score;
            discount *= discountFactor;
            tot += discount * value;
            denom += discount;

        }

        double discountedScore = tot / denom;
        System.out.println("Final value: " + value);
        System.out.println("Discounted value = " + discountedScore);

    }

}
