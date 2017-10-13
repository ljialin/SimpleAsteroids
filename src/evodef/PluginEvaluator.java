package evodef;

/**
 * This JUST defines an evaluation interface, to be plugged in to a wider context
 */

public interface PluginEvaluator {
    double fitness(int[] solution);
}
