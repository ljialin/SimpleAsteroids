package graph;

import evomaze.MazeModel;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class GraphBuilder {

    static boolean wrap = false;

    static int[][] deltas = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

    public Graph buildGraph(MazeModel mazeModel) {

        Graph graph = new Graph();

        // this relies on the fact that
        for (int i = 0; i < mazeModel.width; i++) {
            for (int j = 0; j < mazeModel.height; j++) {
                // only add an arc if the from one is free
                // and the to one is free

                // and do them for each difference
                // can make new Vertices each time since
                // they have properly defined equals and hashcode methods
                if (mazeModel.free(i, j)) {
                    for (int[] delta : deltas) {

                        // allow for wrap around
                        if (wrap) {
                            int sx = (i + delta[0] + mazeModel.width) % mazeModel.width;
                            int sy = (j + delta[1] + mazeModel.height) % mazeModel.height;
                            if (mazeModel.free(sx, sy)) {
                                graph.addOneWay(new Vertex(i, j), new Vertex(sx, sy));
                            }
                        } else {
                            int sx = i + delta[0];
                            int sy = j + delta[1];
                            try {
                                if (mazeModel.free(sx, sy)) {
                                    graph.addOneWay(new Vertex(i, j), new Vertex(sx, sy));
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        }

        return graph;


    }
}