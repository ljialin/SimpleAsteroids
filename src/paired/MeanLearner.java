package paired;

import evodef.NoisySolutionEvaluator;
import ntuple.GeneMeanModel;
import ntuple.ScoredVec;
import utilities.Picker;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by sml on 30/06/2017.
 */

public class MeanLearner implements ScoredVectorLearner {

    double[] fitness;

    @Override
    public double[] getFitness() {
        return fitness;
    }

    @Override
    public int[] learn(List<ScoredVec> scoredVecs, NoisySolutionEvaluator evaluator) {
        int n = evaluator.searchSpace().nDims();
        fitness = new double[scoredVecs.size()];

        // set up the models
        GeneMeanModel[] models = new GeneMeanModel[n];
        for (int i=0; i<n; i++)
            models[i] = new GeneMeanModel(evaluator.searchSpace().nValues(i));

        // now train the,

        int ix = 0;

        for (ScoredVec sv : scoredVecs) {
            // update it here
            for (int i=0; i<n; i++) {
                models[i].updateMean(sv.p[i], sv.score);
            }
            int[] p = argmax(models);
            // System.out.println(Arrays.toString(p));
            fitness[ix++] = evaluator.trueFitness(p);
        }
        // now return the argmax array
        return argmax(models);
    }

    static int[] argmax(GeneMeanModel[] models) {
        int[] argMax = new int[models.length];
        for (int i=0; i<models.length; i++) {
            argMax[i] = models[i].argmax();
            // System.out.println("GridModel: " + i + " : " + models[i].toString() );

        }
        return argMax;
    }

}
