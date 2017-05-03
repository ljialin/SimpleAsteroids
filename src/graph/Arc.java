package graph;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class Arc {
    public Vertex from;
    public Vertex to;
    public int dist;

    public Arc(Vertex from, Vertex to, int dist) {
        this.from = from;
        this.to = to;
        this.dist = dist;
    }

    public String toString() {
        return from + " -> " + to;
    }
}
