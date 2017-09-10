package ga;

/**
 * Created by Simon Lucas on 22/06/2017.
 */
public class ScoredVec implements Comparable<ScoredVec> {
    public int[] p;
    public Double score;

    public ScoredVec(int[] p, double score) {
        this.p = p;
        this.score = score;
    }

    public ScoredVec(int[] p) {
        this.p = p;
    }


    @Override
    public int compareTo(ScoredVec o) {
        return -score.compareTo(o.score);
    }
}
