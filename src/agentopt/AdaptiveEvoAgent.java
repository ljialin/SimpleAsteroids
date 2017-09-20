package agentopt;

import evodef.SearchSpace;

public class AdaptiveEvoAgent implements SearchSpace{


    

    double[] discountFactor = {0.5, 0.8, 0.9, 0.95, 0.99, 1.0};
    int[] rolloutLength = {1, 2, 4, 7, 10, 15, 25};
    double[] explorationFactor = {0, 0.5, 1.0, 2.0, 5.0};

    int[] nValues = new int[]{discountFactor.length, rolloutLength.length, explorationFactor.length};
    int nDims = nValues.length;

    public static int discountFactorIndex = 0;
    public static int selectionPressureIndex = 1;
    public static int popSizeIndex = 2;
    public static int sampleRateIndex = 3;

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return nValues[i];
    }
}
