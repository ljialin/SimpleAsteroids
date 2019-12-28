package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import utilities.ElapsedTimer;

public class RunAsteroidsDemo {

    // todo: Fix the error that at the moment
    // the ship is not turning to avoid collisions
    // at all, depsite the fact that the collisions
    // are costly
    // why could that be?

    public static void main(String[] args) {
        ElapsedTimer timer = new ElapsedTimer();

        boolean visible = true;
        int nTicks = 1000;
        int startLevel = 1;
        int nLives = 3;

        Game.seqLength = 100;
        Game.nEvals = 20;

        GameParameters params = new GameParameters().injectValues(new DefaultParams());
        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params);
        gameState.initialLevel = 5;
        Game game = new Game(gameState, visible);
        gameState.forwardModel.nLives = nLives;

        Game.copyTest = false;

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);

        gameState = gameState.copy();
        // gameState.
        System.out.println(ForwardModel.totalTicks);
        System.out.println("nTicks:\t " + gameState.nTicks());
        System.out.println("fTicks:\t " + gameState.forwardModel.nTicks);
        System.out.println("t(ms):\t " + t);

        System.out.println(timer);
        System.out.println(gameState.getScore());
        System.out.println(gameState.isTerminal());

    }
}
