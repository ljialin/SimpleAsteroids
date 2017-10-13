package evodef;

import java.util.Arrays;

public class SimpleGameAdapter implements SolutionEvaluator {

    PluginEvaluator pluginEvaluator;
    SearchSpace searchSpace;
    EvolutionLogger logger = new EvolutionLogger();

    public SimpleGameAdapter setEvaluator(PluginEvaluator pluginEvaluator) {
        this.pluginEvaluator = pluginEvaluator;
        return this;
    }

    public SimpleGameAdapter setSearchSpace(SearchSpace searchSpace) {
        this.searchSpace = searchSpace;
        return this;
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public double evaluate(int[] solution) {
        // System.out.println("Evaluating: " + Arrays.toString(solution));
        double fitness = pluginEvaluator.fitness(solution);
        // System.out.println(fitness);
        logger.log(fitness, solution, false);
        return fitness;
    }

    @Override
    public boolean optimalFound() {
        return false;
    }

    @Override
    public SearchSpace searchSpace() {
        return searchSpace;
    }

    @Override
    public int nEvals() {
        return logger.nEvals();
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
