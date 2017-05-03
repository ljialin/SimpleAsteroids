package graph;

/**
 * Created by simonmarklucas on 06/08/2016.
 */
public class ScoredArc {
    Arc arc;
    int score;

    public ScoredArc(Arc arc, int score) {
        this.arc = arc;
        this.score = score;
    }

    public String toString() {
        return arc + " = " + score;
    }

    // not needed
//    public int hashCode() {
//        return arc.from.hashCode() * arc.to.hashCode() * score;
//    }


}
