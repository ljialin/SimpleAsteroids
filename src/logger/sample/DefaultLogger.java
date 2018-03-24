package logger.sample;

import logger.core.CorrelationLogger;
import logger.util.EntropyLogger;

import java.util.ArrayList;

public class DefaultLogger {
    EntropyLogger actionEntropy = new EntropyLogger().setName("Action Entropy");
    EntropyLogger scoreEntropy = new EntropyLogger().setName("Score Entropy");
    CorrelationLogger correlationLogger = new CorrelationLogger();

    public DefaultLogger logAction(int action) {
        actionEntropy.log(action);
        return this;
    }

    public DefaultLogger logScore(double score) {
        scoreEntropy.log(score);
        return this;
    }

    public DefaultLogger report() {
        System.out.println(actionEntropy);
        System.out.println(scoreEntropy);
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Action entropy: %.3f\n", actionEntropy.entropy()));
        sb.append(String.format("Score entropy: %.3f\n", scoreEntropy.entropy()));
        sb.append("\n");
        sb.append(correlationLogger);
        return sb.toString();
    }

    public void logShadowActions(ArrayList<Integer> actions) {
        // now do what?
        correlationLogger.log(actions);
    }
}
