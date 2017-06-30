package paired;

import evodef.NoisySolutionEvaluator;
import ntuple.GeneArrayModel;
import ntuple.ScoredVec;
import utilities.Picker;
import utilities.StatSummary;

import java.util.List;
import java.util.Random;

/**
 * Created by sml on 30/06/2017.
 */
public class PairedDifferenceLearner implements ScoredVectorLearner {

    double[] fitness;

    @Override
    public double[] getFitness() {
        return fitness;
    }

    @Override
    public int[] learn(List<ScoredVec> scoredVecs, NoisySolutionEvaluator evaluator) {
        int n = evaluator.searchSpace().nDims();
        fitness = new double[scoredVecs.size()];

        GeneArrayModel geneArrayModel = new GeneArrayModel(evaluator.searchSpace());

        for (int i=0; i<scoredVecs.size(); i++) {
            for (int j=i+1; j<scoredVecs.size(); j++) {
                ScoredVec svi = scoredVecs.get(i);
                ScoredVec svj = scoredVecs.get(j);
                geneArrayModel.updateModelDiff(svi, svj);

//                if (svi.score > svj.score) {
//                    geneArrayModel.updateModel(svi, svj);
//                } else {
//                    geneArrayModel.updateModel(svj, svi);
//                }

            }
            fitness[i] = evaluator.trueFitness(geneArrayModel.argMax());
        }
        // geneArrayModel.report();
        return geneArrayModel.argMax();
    }
}
