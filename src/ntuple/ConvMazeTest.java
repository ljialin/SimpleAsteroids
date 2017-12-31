package ntuple;

import bandits.BanditArray;
import bandits.BanditGene;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import evodef.SolutionEvaluator;
import evomaze.Constants;
import evomaze.MazeView;
import evomaze.ShortestPathTest;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by simonmarklucas on 27/05/2016.
 */
public class ConvMazeTest {
    int nDimenions;
    int mValues = 2;
    BanditArray genome;
    static Random rand = new Random();
    // static double noiseStdDev = 0.78 / Math.sqrt(2);
    static double noiseStdDev = 0.1;

    SolutionEvaluator evaluator;


    // 50000 gives good results on a 20x20 grid
    // set lower to see some poor examples

    static int nEvals = 1000;


    static ConvNTuple convNTuple;
    static int reportFrequency = 10;

    public static void main(String[] args)  throws Exception {

        // the number of bandits is equal to the size of the array

        int nDimensions = Constants.nBits;
        System.out.println("n dimensions = " + nDimensions);

        int nTrials = 5;

        int imageSize = (int) Math.sqrt(nDimensions);

        int filterSize = 6;
        convNTuple = new ConvNTuple().setImageDimensions(imageSize, imageSize);
        convNTuple.setFilterDimensions(filterSize, filterSize);
        convNTuple.setStride(2).setMValues(2);
        convNTuple.makeIndicies();

        ConvMazeTest convMazeTest = new ConvMazeTest(nDimensions);

        System.out.println(convMazeTest.runTrials(nDimensions, nTrials));
        // System.out.println("HashMap: " + convNTuple.ntMap);

    }

    static int[][] toSquareArray(int[] a) {
        int len = (int) Math.sqrt(a.length);
        int[][] m = new int[len][len];
        for (int i=0; i<a.length; i++) {
            m[i%len][i/len] = a[i];
        }
        return m;
    }

    static RankCorrelation rankCorrelation;

    static boolean createFiles = false;

    public StatSummary runTrials(int nDimenions, int nTrials) throws Exception {
        StatSummary ss = new StatSummary();

        ArrayList<int[][]> examples = new ArrayList<>();


        // System.out.println(examples);

        rankCorrelation = new RankCorrelation();


        for (int i=0; i<nTrials; i++) {

            evaluator = new ShortestPathTest();
            System.out.println("N DIMS = " + evaluator.searchSpace().nDims());
            ElapsedTimer t = new ElapsedTimer();
            NTupleBanditEA nTupleBanditEA = new NTupleBanditEA().setKExplore(nDimenions);

            nTupleBanditEA.setModel(convNTuple);
            // nTupleBanditEA.s
            int[] solution = nTupleBanditEA.runTrial(evaluator, nEvals);
            System.out.println("Solution array: " + Arrays.toString(solution));
            System.out.println(t);
            double fitness = evaluator.evaluate(solution);
            ss.add(fitness);
            System.out.println("Checking fitness: " + fitness);
            examples.add(toSquareArray(solution));
            System.out.println("Rank correlation check:");
            // rankCorrelation.rankCorrelation();

            convNTuple.report(evaluator);
            MazeView.showMaze(solution, "" + fitness);

        }

        if (createFiles) {
            System.out.println("Created mazes");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String out = gson.toJson(examples);
            System.out.println("Created JSON String");

            // System.out.println(out);

            String outputFile = "data/mazes.json";
            PrintWriter writer = new PrintWriter(outputFile);

            writer.print(out);
            writer.close();

            System.out.println("Wrote file with " + examples.size() + " examples");
        }

        return ss;

    }



    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    boolean success;
    int trialsSoFar;

    public ConvMazeTest(int nDimenions) {
        this.nDimenions = nDimenions;
        // Constants.nBits = nDimenions;
    }

    static int index = 0;

}
