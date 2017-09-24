package agentopt;

import evodef.SearchSpace;

public class AdaptiveEvoAgentSpace implements SearchSpace {

    public static void main(String[] args) {
        AdaptiveEvoAgentSpace space = new AdaptiveEvoAgentSpace();
        System.out.println(space.report(new int[]{0, 0, 0, 0}));
    }

    static double[] discountFactor = {0.5, 0.8, 0.9, 0.95, 0.99, 1.0};
    static int[] rolloutLength = {2, 4, 7, 10, 15, 25};
    static double[] explorationFactor = {0, 0.5, 1.0, 2.0, 5.0};
    static int[] historyLength = {2, 4, 7, 10, 15, 25, 50};

    static int[] nValues = new int[]{discountFactor.length, rolloutLength.length,
            explorationFactor.length, historyLength.length};

    public static int discountFactorIndex = 0;
    public static int rolloutLengthIndex = 1;
    public static int exlporationFactorIndex = 2;
    public static int historyLengthIndex = 3;

    @Override
    public int nDims() {
        return nValues.length;
    }

    @Override
    public int nValues(int i) {
        return nValues[i];
    }

    // there should be an easier way to retrieve values than this ...
    // and there is (e.g. to return an Object each time, then type cast

    public double getDiscountFactor(int[] solution) {
        return discountFactor[solution[discountFactorIndex]];
    }

    public int getRolloutLength(int[] solution) {
        return rolloutLength[solution[rolloutLengthIndex]];
    }

    public double getExplorationFactor(int[] solution) {
        return explorationFactor[solution[exlporationFactorIndex]];
    }

    public int getHistoryLength(int[] solution) {
        return historyLength[solution[historyLengthIndex]];
    }

    public String report(int[] solution) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Discount factor: %.3f\n", getDiscountFactor(solution)));
        sb.append(String.format("Rollout Length:  %d\n", getRolloutLength(solution)));
        sb.append(String.format("k Exploration:   %.3f\n", getExplorationFactor(solution)));
        sb.append(String.format("History Length:  %d\n", getHistoryLength(solution)));
        return sb.toString();
    }

}
