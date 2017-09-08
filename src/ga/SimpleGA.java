package ga;

import evodef.*;
import ntuple.NTupleSystem;
import utilities.StatSummary;

import java.util.ArrayList;

/**
 *
 */

public class SimpleGA implements EvoAlg {

    int popSize;

    // default to 1
    int nSamples = 1;

    // the percentage of individuals generated via crossover
    double crossoverRate;

    ArrayList<ScoredVec> pop;
    SolutionEvaluator evaluator;

    public SimpleGA setPopulationSize() {
        return this;
    }

    public SimpleGA initPop() {
        pop = new ArrayList<>();
        for (int i=0; i<popSize; i++) {
            pop.add(new ScoredVec(SearchSpaceUtil.randomPoint(evaluator.searchSpace())));
        }
        return this;
    }

    @Override
    public void setInitialSeed(int[] seed) {
        throw new RuntimeException("Not yet implemented");
        // aim will be to include actual seed plus many mutations of it
    }

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;

        initPop();

        while (evaluator.nEvals() < nEvals) {

            // evaluate them



            // breed them

        }

        return pop.get(0).p;

    }

    private void evalPop() {
        for (ScoredVec sv : pop) {
            sv.score = fitness(sv.p);
        }
    }

    double fitness(int[] sol) {
        if (nSamples == 1)
            return evaluator.evaluate(sol);
        // otherwise ...
        StatSummary ss = new StatSummary();
        for (int i=0; i<nSamples; i++) {
            double fitness = evaluator.evaluate(sol);
            ss.add(fitness);

        }
        return ss.mean();
    }



    @Override
    public void setModel(NTupleSystem nTupleSystem) {

    }

    @Override
    public NTupleSystem getModel() {
        return null;
    }

    @Override
    public EvolutionLogger getLogger() {
        return null;
    }

    @Override
    public void setSamplingRate(int samplingRate) {

    }
}
