package ntuple;

import evodef.EvalMaxM;
import evodef.SearchSpace;
import evodef.SearchSpaceUtil;
import evogame.Mutator;
import utilities.Picker;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by simonmarklucas on 29/06/2017.
 */
public class PowerOfDifferencePairsTest {

    // okay this is interesting: the paired idea does not work well when the
    // vectors are far apart

    // we already knew this


    public static void main(String[] args) {

        // create a list to store the scored vectors in
        List<ScoredVec> scoredVecs = new ArrayList<>();

        // create the random vectors, score them
        // and put them in a list


        // make k random vectors
        int k = 100;

        // each n-dimensional
        int n = 100;

        // each dimension having m possible values
        // popular value for m is 2
        int m = 2;

        // when evaluation add gaussian noise with this standard deviation
        double noise = 1.0;

        EvalMaxM  searchSpace = new EvalMaxM(n, m, noise);

        // now create and score the vectors and add them do the list

        int[] px = SearchSpaceUtil.randomPoint(searchSpace);
        Mutator mutator = new Mutator(searchSpace);
        mutator.pointProb = 0.0; // 1.0; // 3 / n;
        // mutator
        Mutator.flipAtLeastOneValue = true;
        Mutator.totalRandomChaosMutation = false;

        for (int i=0; i<k; i++) {
            // int[] p = SearchSpaceUtil.randomPoint(searchSpace);
            int[] p = mutator.randMut(px);
            double score = searchSpace.evaluate(p);
            scoredVecs.add(new ScoredVec(p, score));
            System.out.println(Arrays.toString(p) + " : " + String.format("%.2f\t %.0f", score, searchSpace.trueFitness(p)));
        }

        // now run an experiment each way to determine the arg max
        // and then evaluate the quality of that

        int[] p1 = argMaxPairedModel(scoredVecs, searchSpace);

        System.out.println(Arrays.toString(p1));

        System.out.println("True fitness is: " + searchSpace.trueFitness(p1));

        int[] p2 = argMaxMeanModel(scoredVecs, searchSpace);

        System.out.println(Arrays.toString(p2));

        System.out.println("True fitness is: " + searchSpace.trueFitness(p2));



    }

    static int[] argMaxPairedModel(List<ScoredVec> scoredVecs, SearchSpace searchSpace) {

        GeneArrayModel geneArrayModel = new GeneArrayModel(searchSpace);

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
        }
        geneArrayModel.report();
        return geneArrayModel.argMax();
    }

    static double epsilon = 1e-20;
    static Random random = new Random();

    static int[] argMaxMeanModel(List<ScoredVec> scoredVecs, SearchSpace searchSpace) {
        // for now we're just going to assume a binary vector
        int n = searchSpace.nDims();
        StatSummary[][] means = new StatSummary[n][];
        for (int i=0; i<means.length; i++) {
            means[i] = new StatSummary[searchSpace.nValues(i)];
            for (int j=0; j<means[i].length; j++) {
                means[i][j] = new StatSummary();
            }
        }
        for (ScoredVec sv : scoredVecs) {
            // update it here

            for (int i=0; i<n; i++) {
                means[i][sv.p[i]].add(sv.score);
            }

        }

        // now print out for interest
        // System.out.println(Arrays.toString(means));

        // now return the argmax array

        int[] argMax = new int[n];
        for (int i=0; i<n; i++) {
            argMax[i] = argmax(means[i]);
        }

        return argMax;
    }

    static int argmax(StatSummary[] stats) {
        Picker<Integer> picker = new Picker<>(Picker.MAX_FIRST);
        for (int i=0; i<stats.length; i++) {
            picker.add(stats[i].mean() + epsilon * random.nextDouble(), i);
        }
        return picker.getBest();
    }

}
