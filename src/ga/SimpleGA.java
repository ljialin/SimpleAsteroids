package ga;

import evodef.*;
import evogame.Mutator;
import ntuple.NTupleSystem;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 */


public class SimpleGA implements EvoAlg {

    public static void main(String[] args) {
        int popSize = 100;
        SimpleGA sga = new SimpleGA();
        for (int i=0; i<20; i++) {
            System.out.println(sga.selectRank(popSize));
        }
    }

    int popSize;

    // default to 1
    int nSamples = 1;

    double selectionPressure = 0.5;


    // the percentage of individuals generated via crossover
    double crossoverRate;

    ArrayList<ScoredVec> pop;
    SolutionEvaluator evaluator;

    public SimpleGA setPopulationSize(int popSize) {
        this.popSize = popSize;
        return this;
    }

    public SimpleGA setSampleRate(int nSamples) {
        this.nSamples = nSamples;
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

    static Random rand = new Random();


    Mutator mutator;

    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {
        this.evaluator = evaluator;
        mutator = new Mutator(evaluator.searchSpace());

        initPop();

        while (evaluator.nEvals() < nEvals) {

            // evaluate them
            evalPop();


            ArrayList<ScoredVec> nextPop = new ArrayList<>();

            // child c
            int[] c;
            if (rand.nextDouble() < crossoverRate) {
                int[] p1 = select();
                int[] p2 = select();
                c = uniformCrossover(p1, p2);
            } else {
                int[] p = select();
                c = mutator.randMut(p);
            }
            nextPop.add(new ScoredVec(c));

            // breed them
            pop = nextPop;

        }
        return pop.get(0).p;
    }

    int[] uniformCrossover(int[] p1, int[] p2) {
        int[] c = new int[p1.length];
        for (int i=0; i<c.length; i++) {
            c[i] = rand.nextDouble() < 0.5 ? p1[i] : p2[i];
        }
        return c;
    }

    int[] select() {
        // assumes population has already been sorted in to order
        int ix = selectRank(pop.size());
        return pop.get(ix).p;
    }

    int selectRank(int popSize) {
        double x = rand.nextDouble() * 0.7;
        int ix = (int) (popSize * x * x);
        return ix;
    }

    private void evalPop() {
        for (ScoredVec sv : pop) {
            sv.score = fitness(sv.p);
        }
        // and sort them
        Collections.sort(pop);
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
