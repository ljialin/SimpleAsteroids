package evomaze;

import graph.Vertex;

/**
 * Created by simonmarklucas on 07/08/2016.
 */
public interface Constants {
    Vertex from = new Vertex(0, 0);
    Vertex to = new Vertex(5, 5);


    int nBits = (to.x + 1) * (to.x + 1);
}
