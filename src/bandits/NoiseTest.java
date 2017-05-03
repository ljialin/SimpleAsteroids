package bandits;

import java.util.Random;

/**
 * Created by simonmarklucas on 30/05/2016.
 */
public class NoiseTest {
    public static void main(String[] args) {
        Random rand = new Random();

        int nTrials = 10000;
        int totalErrors = 0;
        double noiseStdDev = 0.78;

        for (int i=0; i<nTrials; i++) {
            // every time a mutation is made in OneMax
            // either a zero is flipped to a one
            // which changes the fitness by +1
            // or a one is flipped to a zero
            // which changes the fitness by -1

            // there are two effects that we can model
            // one is the effect of noise affecting the
            // delta fitness values
            //
            // the other is the the noise making a wrong decision
            // to incorrectly accept or reject a mutation
            // i.e. accept a change for the worse
            // or reject a change for the better

            // for now we tune the noise level
            // to create a preferred wrong decision rate

            // each time we generate the true fitness change
            // then add noise to it
            // an error occurs if the signs of the two are different

            double fitnessDelta = 2 * rand.nextInt(2) - 1;
            double noise = noiseStdDev * rand.nextGaussian();
            double noisyFitnessDelta = fitnessDelta + noise;

            int error = fitnessDelta * noisyFitnessDelta < 0 ? 1 : 0;

            // System.out.format("%d\t %+.2f\t %+.2f\t %d\n", i, fitnessDelta, noisyFitnessDelta, error);

            totalErrors += error;

        }

        System.out.println();
        System.out.println("Total errors = " + totalErrors);

    }
}
