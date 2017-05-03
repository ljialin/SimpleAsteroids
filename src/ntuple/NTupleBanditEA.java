package ntuple;

import evodef.*;
import evogame.Mutator;
import utilities.ElapsedTimer;
import utilities.Picker;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by sml on 09/01/2017.
 *
 */

public class NTupleBanditEA  implements EvoAlg {

    NTupleSystem nTupleSystem;

    // the exploration rate normally called K or C - called kExplore here for clarity
    double kExplore = 1.0;
    // the number of neighbours to explore around the current point each time
    // they are only explored IN THE FITNESS LANDSCAPE MODEL, not by sampling the fitness function
    int nNeighbours = 100;

    // when searching for the best solution overall, at the end of the run
    // we ask the NTupleMemory to explore a neighbourhood around each
    // of the points added during the search
    // this param controls the size of the neighbourhood
    int neighboursWhenFindingBest = 10;


    public NTupleBanditEA(double kExplore, int nNeighbours) {
        this.kExplore = kExplore;
        this.nNeighbours = nNeighbours;
    }

    public NTupleBanditEA() {
    }



    int[] seed;

    public void setInitialSeed(int[] seed) {
        this.seed = SearchSpaceUtil.copyPoint(seed);
    }

    SolutionEvaluator evaluator;
    @Override
    public int[] runTrial(SolutionEvaluator evaluator, int nEvals) {

        this.evaluator = evaluator;
        // set  up some convenient references
        SearchSpace searchSpace = evaluator.searchSpace();
        EvolutionLogger logger = evaluator.logger();
        Mutator mutator = new Mutator(searchSpace);

        nNeighbours = (int) Math.min (nNeighbours, SearchSpaceUtil.size(searchSpace) / 4);
        // System.out.println("Set neighbours to: " + nNeighbours);

        // create an NTuple fitness landscape model
        nTupleSystem = new NTupleSystem(searchSpace);
        nTupleSystem.addTuples();

        // then each time around the loop try the following
        // create a neighbourhood set of points and pick the best one that combines it's exploitation and evaluation scores

        StatSummary ss = new StatSummary();

        int[] p;
        if (seed == null) {
            p = SearchSpaceUtil.randomPoint(searchSpace);
        } else {
            p = seed;
        }

        // nTupleSystem.printDetailedReport();

        while (evaluator.nEvals() < nEvals) {

            // each time around the loop we make one fitness evaluation of p
            // and add this NEW information to the memory

            double fitness = evaluator.evaluate(p);
            ElapsedTimer t = new ElapsedTimer();

            nTupleSystem.addPoint(p, fitness);

            // ss.add(t.elapsed());
//            System.out.println(ss);
//            System.out.println("N Neighbours: " + nNeighbours);
            EvaluateChoices evc = new EvaluateChoices(nTupleSystem, kExplore);
            // evc.add(p);

            // and then explore the neighbourhood around p, balancing exploration and exploitation
            // depending on the mutation function, some of the neighbours could be far away
            // or some of them could be duplicates - duplicates a bit wasteful so filter these
            // out - repeat until we have the required number of unique neighbours



            while (evc.n() < nNeighbours) {
                int[] pp = mutator.randMut(p);
                evc.add(pp);
            }

            // now set the next point to explore
            p = evc.picker.getBest();
//            logger.keepBest(picker.getBest(), picker.getBestScore());
            // System.out.println("Best solution: " + Arrays.toString(picker.getBest()) + "\t: " + picker.getBestScore());

        }

//        System.out.println("Time for calling addPoint: ");
//        System.out.println(ss);

        // int[] solution = nTupleSystem.getBestSolution();
        int[] solution = nTupleSystem.getBestOfSampled();
        // int[] solution = nTupleSystem.getBestOfSampledPlusNeighbours(neighboursWhenFindingBest);
        logger.keepBest(solution, evaluator.evaluate(solution));
        return solution;
    }


    @Override
    public void setModel(NTupleSystem nTupleSystem) {
        this.nTupleSystem = nTupleSystem;
    }

    @Override
    public NTupleSystem getModel() {
        return nTupleSystem;
    }

    @Override
    public EvolutionLogger getLogger() {
        return evaluator.logger();
    }

}
