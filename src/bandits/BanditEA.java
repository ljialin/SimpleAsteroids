package bandits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import evodef.SolutionEvaluator;
import evomaze.MazeView;
import evomaze.ShortestPathTest;
import ntuple.ConvNTuple;
import ntuple.RankCorrelation;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by simonmarklucas on 27/05/2016.
 */
public class BanditEA {
    int nBandits;
    BanditArray genome;
    static Random rand = new Random();
    // static double noiseStdDev = 0.78 / Math.sqrt(2);
    static double noiseStdDev = 0.1;

    SolutionEvaluator evaluator = null;


    // 50000 gives good results on a 20x20 grid
    // set lower to see some poor examples

    static int nEvals = 10000;


    static ConvNTuple convNTuple;
    static int reportFrequency = 10;

    public static void main(String[] args)  throws Exception {

        // the number of bandits is equal to the size of the array
        int nBandits = 400;

        int nTrials = 10;

        int imageSize = (int) Math.sqrt(nBandits);

        int filterSize = 4;
        convNTuple = new ConvNTuple().setImageDimensions(imageSize, imageSize);
        convNTuple.setFilterDimensions(filterSize, filterSize);
        convNTuple.setStride(2).setMValues(2);
        convNTuple.makeIndicies();


        System.out.println(runTrials(nBandits, nTrials));

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

    public static StatSummary runTrials(int nBandits, int nTrials) throws Exception {
        StatSummary ss = new StatSummary();

        ArrayList<int[][]> examples = new ArrayList<>();


        // System.out.println(examples);

        rankCorrelation = new RankCorrelation();


        for (int i=0; i<nTrials; i++) {

            BanditEA ea = new BanditEA(nBandits);

            ea.evaluator = new ShortestPathTest();

            ElapsedTimer t = new ElapsedTimer();
            ea.run(nEvals);
            System.out.println(t);
            if (ea.success) {
                // ss.add(ea.trialsSoFar);
            }
            ss.add(ea.evaluate(ea.genome));
            System.out.println("Checking fitness: " + ea.evaluate(ea.genome));
            examples.add(toSquareArray(ea.genome.toArray()));

            System.out.println("Rank correlation check:");
            rankCorrelation.rankCorrelation();

        }

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



        return ss;

    }



    // use these instance variables to track whether
    // a run is successful and the number of evaluations used
    boolean success;
    int trialsSoFar;

    public BanditEA(int nBandits) {
        this.nBandits = nBandits;
        genome = new BanditArray(nBandits);
    }

    public BanditArray run(int nEvals) {

        double bestYet = evaluate(genome);
        success = false;
        // will make 2 trials each time around the loop
        for (trialsSoFar = 2; trialsSoFar<=nEvals; trialsSoFar+=2) {

            // each evaluation, make a mutation
            // measure the fitness
            // and feed it back

            // Jialin pointed out that the trials so far
            // is actually twice the number of iterations
            // but this is fixable to be the same in the case of

            BanditGene gene = genome.selectGeneToMutate(trialsSoFar);

            double before = evaluate(genome);
            gene.mutate();
            double after = evaluate(genome);

            double delta = after - before;

            double noise = rand.nextGaussian() * noiseStdDev;
            delta += noise;

            gene.applyReward(delta);
            gene.revertOrKeep(delta);

            bestYet = Math.max(before, after);

            // System.out.println(trialsSoFar + "\t " + bestYet);
            // System.out.println(countOnes(genome.toArray()) + " \t " + Arrays.toString(genome.toArray()));

            if (bestYet == nBandits) {
                System.out.println("Optimum found after " + trialsSoFar + " evals");
                success = true;
                break;
            }
        }

        System.out.println("Best solution: " + bestYet);

        String title = "BanditEA: " + evaluate(genome);
        MazeView.showMaze(genome.toArray(), title);

        return genome;
    }

    // this method is done as a sanity check to ensure
    // that the algorithm is actually working and returns
    // the correct array of values
    public int countOnes(int[] a) {
        int tot = 0;
        for (int i=0; i<a.length; i++) {
            tot += a[i];
        }
        return tot;
    }



    static int blockSize = 8;
    // block size MUST be a multiple of the genome length
    public double evaluate(BanditArray genome) {
        if (evaluator!= null) return externalEvaluation(genome);
        double tot = 0;
        int ix = 0;
        while (ix < genome.genome.size()) {
            // directly access the current value field of each bandit
            boolean block = true;
            for (int j=0; j<blockSize; j++) {
                if (genome.genome.get(ix).x != 1)
                    block = false;
                ix++;
            }
            if (block) {
                tot += blockSize;
            }
        }
        return tot;
    }

    static int index = 0;



    private double externalEvaluation(BanditArray genome) {
//        int[] bits = new int[genome.nBandits];
//
//        for (int i=0; i<genome.nBandits; i++) {
//            bits[i] = genome.genome.get(i).x;
//        }
//



        int[] x = genome.toArray();
        double value = evaluator.evaluate(x);

        // we want to add this, but also do some more...


        if (convNTuple.nSamples() % reportFrequency == 0) {
            double estimate = convNTuple.getMeanEstimate(x);

            // the line below is to check that we get a rank correlation close to zero when
            // using a random estimate
            // would normally be commented out
            // estimate = Math.random();

            rankCorrelation.add(index++, value, estimate);

//            System.out.format("True value v. estimate:  %.1f\t %.1f\n", value, estimate);
//            System.out.println("Exploration stats: ");
//            System.out.println(convNTuple.getNoveltyStats(x));
//            System.out.println();
        }

        convNTuple.addPoint(x, value);


        // System.out.println(Arrays.toString(bits));
        // System.out.println("Fitness = " + value);
        return value;


    }
}
