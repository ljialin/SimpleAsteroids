package logger.core;

import java.util.ArrayList;

public class ActionScoreLog {
    ArrayList<Integer> actions = new ArrayList<>();
    ArrayList<Double> scores = new ArrayList<>();

    public ActionScoreLog add(int action, double score) {
        actions.add(action);
        scores.add(score);
        return this;
    }

}
