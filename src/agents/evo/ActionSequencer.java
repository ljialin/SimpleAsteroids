package agents.evo;

import agents.dummy.DoNothingAgent;
import evodef.PluginEvaluator;
import ggi.core.AbstractGameState;
import ggi.core.SimplePlayerInterface;
import plot.NullPlayoutPlotter;
import plot.PlayoutPlotterInterface;

public class ActionSequencer implements PluginEvaluator {

    public static void main(String[] args) {

//        // fix a random seed to check repeatability
//        Random random = new Random(3);
//        GameState.random = random;
//
//        GameState gameState = new GameState().defaultState();
//        int[] seq = new int[]{0, 1, 2, 3, 4, 0, 1, 2, 2, 3, 4, 0, 1, 2, 1, 2, 3};
//        System.out.println("Initial fitness: " + gameState.getScore());
//        ActionSequencer actionSequencer = new ActionSequencer().setGameState(gameState).setPlayerId(0);
//        double fitness = actionSequencer.fitness(seq);
//        System.out.println("Final fitness: " + fitness);
    }

    AbstractGameState initialState, terminalState;
    int playerId;

    // double
    PlayoutPlotterInterface playoutPlotter;

    public ActionSequencer() {
        // don't plot the playouts by default
        playoutPlotter = new NullPlayoutPlotter();
    }

    public ActionSequencer setGameState(AbstractGameState gameState) {
        // actually makes a copy of it to avoid modifying the original
        this.initialState = gameState.copy();
        return this;
    }

    public ActionSequencer setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    public AbstractGameState getTerminalState() {
        return terminalState;
    }

//    public ActionSequencer actVersusDoNothing(int[] seq, int ownedBy) {
//        // careful, this may not be copiing the game state ...
//        terminalState = initialState.copy();
//        int[] actions = new int[2];
//        for (int a : seq) {
//            actions[ownedBy] = a;
//            actions[1 - ownedBy] = GameState.doNothing;
//            terminalState.next(actions);
//        }
//        // System.out.println("Terminal score: " + terminalState.getScore());
//        return this;
//    }

    SimplePlayerInterface opponent = new DoNothingAgent();

    public ActionSequencer setOpponent(SimplePlayerInterface opponent) {
        this.opponent = opponent;
        return this;
    }

    static boolean exitLoopWhenGameOver = false;

    public ActionSequencer actVersusAgent(int[] seq, int playerId) {
        // careful, this may not be copiing the game state ...
        terminalState = initialState.copy();
        playoutPlotter.startPlayout(terminalState.getScore());

        int[] actions = new int[2];
        for (int a : seq) {
            actions[playerId] = a;
            actions[1 - playerId] = opponent.getAction(terminalState, 1-playerId);
            terminalState.next(actions);
            playoutPlotter.addScore(terminalState.getScore());
            if (exitLoopWhenGameOver && terminalState.isTerminal()) break;
        }
        // System.out.println("Terminal score: " + terminalState.getScore());
        playoutPlotter.plotPlayout();
        return this;
    }

    @Override
    public double fitness(int[] solution) {
        double rawScore = actVersusAgent(
                solution, playerId).terminalState.getScore();
        return playerId == 0 ? rawScore : -rawScore;
    }

}
