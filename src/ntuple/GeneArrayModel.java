package ntuple;

import evodef.SearchSpace;

/**
 * Created by simonmarklucas on 24/06/2017.
 */
public class GeneArrayModel {

    int nGenes;
    GenePairedModel[] geneModel;

    public GeneArrayModel(SearchSpace searchSpace) {
        nGenes = searchSpace.nDims();
        geneModel = new GenePairedModel[nGenes];
        for (int i=0; i<nGenes; i++) {
            geneModel[i] = new GenePairedModel(searchSpace.nValues(i));
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
    static boolean weightDifferences = true;

    public void updateModel(ScoredVec winner, ScoredVec loser) {

        // weight the difference
        int nDifferent = 0;
        for (int i=0; i<nGenes; i++) {
            if (winner.p[i] != loser.p[i])
                nDifferent++;
        }
        if (nDifferent == 0) {
            if (verbose) System.out.println("No differences between winner and loser");
            // no work to do here
            return;
        } else {

        }

        // distribute the credit for the differences to the different parts

        double weight = nGenes;
        if (weightDifferences) weight /= nDifferent;

        if (verbose) {
            System.out.println(nDifferent + " : " + weight);
        }

        for (int i=0; i<nGenes; i++) {
            if (winner.p[i] != loser.p[i]) {
                geneModel[i].update(winner.p[i], loser.p[i], weight);
            }
        }
    }

    public void resetStats() {
        for (GenePairedModel gene : geneModel) {
            gene.resetStats();
        }
    }

    public void report() {
        for (int i=0; i<geneModel.length; i++) {
            System.out.println(i + "\t " + geneModel[i].toString());
        }
    }

    public void updateModelDiff(ScoredVec svi, ScoredVec svj) {
        for (int i=0; i<nGenes; i++) {
            if (svi.p[i] != svj.p[i]) {
                geneModel[i].updateDiff(svi.p[i], svj.p[i], svi.score - svj.score);
            }
        }
    }

    public void updateModelMean(ScoredVec sv) {
        for (int i=0; i<nGenes; i++) {
            geneModel[i].updateMean( sv.p[i], sv.score );
        }
    }
}
