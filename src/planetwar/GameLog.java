package planetwar;

import utilities.StatSummary;

import java.util.ArrayList;

public class GameLog {
    public String name;
    public ArrayList<Double> scores = new ArrayList<>();
    public StatSummary diffs = new StatSummary();
    Double previous;
    public int leadChanges = 0;
    double initialGrowthRate;


    public GameLog setName(String name) {
        this.name = name;
        return this;
    }

    public GameLog setInitialGrowthRate(double initialGrowthRate) {
        this.initialGrowthRate = initialGrowthRate;
        // System.out.println("Initial growth rate: " + initialGrowthRate);
        return this;
    }

    public void addScore(double score) {
        scores.add(score);
        if (previous != null) {
            diffs.add(score - previous);
            // check of the lead has changed hands, defined by a zero crossing
            if (score * previous < 0)
                leadChanges++;
        }
        previous = score;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + "\n");
        sb.append("Game final score: " + previous + "\n");
        sb.append("Lead reversals: " + leadChanges + "\n");
        sb.append("Leader had advantage? " + leaderHadAdvantage() + "\n");
        return sb.toString();
    }

    public boolean leaderHadAdvantage() {
        return ((initialGrowthRate * previous) > 0);
    }

}
