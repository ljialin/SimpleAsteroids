package paired;

import evodef.NoisySolutionEvaluator;
import ntuple.ScoredVec;

import java.util.List;

/**
 * Created by sml on 30/06/2017.
 *
 * Yes, this is just supervised learning but for some very specific experiments
 * to compare paired difference learning versus learning mean values
 *
 */
public interface ScoredVectorLearner {

    double[] getFitness();

    int[] learn(List<ScoredVec> scoredVecs, NoisySolutionEvaluator evaluator);

}
