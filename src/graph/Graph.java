package graph;

import java.util.*;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class Graph {

    // when a Vertex has no successors we'll return a fixed
    // empty list instead of creating a new one each time
    final static List<Arc> emptyList = new ArrayList<Arc>();

    Map<Vertex,List<Arc>> arcs;
    HashSet<Vertex> vertices;

    public Graph() {
        arcs = new HashMap<Vertex, List<Arc>>();
        vertices = new HashSet<>();
    }

//    public Vertex getVertex(int i, int j) {
//        Vertex probe = new Vertex(i, j);
//        if (vertices.contains(probe));
//    }

    public void add(Arc arc) {
        List<Arc> out = arcs.get(arc.from);

        if (out == null) {
            // create a new successor list if this is the first time we've seen this vertex as a starting segment
            out = new ArrayList<Arc>();
            // System.out.println("Created new Successor list for " + arc.from);
            arcs.put(arc.from, out);
        }
        out.add(arc);
    }

    public void addOneWay(Vertex v1, Vertex v2) {
        add(new Arc(v1, v2, 1));
    }

    public void addTwoWay(Vertex v1, Vertex v2) {
        add(new Arc(v1, v2, 1));
        add(new Arc(v2, v1, 1));
    }


    public List<Arc> next(Vertex v) {
        List<Arc> list = arcs.get(v);
        if (list == null) {
            return emptyList;
        } else {
            return list;
        }
    }

    public List<Arc> getArcs() {
        ArrayList<Arc> a = new ArrayList<Arc>();
        for (List<Arc> la : arcs.values()) {
            a.addAll(la);
        }
        return a;

    }

    public Set<Vertex> getVertices() {
        return arcs.keySet();
    }
}
