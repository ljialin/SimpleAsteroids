package paired;

import evodef.EvalMaxM;
import evodef.NoisySolutionEvaluator;
import evodef.SearchSpaceUtil;
import evogame.Mutator;
import ntuple.ScoredVec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon Lucas on 30/06/2017.
 */
public class ProblemInstance {

    // create a list to store the scored vectors in
    List<ScoredVec> scoredVecs;

    // each n-dimensional
    int n = 10;

    // each dimension having m possible values
    // popular value for m is 2
    int m = 2;

    // make k random vectors
    int k = 10;


    // when evaluation add gaussian noise with this standard deviation
    double noise = 1.0;

    NoisySolutionEvaluator evaluator = new EvalMaxM(n, m, noise);

    // now create and score the vectors and add them do the list


    public ProblemInstance() {
    }

    public ProblemInstance(int n, int m, int k, NoisySolutionEvaluator evaluator) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.evaluator = evaluator;
        // getVecs();
    }

    public ProblemInstance useVecsAroundRandomPoint() {
        scoredVecs = new ArrayList<>();
        int[] px = SearchSpaceUtil.randomPoint(evaluator.searchSpace());
        Mutator mutator = new Mutator(evaluator.searchSpace());
        mutator.pointProb = 0.0; // 1.0; // 3 / n;
        // mutator
        Mutator.flipAtLeastOneValueDefault = true;
        Mutator.totalRandomChaosMutation = false;

        for (int i = 0; i < k; i++) {
            // int[] p = SearchSpaceUtil.randomPoint(searchSpace);
            int[] p = mutator.randMut(px);
            double score = evaluator.evaluate(p);
            scoredVecs.add(new ScoredVec(p, score));
            // System.out.println(Arrays.toString(p) + " : " + String.format("%.2f\t %.0f", score, evaluator.trueFitness(p)));
        }
        return this;
    }

    public ProblemInstance useRandomVecs() {
        scoredVecs = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            // int[] p = SearchSpaceUtil.randomPoint(searchSpace);
            int[] p = SearchSpaceUtil.randomPoint(evaluator.searchSpace());
            double score = evaluator.evaluate(p);
            scoredVecs.add(new ScoredVec(p, score));
            // System.out.println(Arrays.toString(p) + " : " + String.format("%.2f\t %.0f", score, evaluator.trueFitness(p)));
        }
        return this;
    }



}
