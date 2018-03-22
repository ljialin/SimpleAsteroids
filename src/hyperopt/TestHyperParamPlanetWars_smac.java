package hyperopt;

import evodef.AnnotatedFitnessSpace;
import planetwar.EvoAgentSearchSpace;

import java.util.Arrays;

public class TestHyperParamPlanetWars_smac {
    /**
     * Receives one solution as args, evaluates and prints to stdout the fitness
     */
    public static void main(String[] args) {
        AnnotatedFitnessSpace testPlanetWars = new EvoAgentSearchSpace();
        int sum = 0;
        for (int i=0; i<100; i++) {
            double fitness = runTrial(testPlanetWars, args);
            sum += fitness;
//            System.out.println(fitness);
        }
        System.out.println(sum);
    }

    /**
     * Runs one evaluation of a solution p
     * @param evaluator - solution evaluator
     * @param solution - solution to be evaluated
     * @return - fitness of solution p
     */
    public static double runTrial(AnnotatedFitnessSpace evaluator, String[] solution) {
        int[] p = Arrays.stream(solution).mapToInt(Integer::parseInt).toArray();
        return evaluator.evaluate(p);
    }
}

