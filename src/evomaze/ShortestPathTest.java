package evomaze;

import evodef.EvolutionLogger;
import evodef.SearchSpace;
import evodef.SolutionEvaluator;
import graph.Graph;
import graph.GraphBuilder;
import graph.ShortestPath;
import graph.Vertex;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

import static evomaze.Constants.*;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class ShortestPathTest implements SolutionEvaluator, SearchSpace {
    public static void main(String[] args) {
        int n = 225;

        int nReps = 30;

        StatSummary ssTimes = new StatSummary();
        StatSummary ssResults = new StatSummary();

        for (int i=0; i<nReps; i++) {
            ElapsedTimer et = new ElapsedTimer();
            Integer shortest = runOnce(n);
            if (shortest != null)
                ssResults.add(shortest);
            ssTimes.add(et.elapsed());
        }

        System.out.println("Results");
        System.out.println(ssResults);
        System.out.println("Times");
        System.out.println(ssTimes);

    }

    public static Integer runOnce(int n) {
        int[] bits = new int[n];
        Random random = new Random();
        double pOne = 0.3;
        for (int i=0; i<n; i++) {
            bits[i] = (random.nextDouble() < pOne) ? 1 : 0;
        }

        return new ShortestPathTest().findShortestPath(bits);

    }

    public Integer findShortestPath(int[] bits) {
        MazeModel mazeModel = new MazeModel(bits);

        Graph graph = new GraphBuilder().buildGraph(mazeModel);

        // System.out.println(graph.getArcs());

        // System.out.println("Finding shortest path:");

//        Vertex from = new Vertex(0, 0);
//        Vertex to = new Vertex(5, 5);

        ShortestPath shortestPath = new ShortestPath();
        Integer result = shortestPath.runShortestPath(graph, from, to);
        // System.out.println("Result = " + result);

        // shortestPath.printTrace(from, to);

        // System.out.println(shortestPath.getPath(from, to));

        return result;
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public double evaluate(int[] bits) {
        Integer shortestPath = findShortestPath(bits);

        int fitness;
        if (shortestPath == null) fitness = 0;
        else fitness = shortestPath;

        // add the fitness to the model

        logger.log(fitness, bits, false);

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

    EvolutionLogger logger = new EvolutionLogger();
    @Override
    public EvolutionLogger logger() {
        return logger;
    }

    @Override
    public Double optimalIfKnown() {
        return null;
    }

    @Override
    public int nDims() {
        return Constants.nBits;
    }

    @Override
    public int nValues(int i) {
        // always two possible values (wall or not wall) for anywhere in the maze
        return 2;
    }
}
