package planetwar;

import evodef.PluginEvaluator;

import java.util.Random;

public class ActionSequencer implements PluginEvaluator {

    public static void main(String[] args) {

        // fix a random seed to check repeatability
        Random random = new Random(3);
        GameState.random = random;

        GameState gameState = new GameState().defaultState();
        int[] seq = new int[]{0, 1, 2, 3, 4, 0, 1, 2, 2, 3, 4, 0, 1, 2, 1, 2, 3};
        System.out.println("Initial fitness: " + gameState.getScore());
        ActionSequencer actionSequencer = new ActionSequencer().setGameState(gameState).setPlayerId(0);
        double fitness = actionSequencer.fitness(seq);
        System.out.println("Final fitness: " + fitness);
    }

    GameState initialState, terminalState;
    int playerId;

    // double

    public ActionSequencer setGameState(GameState gameState) {
        // actually makes a copy of it to avoid modifying the original
        this.initialState = gameState.copy();
        return this;
    }

    public ActionSequencer setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }

    public GameState getTerminalState() {
        return terminalState;
    }

    public ActionSequencer actVersusDoNothing(int[] seq, int playerId) {
        // careful, this may not be copiing the game state ...
        terminalState = initialState.copy();
        int[] actions = new int[2];
        for (int a : seq) {
            actions[playerId] = a;
            actions[1 - playerId] = GameState.doNothing;
            terminalState.next(actions);
        }
        // System.out.println("Terminal score: " + terminalState.getScore());
        return this;
    }

    SimplePlayerInterface opponent = new DoNothingAgent();

    public ActionSequencer setOpponent(SimplePlayerInterface opponent) {
        this.opponent = opponent;
        return this;
    }

    public ActionSequencer actVersusAgent(int[] seq, int playerId) {
        // careful, this may not be copiing the game state ...
        terminalState = initialState.copy();
        int[] actions = new int[2];
        for (int a : seq) {
            actions[playerId] = a;
            actions[1 - playerId] = GameState.doNothing;
            terminalState.next(actions);
        }
        // System.out.println("Terminal score: " + terminalState.getScore());
        return this;
    }

    @Override
    public double fitness(int[] solution) {
        // double rawScore = actVersusDoNothing(solution, playerId).terminalState.getScore();
        // should this be negated for the minimising player
        double rawScore = actVersusAgent(solution, playerId).terminalState.getScore();
        return playerId == 0 ? rawScore : -rawScore;
    }
}
