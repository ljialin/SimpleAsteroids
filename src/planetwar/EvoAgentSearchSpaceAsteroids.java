package planetwar;

import asteroids.AsteroidsGameState;
import evodef.*;
import evogame.DefaultParams;
import evogame.GameParameters;
import evogame.Mutator;
import ga.SimpleRMHC;
import ntuple.CompactSlidingGA;
import ntuple.SlidingMeanEDA;

/**
 *   Note: this code needs refactoring to remove the solution evaluator (in this case
 *   based on the Asteroids game) and to put in to a separate class.
 *
 *   This has been done in a non-ideal way for now to get it working quickly.
 */

public class EvoAgentSearchSpaceAsteroids implements NoisySolutionEvaluator, SearchSpace {

    public static void main(String[] args) {
        EvoAgentSearchSpaceAsteroids searchSpace = new EvoAgentSearchSpaceAsteroids();

        int[] point = SearchSpaceUtil.randomPoint(searchSpace);

        System.out.println(searchSpace.report(point));

        System.out.println();
        System.out.println("Size: " + SearchSpaceUtil.size(searchSpace));

        NoisySolutionEvaluator eval = new EvoAgentSearchSpaceAsteroids();
        System.out.println("Search space size: " + SearchSpaceUtil.size(eval.searchSpace()));

        int[] solution = {1, 1, 1, 0, 5};

        System.out.println("Checking fitness");
        HyperParamTuningTest.runChecks(eval, solution);
        searchSpace.report(solution);

    }

    double[] pointMutationRate = {0.0, 1.0, 2.0, 3.0};
    boolean[] flipAtLeastOneBit = {false, true};
    boolean[] useShiftBuffer = {false, true};
    int[] nResamples = {1, 2, 3};
    int[] seqLength = {5, 10, 15, 20, 50, 100, 150};


    public static int tickBudget = 2000;

    int[] nValues = new int[]{pointMutationRate.length, flipAtLeastOneBit.length,
            useShiftBuffer.length, nResamples.length, seqLength.length};
    int nDims = nValues.length;

    static int pointMutationRateIndex = 0;
    static int flipAtLeastOneBitIndex = 1;
    static int useShiftBufferIndex = 2;
    static int nResamplesIndex = 3;
    static int seqLengthIndex = 4;

    // NoisySolutionEvaluator problemEvaluator;

    // log the solutions found
    EvolutionLogger logger;

    public EvoAgentSearchSpaceAsteroids() {
        this.logger = new EvolutionLogger();
    }

    public String report(int[] solution) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("pointMutationRate:     %.2f\n", pointMutationRate[solution[pointMutationRateIndex]]));
        sb.append(String.format("flipAtLeastOneBit:     %s\n", flipAtLeastOneBit[solution[flipAtLeastOneBitIndex]]));
        sb.append(String.format("useShiftBuffer:        %s\n", useShiftBuffer[solution[useShiftBufferIndex]]));
        sb.append(String.format("nResamples:            %d\n", nResamples[solution[nResamplesIndex]]));
        sb.append(String.format("seqLength:             %d\n", seqLength[solution[seqLengthIndex]]));
        sb.append(String.format("nEvals:                %d\n", getNEvals(solution)));
        return sb.toString();
    }

    public int getNEvals(int[] solution) {
        return tickBudget / seqLength[solution[seqLengthIndex]];
    }

//    public EvoAgentSearchSpace setEvaluator(NoisySolutionEvaluator problemEvaluator) {
//        this.problemEvaluator = problemEvaluator;
//        return this;
//    }

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

    int innerEvals = 2000;

    int nEvals = 0;
    @Override
    public void reset() {
        nEvals = 0;
    }


    static GameParameters params = new GameParameters().injectValues(new DefaultParams());

    static int maxTick = 1000;

    @Override
    public double evaluate(int[] x) {

        // create a problem to evaluate this one on ...
        // this should really be set externally, but just doing it this way for now

        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params).initForwardModel();

        // search space will need to be set before use
        Mutator mutator = new Mutator(null);
        mutator.pointProb = pointMutationRate[x[pointMutationRateIndex]];
        mutator.flipAtLeastOneValue = flipAtLeastOneBit[x[flipAtLeastOneBitIndex]];

        // setting to true may give best performance
        mutator.totalRandomChaosMutation = false;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples[x[nResamplesIndex]]);
        simpleRMHC.setMutator(mutator);

        EvoAlg sliding = new SlidingMeanEDA();

        EvoAgent evoAgent = new EvoAgent().setEvoAlg(simpleRMHC, getNEvals(x));
        // EvoAgent evoAgent = new EvoAgent().setEvoAlg(sliding, getNEvals(x));
        evoAgent.setUseShiftBuffer(useShiftBuffer[x[useShiftBufferIndex]]);
        evoAgent.setSequenceLength(seqLength[x[seqLengthIndex]]);

        for (int i=0; i<maxTick; i++) {
            int action = evoAgent.getAction(gameState, 0);
            gameState.next(new int[]{action});
        }
        double fitness = gameState.getScore();

        logger.log(fitness, x, false);

        System.out.println("Fitness: " + (int) fitness + " : " + fitness);
        System.out.println();

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
