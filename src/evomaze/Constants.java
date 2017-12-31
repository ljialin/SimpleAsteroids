package evomaze;

import graph.Vertex;

/**
 * Created by simonmarklucas on 07/08/2016.
 */
public interface Constants {
    Vertex from = new Vertex(1, 1);
    Vertex to = new Vertex(4, 4);


    int nBits = (to.x + 2) * (to.y + 2);
}
