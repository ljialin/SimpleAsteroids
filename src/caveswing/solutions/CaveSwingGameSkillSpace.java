package caveswing.solutions;

import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import caveswing.core.CaveGameFactory;
import caveswing.core.CaveSwingParams;
import caveswing.test.EvoAgentVisTest;
import evodef.*;
import ga.SimpleRMHC;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import ggi.tests.SpeedTest;
import math.Vector2d;
import ntuple.params.DoubleParam;
import ntuple.params.IntegerParam;
import ntuple.params.Param;
import ntuple.params.Report;
import utilities.ElapsedTimer;

import java.awt.*;


/**
 * This is a solution to the lab script excer
 */
public class CaveSwingGameSkillSpace implements AnnotatedFitnessSpace {

    public static void main(String[] args) {
        ElapsedTimer timer = new ElapsedTimer();
        CaveSwingGameSkillSpace searchSpace = new CaveSwingGameSkillSpace();
        searchSpace.verbose = true;
        int[] point = SearchSpaceUtil.randomPoint(searchSpace);

        // or choose a specific one:

        point = new int[]{1, 4, 3, 3, 0, 0};

        System.out.println(Report.report(searchSpace, point));
        System.out.println();
        System.out.println("Size: " + SearchSpaceUtil.size(searchSpace));
        System.out.println("Value: " + searchSpace.evaluate(point));
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
        };
    }

    double[] xGravity = {-0.02, 0.0, 0.02};
    double[] yGravity = {-0.02, -0.01, 0, 0.01, 0.02};

    double[] hooke = {0.0, 0.005, 0.01, 0.02, 0.03};
    double[] lossFactor = {0.99, 0.999, 1.0, 1.01};
    double[] anchorHeight = {0.2, 0.5, 0.8};
    int[] nAnchors = {5, 10, 15};

    int[] nValues = new int[]{xGravity.length, yGravity.length,
            hooke.length, lossFactor.length, anchorHeight.length, nAnchors.length};
    int nDims = nValues.length;

    static int xGravityIndex = 0;
    static int yGravtityIndex = 1;
    static int hookeIndex = 2;
    static int lossFactorIndex = 3;
    static int anchorHeightIndex = 4;
    static int nAnchorsIndex = 5;

    int nGames = 10;
    int maxSteps = 1000;

    public EvolutionLogger logger;

    public CaveSwingGameSkillSpace() {
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

    @Override
    public double evaluate(int[] x) {

        // bundle extract the selected params from the solution vector
        // and inject in to the game design params

        GameRunnerTwoPlayer gameRunner = new GameRunnerTwoPlayer();

        // set up the params
        CaveSwingParams params = new CaveSwingParams();

        params.gravity = new Vector2d(
                xGravity[x[xGravityIndex]],
                yGravity[x[yGravtityIndex]]
        );

        params.hooke = hooke[x[hookeIndex]];
        params.lossFactor = lossFactor[x[lossFactorIndex]];
        params.meanAnchorHeight = params.height * anchorHeight[x[anchorHeightIndex]];
        params.nAnchors = nAnchors[x[nAnchorsIndex]];

        // using a Game Factory enables the tester to start with a fresh copy of the
        // game each time
        CaveGameFactory gameFactory = new CaveGameFactory().setParams(params);
        SpeedTest randomTest = new SpeedTest().setGameFactory(gameFactory);
        randomTest.setPlayer(new RandomAgent());

        randomTest.playGames(nGames, maxSteps);

        if (verbose) {
            System.out.println("Random play");
            System.out.println(randomTest.gameScores);
        }

        SpeedTest skillTest = new SpeedTest().setGameFactory(gameFactory);
        skillTest.setPlayer(getEvoAgent());
        skillTest.playGames(nGames, maxSteps);

        if (verbose) {
            System.out.println("Skilful play");
            System.out.println(skillTest.gameScores);
        }

        double value = skillTest.gameScores.mean() - randomTest.gameScores.mean();
        // return this for now, and see what we get
        logger.log(value, x, false);
        return value;
    }

    public static SimplePlayerInterface getEvoAgent() {
        //
        // todo Add in the code to make this
        int nResamples = 1;

        DefaultMutator mutator = new DefaultMutator(null);
        // setting to true may give best performance
        mutator.totalRandomChaosMutation = true;
        mutator.flipAtLeastOneValue = true;
        mutator.pointProb = 5;

        SimpleRMHC simpleRMHC = new SimpleRMHC();
        simpleRMHC.setSamplingRate(nResamples);
        simpleRMHC.setMutator(mutator);

        EvoAlg evoAlg = simpleRMHC;

        // evoAlg = new SlidingMeanEDA();

        int nEvals = 20;
        int seqLength = 100;
        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setUseShiftBuffer(true);
        return evoAgent;
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
