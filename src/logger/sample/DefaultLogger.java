package logger.sample;

import logger.core.ScalarLogger;
import logger.util.EntropyLogger;

public class DefaultLogger {
    EntropyLogger actionEntropy = new EntropyLogger().setName("Action Entropy");
    EntropyLogger scoreEntropy = new EntropyLogger().setName("Score Entropy");

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
        return sb.toString();
    }
}
