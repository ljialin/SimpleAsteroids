package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import ggi.agents.SimpleEvoAgent;
import utilities.ElapsedTimer;

public class GameSpeedTest {

    // Note: use RunAsteroidsDemo for visual version
    // this just runs a speed test using a simple SFP agent

    public static void main(String[] args) {
        ElapsedTimer timer = new ElapsedTimer();

        boolean visible = false;
        int nTicks = 1000;
        int startLevel = 1;
        int nLives = 3;

//        Game.seqLength = 100;
//        Game.nEvals = 20;

        GameParameters params = new GameParameters().injectValues(new DefaultParams());
        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params);
        gameState.initialLevel = 5;
        Game game = new Game(gameState, visible);
        gameState.forwardModel.nLives = nLives;

        Game.copyTest = false;

        ElapsedTimer t = new ElapsedTimer();
        // game.controller = game.getEvoAgent();
        SimpleEvoAgent evoAgent = new SimpleEvoAgent();
        evoAgent.flipAtLeastOneValue = true;
        evoAgent.sequenceLength = 100;
        evoAgent.useShiftBuffer = true;
        evoAgent.expectedMutations = 10;

        game.controller = new EvoAgentAdapter().setAgent(evoAgent);
        game.run(nTicks);

        gameState = gameState.copy();
        // gameState.
        System.out.println("Total game ticks: " + ForwardModel.totalTicks);
        long elapsed = t.elapsed();
        System.out.println(ForwardModel.totalTicks / elapsed + "k ticks per second");
        System.out.println("nTicks:\t " + gameState.nTicks());
        System.out.println("fTicks:\t " + gameState.forwardModel.nTicks);
        System.out.println("t(ms):\t " + t);

        System.out.println(timer);
        System.out.println("Score:\t " + gameState.getScore());
        System.out.println(gameState.isTerminal());

    }
}
