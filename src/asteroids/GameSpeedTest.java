package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import utilities.ElapsedTimer;

public class GameSpeedTest {

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

        GameParameters params = new GameParameters().injectValues(new DefaultParams());
        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params);
        gameState.initialLevel = 5;
        Game game = new Game(gameState, visible);
        gameState.forwardModel.nLives = nLives;

        Game.copyTest = true;

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);
        System.out.println(t);

        System.out.println(timer);

    }
}
