package logger.sample;

import logger.core.CorrelationLogger;
import logger.core.TrajectoryLogger;
import logger.util.EntropyLogger;
import logger.view.TrajectoryView;
import spinbattle.view.ParticleEffect;
import utilities.JEasyFrame;
import utilities.SideStack;

import java.awt.*;
import java.util.ArrayList;

public class DefaultLogger {
    EntropyLogger actionEntropy = new EntropyLogger().setName("Action Entropy");
    EntropyLogger scoreEntropy = new EntropyLogger().setName("Score Entropy");
    CorrelationLogger correlationLogger = new CorrelationLogger();
    TrajectoryLogger trajectoryLogger = new TrajectoryLogger();
    public SideStack<ParticleEffect> effects = new SideStack<>();

    public DefaultLogger logAction(int action) {
        actionEntropy.log(action);
        return this;
    }

    public DefaultLogger logEffect(ParticleEffect effect) {
        effects.push(effect);
        // System.out.println(effects.stackString());
        return this;
    }

    public TrajectoryLogger getTrajectoryLogger() {
        return trajectoryLogger;
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
        if (correlationLogger != null) {
            sb.append(correlationLogger);
        }
        return sb.toString();
    }

    public void logShadowActions(ArrayList<Integer> actions) {
        // now do what?
        correlationLogger.log(actions);
    }

    public DefaultLogger showTrajectories(int width, int height, String title) {

        TrajectoryView tv = new TrajectoryView();
        tv.setTrajectories(trajectoryLogger.trajectories).
                setDimension(new Dimension(width, height));
        tv.commonStartPoint = false;
        new JEasyFrame(tv, title);
        return this;
    }
}
