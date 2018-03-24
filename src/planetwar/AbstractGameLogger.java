package planetwar;


import logger.sample.DefaultLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: a logger for abstract games!
 * The class itself is not abstract ...
 */
public class AbstractGameLogger implements SimplePlayerInterface {

    DefaultLogger defaultLogger = new DefaultLogger();
    List<SimplePlayerInterface> shadows = new ArrayList<>();

    SimplePlayerInterface agent;

    public AbstractGameLogger setAgent(SimplePlayerInterface agent) {
        this.agent = agent;
        return this;
    }

    public AbstractGameLogger addShadow(SimplePlayerInterface agent) {
        shadows.add(agent);
        return this;
    }

    @Override
    public int getAction(AbstractGameState gameState, int playerId) {
        int action = agent.getAction(gameState.copy(), playerId);
        defaultLogger.logAction(action);
        // note that this is the score BEFORE the action has been taken
        defaultLogger.logScore(gameState.getScore());
        logShadowResponses(gameState, action, playerId);
        return action;
    }

    private void logShadowResponses(AbstractGameState gameState, int action, int playerId) {
        // iterate over all the shadow agents, collecting their responses
        // put the main agent response at the start of the list

        ArrayList<Integer> actions = new ArrayList<>();
        actions.add(action);
        // add actions from each shadowing agent
        for (SimplePlayerInterface shadowAgent : shadows) {
            actions.add(shadowAgent.getAction(gameState.copy(), playerId));
        }
        // now add all the actions that have been logged
        defaultLogger.logShadowActions(actions);


    }


    public String toString() {
        return agent.toString() + "\n" + defaultLogger.toString();
    }
}
