package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class ShortestPath {

    // run something like Dijkstra's algorithm to find shortest
    // path

    Map<Vertex, ScoredArc> scores;
    Integer shortestPathDistance;
    ScoredArc backTrack = null;

    public Integer runShortestPath(Graph g, Vertex from, Vertex to) {
        // set these up here for future reference
        // the scores table can be used to recover the shortest path
        scores = new HashMap<>();
        shortestPathDistance = null;
        return shortestPath(g, from, to, 0);
    }

    public Integer shortestPath(Graph g, Vertex cur, Vertex to, int dist) {

        // build up a list of the quickest way to get to each of them

        // each time we find something new, propagate it

        // and also store the best way to get there


        // if (shortestPathDistance != null) return shortestPathDistance;
        for (Arc arc : g.next(cur)) {
            int distTo = dist + arc.dist;
            // naming this makes the code easier to understand
            Vertex next = arc.to;
            if (next.equals(to)) {
                // found destination
                if (shortestPathDistance == null || distTo < shortestPathDistance) {
                    shortestPathDistance = distTo;
                    scores.put(to, new ScoredArc(arc, distTo));

                }
                // shortestPathDistance = distTo;
                // System.out.println("###################### Shortest path = " + shortestPathDistance);
                // return shortestPathDistance;
            }
            // keep looking - is this useful information?
            else {
                ScoredArc bestYet = scores.get(next);
                if (bestYet == null || distTo < bestYet.score) {

                    // new best score to reach this vertex
                    bestYet = new ScoredArc(arc, distTo);
                    // System.out.println("New scored arc: " + bestYet);
                    // System.out.println(scores);
                    scores.put(next, bestYet);
                    shortestPath(g, next, to, distTo);


                } else {
                    // System.out.println("Already had best way: " + bestYet);
                }
            }

        }

        return shortestPathDistance;

    }

    public void printTrace(Vertex from, Vertex to) {
        // just show the contents of the arcs
        for (Vertex v : scores.keySet()) {
            if (v.equals(from)) System.out.println("From:");
            if (v.equals(to)) System.out.println("To: ");
            System.out.println(v + " : " + scores.get(v));
        }
    }

    public List<Vertex> getPath(Vertex from, Vertex cur) {
        ArrayList<Vertex> path = new ArrayList<>();
        path.add(cur);
        // work backwards from destination to the start
        try {
            while (!cur.equals(from)) {
                Arc arc = scores.get(cur).arc;
                cur = arc.from;
                path.add(cur);
            }
        } catch (Exception e) {

        }
        return path;
    }
}
