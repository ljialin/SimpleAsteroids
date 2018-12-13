package caveswing.design;

import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import caveswing.core.CaveGameFactory;
import caveswing.core.CaveSwingParams;
import evodef.*;
import ggi.agents.EvoAgentFactory;
import ggi.core.SimplePlayerInterface;
import ggi.tests.SpeedTest;
import math.Vector2d;
import ntuple.params.DoubleParam;
import ntuple.params.IntegerParam;
import ntuple.params.Param;
import ntuple.params.Report;
import utilities.ElapsedTimer;

public class CaveSwingFitnessSpace implements AnnotatedFitnessSpace {

    public static void main(String[] args) {
        ElapsedTimer timer = new ElapsedTimer();
        CaveSwingFitnessSpace fitnessSpace = new CaveSwingFitnessSpace();
        fitnessSpace.verbose = true;
        int[] point = SearchSpaceUtil.randomPoint(fitnessSpace);

        // or choose a specific one:

        // point = new int[]{1, 5, 2, 0, 4, 0, 1, 1};

        point = new int[]{1, 5, 2, 0, 4, 1, 3, 2};

        System.out.println(Report.report(fitnessSpace, point));
        System.out.println();
        System.out.println("Size: " + SearchSpaceUtil.size(fitnessSpace));
        System.out.println("Value: " + fitnessSpace.evaluate(point));
        System.out.println(timer);
    }

    boolean verbose = false;

    public Param[] getParams() {
        return new Param[]{
                new DoubleParam().setArray(xGravity).setName("x Gravity"),
                new DoubleParam().setArray(yGravity).setName("y Gravity"),
                new DoubleParam().setArray(hooke).setName("Hooke's constant"),
                new DoubleParam().setArray(lossFactor).setName("Loss factor"),
                new DoubleParam().setArray(anchorHeight).setName("Anchor height"),
                new IntegerParam().setArray(nAnchors).setName("Number of Anchors"),
                new IntegerParam().setArray(height).setName("Height in pixels"),
                new IntegerParam().setArray(width).setName("Width in pixels"),
        };
    }

    double[] xGravity = {-0.02, 0.0, 0.02};
    double[] yGravity = {-0.2, -0.1, 0, 0.1, 0.2, 0.5, 1.0};

    double[] hooke = {0.0, 0.005, 0.01, 0.02, 0.03};
    double[] lossFactor = {0.99, 0.999, 1.0, 1.01};
    double[] anchorHeight = {0.1, 0.2, 0.3, 0.4, 0.5};
    int[] nAnchors = {5, 10, 15, 20, 25};
    int[] height = {100, 200, 300, 400};
    int[] width = {600, 900, 1500, 2500};

    int[] nValues = new int[]{xGravity.length, yGravity.length,
            hooke.length, lossFactor.length, anchorHeight.length, nAnchors.length, height.length, width.length};
    int nDims = nValues.length;

    static int xGravityIndex = 0;
    static int yGravtityIndex = 1;
    static int hookeIndex = 2;
    static int lossFactorIndex = 3;
    static int anchorHeightIndex = 4;
    static int nAnchorsIndex = 5;
    static int heightIndex = 6;
    static int widthIndex = 7;

    int nGames = 1;
    int maxSteps = 500;

    public EvolutionLogger logger;

    public CaveSwingFitnessSpace() {
        this.logger = new EvolutionLogger();
    }

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return nValues[i];
    }

    // int innerEvals = 2000;

    // int nEvals = 0;
    @Override
    public void reset() {
        // nEvals = 0;
        logger.reset();
    }

    public CaveSwingParams getParams(int[] x) {
        // bundle extract the selected params from the solution vector
        // and inject in to the game design params

        // set up the params
        CaveSwingParams params = new CaveSwingParams();

        params.gravity = new Vector2d(
                xGravity[x[xGravityIndex]],
                yGravity[x[yGravtityIndex]]
        );

        params.hooke = hooke[x[hookeIndex]];
        params.lossFactor = lossFactor[x[lossFactorIndex]];
        params.nAnchors = nAnchors[x[nAnchorsIndex]];
        params.height = height[x[heightIndex]];
        params.width = width[x[widthIndex]];

        // set this after the height has been set
        params.meanAnchorHeight = params.height * anchorHeight[x[anchorHeightIndex]];

        params.maxTicks = maxSteps;
        return params;
    }

    @Override
    public double evaluate(int[] x) {

        CaveSwingParams params = getParams(x);
        // using a Game Factory enables the tester to start with a fresh copy of the
        // game each time
        CaveGameFactory gameFactory = new CaveGameFactory().setParams(params);
        SpeedTest speedTest = new SpeedTest().setGameFactory(gameFactory);


//        SimplePlayerInterface randPlayer = new RandomAgent();
//        speedTest.setPlayer(randPlayer);
//        double dumbScore = speedTest.playGames(nGames, maxSteps).gameScores.mean();

        EvoAgentFactory evoAgentFactory = new EvoAgentFactory();
        evoAgentFactory.seqLength = 100;
        EvoAgent evoGood = evoAgentFactory.getAgent();

        // set to visual just for testing
        // evoGood.setVisual();
        speedTest = new SpeedTest().setGameFactory(gameFactory);
        speedTest.setPlayer(evoGood);
        double smartScore = speedTest.playGames(nGames, maxSteps).gameScores.mean();


        // try a different dumb score

        evoAgentFactory.useShiftBuffer = false;
        evoAgentFactory.seqLength = 20;
        EvoAgent evoMediocre = evoAgentFactory.getAgent();

        speedTest = new SpeedTest().setGameFactory(gameFactory);
        speedTest.setPlayer(evoMediocre);
        double dumbScore = speedTest.playGames(nGames, maxSteps).gameScores.mean();

        System.out.println((int) dumbScore + "\t " + (int) smartScore);

        double value = smartScore - dumbScore;
        logger.log(value, x, false);

        return value;

//        speedTest.playGames(nGames, maxSteps);
//
//        if (verbose) {
//            System.out.println(speedTest.gameScores);
//        }
//
//        double value = speedTest.gameScores.mean();
//        // return this for now, and see what we get
//        logger.log(value, x, false);
//        return value;
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
