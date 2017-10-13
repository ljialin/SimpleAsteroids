package ga;

import evodef.EvolutionLogger;
import evodef.NoisySolutionEvaluator;
import evodef.SearchSpace;

import java.util.Arrays;

public class SimpleGASearchSpace implements NoisySolutionEvaluator, SearchSpace {

    double[] crossoverRate = {0, 0.2, 0.4, 0.6, 0.8, 1,0};
    double[] selectionPressure = {0.0, 0.2, 0.5, 0.75, 1.0};
    int[] popSize = {2, 5, 10, 20, 50, 100};
    int[] sampleRate = {1, 2,3,5,10};

    int[] nValues = new int[]{crossoverRate.length, selectionPressure.length, popSize.length, sampleRate.length};
    int nDims = nValues.length;

    static int crossoverIndex = 0;
    static int selectionPressureIndex = 1;
    static int popSizeIndex = 2;
    static int sampleRateIndex = 3;

    NoisySolutionEvaluator problemEvaluator;

    // log the solutions found
    EvolutionLogger logger;

    public SimpleGASearchSpace() {
        this.logger = new EvolutionLogger();
    }

    public String report(int[] solution) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("crossover rate:     %.2f\n", crossoverRate[solution[crossoverIndex]]));
        sb.append(String.format("selection pressure: %.2f\n", selectionPressure[solution[selectionPressureIndex]]));
        sb.append(String.format("population size:    %d\n", popSize[solution[popSizeIndex]]));
        sb.append(String.format("sample rate    :    %d\n", sampleRate[solution[sampleRateIndex]]));
        return sb.toString();
    }

    public SimpleGASearchSpace setEvaluator(NoisySolutionEvaluator problemEvaluator) {
        this.problemEvaluator = problemEvaluator;
        return this;
    }

    @Override
    public Boolean isOptimal(int[] solution) {
        return null;
    }

    @Override
    public Double trueFitness(int[] solution) {
        return null;
    }

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return nValues[i];
    }

    int innerEvals = 1000;

    int nEvals = 0;
    @Override
    public void reset() {
        nEvals = 0;

    }

    @Override
    public double evaluate(int[] x) {
        // evaluate hyperparameters x
        // make a new SimpleGA

        // with all the necessary settings

        // note that at the moment, these next few steps are particular to the SimpleGA
        // however, much of the code could be made far more general

        // System.out.println();
        // System.out.println("Before sga");

        SimpleGA sga = new SimpleGA();
        sga.setCrossoverRate(crossoverRate[x[crossoverIndex]]);
        sga.setSelectionPressure(selectionPressure[x[selectionPressureIndex]]);
        sga.setPopulationSize(popSize[x[popSizeIndex]]);
        sga.setSampleRate(sampleRate[x[sampleRateIndex]]);

        // okay, we've made the algorithm, now we evaluate it ...

        // now run the sga on the problem at hand

        problemEvaluator.reset();
        // System.out.println("Aah");
        int[] solution = sga.runTrial(problemEvaluator, innerEvals);
//        System.out.println(nEvals + " -> " + Arrays.toString(solution));
//        System.out.println(sga.getLogger());

        nEvals++;

        // what is the fitness of this solution?
        // well, it should be simply the quality of the solution found at the end of the run

        double fitness = sga.finalFitness; // sga.getLogger().finalFitness();
        // System.out.println("Hey!");

//        System.out.println(Arrays.toString(x));
//        System.out.println(fitness);

        logger.log(fitness, solution, false);

        return fitness;
    }

    @Override
    public boolean optimalFound() {
        return false;
    }

    @Override
    public SearchSpace searchSpace() {
        return this;
    }

    @Override
    public int nEvals() {
        return nEvals;
    }

    @Override
    public EvolutionLogger logger() {
        return logger;
    }

    @Override
    public Double optimalIfKnown() {
        return null;
    }
}
