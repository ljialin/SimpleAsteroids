package evomaze;

import graph.GraphBuilder;
import graph.ShortestPath;
import graph.Vertex;

import java.util.List;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class MazeModel {

    int[] bits;

    public int width;
    public int height;

    Vertex from, to;
    List<Vertex> path;

    public MazeModel(int[] bits) {
        this.bits = bits;
        width = (int) Math.sqrt(bits.length);
        height = width;
    }

    public List<Vertex> findShortestPath(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
        ShortestPath shortestPath = new ShortestPath();
        shortestPath.runShortestPath(new GraphBuilder().buildGraph(this), from, to);
        path = shortestPath.getPath(from, to);
        return path;
    }

    // now need to find the neighbours of each location

    /**
     *
     * @param x - x location in maze
     * @param y - y location in maze
     * @return  true if (x,y) location in maze is free
     */
    public boolean free(int x, int y) {
        x = (x + width) % width;
        y = (y + height) % height;
        return bits[x + y * width] == 0;
    }






}
