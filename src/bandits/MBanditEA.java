package bandits;

import evodef.EvoAlg;
import evodef.EvolutionLogger;
import evodef.SearchSpace;
import evodef.SolutionEvaluator;
import evomaze.MazeView;
import evomaze.ShortestPathTest;
import ntuple.NTupleSystem;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */
public class MBanditEA implements EvoAlg {
    MBanditArray genome;
    static Random rand = new Random();
    // static double noiseStdDev = 0.78 / Math.sqrt(2);
    static double noiseStdDev = 0; //0.5;


    public void setInitialSeed(int[] seed) {
        throw new RuntimeException("Function not yet implemented for this EA");
    }

    public MBanditEA() {
    }


    NTupleSystem model;
    @Override
    public void setModel(NTupleSystem nTupleSystem) {
        this.model = nTupleSystem;
    }

    @Override
    public NTupleSystem getModel() {
        return model;
    }

    @Override
    public EvolutionLogger getLogger() {
        return null;

    }

    @Override
    public void setSamplingRate(int samplingRate) {
        
    }


    public void init(SearchSpace searchSpace) {
        genome = new MBanditArray(searchSpace);
    }


    // this runs a trial of the evolutionary algorithm
    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {

        init(evaluator.searchSpace());
        double bestYet = evaluator.evaluate(genome.toArray());
        int nTrials = 1;
        while (evaluator.nEvals() < nEvals && !evaluator.optimalFound()) {
            // each evaluation, make a mutation
            // measure the fitness
            // and feed it back

            // Jialin pointed out that the trials so far
            // is actually twice the number of iterations
            // but this is fixable to be the same in the case of

            // the evaluator keeps track of all the logging

            MBanditGene gene = genome.selectGeneToMutate(nTrials);

            double before = evaluator.evaluate(genome.toArray());

            gene.mutate();
            double after = evaluator.evaluate(genome.toArray());

            // the concept of trials is used a bit differently within this
            // algorithm - it's just used to control the bandit policy
            // and we're choosing to just increment it once per loop
            nTrials++;

            double delta = after - before;

            // the noise here can be used to help the algorithm escape local minima
            // but should normally be set to zero

            // the noise would then be applied within the

            double noise = rand.nextGaussian() * noiseStdDev;
            delta += noise;

            gene.applyReward(delta);
            gene.revertOrKeep(delta);

            bestYet = Math.max(before, after);

        }

        return genome.toArray();
    }
}
