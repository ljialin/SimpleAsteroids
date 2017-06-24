package ntuple;

import evodef.SearchSpace;

/**
 * Created by simonmarklucas on 24/06/2017.
 */
public class GeneArrayModel {

    int nGenes;
    GeneModel[] geneModel;

    public GeneArrayModel(SearchSpace searchSpace) {
        nGenes = searchSpace.nDims();
        geneModel = new GeneModel[nGenes];
        for (int i=0; i<nGenes; i++) {
            geneModel[i] = new GeneModel(searchSpace.nValues(i));
        }
    }

    public int[] generate() {
        int[] p = new int[nGenes];
        for (int i=0; i<nGenes; i++) {
            p[i] = geneModel[i].generate();
        }
        return p;
    }

    public int[] argMax() {
        int[] p = new int[nGenes];
        for (int i=0; i<nGenes; i++) {
            p[i] = geneModel[i].argmax();
        }
        return p;
    }

    public void updateModel(ScoredVec winner, ScoredVec loser) {
        for (int i=0; i<nGenes; i++) {
            if (winner.p[i] != loser.p[i]) {
                geneModel[i].update(winner.p[i], loser.p[i]);
            }
        }
    }
}
