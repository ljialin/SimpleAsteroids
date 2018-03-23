package planetwar;


import logger.sample.DefaultLogger;

/**
 * Note: a logger for abstract games!
 * The class itself is not abstract ...
 */
public class AbstractGameLogger implements SimplePlayerInterface {

    DefaultLogger defaultLogger = new DefaultLogger();

    SimplePlayerInterface agent;

    public AbstractGameLogger setAgent(SimplePlayerInterface agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        int action = agent.getAction(gameState, playerId);
        defaultLogger.logAction(action);
        defaultLogger.logScore(gameState.getScore());
        return action;
    }

    public String toString() {
        return agent.toString() + "\n" + defaultLogger.toString();
    }
}
