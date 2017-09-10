package ga;

import evodef.*;
import evogame.Mutator;
import ntuple.NTupleSystem;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 */


public class SimpleGA implements EvoAlg {

    public static void main(String[] args) {
        int popSize = 100;
        SimpleGA sga = new SimpleGA().setPopulationSize(20);
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
        System.out.println(popSize);
        return this;
    }

    public SimpleGA setSampleRate(int nSamples) {
        this.nSamples = nSamples;
        return this;
    }

    public SimpleGA setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
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
        // System.out.println(pop.size() + " : " + popSize);

        while (evaluator.nEvals() < nEvals) {

            int prevEvals = evaluator.nEvals();

            // evaluate them
            evalPop();


            // breed them
            pop = breed();

            int diffEvals = evaluator.nEvals() - prevEvals;
            for (int i=0; i<diffEvals; i++) {
                evaluator.logger().logBestYest(pop.get(0).p);
            }

        }
        // a final evaluation
        evalPop();
        return pop.get(0).p;
    }

    ArrayList<ScoredVec> breed() {


        ArrayList<ScoredVec> nextPop = new ArrayList<>();

        while (nextPop.size() < popSize) {
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
        }
        return nextPop;
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
        double x = rand.nextDouble() * selectionPressure;
        int ix = (int) (popSize * x * x);
        return ix;
    }

    private void evalPop() {
        for (ScoredVec sv : pop) {
            sv.score = fitness(sv.p);
        }
        // and sort them
        Collections.sort(pop);

        // System.out.println("Pop size: " + pop.size());
        int i = 0;
//        for (ScoredVec sv : pop) {
//            System.out.println(i++ + " : " + Arrays.toString(sv.p) + " : " + sv.score);
//        }
        // System.out.println();
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
