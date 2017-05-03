package graph;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class Vertex {

    public int x, y;
    public int hash;

    // the use of
    static int maxSquareSize = 100;
    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        hash = x + maxSquareSize * y;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }


    public int hashCode() {
        return hash;
    }

    public boolean equals(Object ob) {
        // this only works when the hashcodes are unique
        // return hashCode() == ob.hashCode();
        return hash == ob.hashCode();
//        try {
//            Vertex v = (Vertex) ob;
//            return v.x == x && v.y == y;
//        } catch (Exception e) {return false;}
    }
}
