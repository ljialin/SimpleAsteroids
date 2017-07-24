package ntuple;

import evodef.SearchSpace;
import utilities.StatSummary;

/**
 * Created by simonmarklucas on 24/06/2017.
 */
public class GeneArrayMeanModel {

    int nGenes;
    GeneMeanModel[] geneModel;

    public GeneArrayMeanModel(SearchSpace searchSpace) {
        nGenes = searchSpace.nDims();
        geneModel = new GeneMeanModel[nGenes];
        for (int i=0; i<nGenes; i++) {
            geneModel[i] = new GeneMeanModel(searchSpace.nValues(i));
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

    static boolean verbose = false;

    public void resetStats() {
        for (GeneMeanModel gene : geneModel) {
            gene.resetStats();
        }
    }

    public void report() {
        for (int i=0; i<geneModel.length; i++) {
            System.out.println(i + "\t " + geneModel[i].toString());
        }
    }

    public void updateModelMean(ScoredVec sv) {
        for (int i=0; i<nGenes; i++) {
            geneModel[i].updateMean( sv.p[i], sv.score );
        }
    }
    public void removeVec(ScoredVec sv) {
        for (int i=0; i<nGenes; i++) {
            geneModel[i].remove( sv.p[i], sv.score );
        }
    }
}
