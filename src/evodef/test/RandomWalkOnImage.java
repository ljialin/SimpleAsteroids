package evodef.test;

import static evodef.test.EvoImageTest.filepath;

public class RandomWalkOnImage {
    public static void main(String[] args) throws Exception {
        EvoImageTest eit = new EvoImageTest();
        filepath = "data/logos/dice.png";
        eit.setImage(filepath);
        eit.computeDistribution();
        int nSteps = 10000;
        // set to zero to just show initial image
        // nSteps = 0;
        // eit.setRandomImage();
        eit.randomWalkAway(nSteps);
    }
}
