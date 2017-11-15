package asteroids;

import evogame.DefaultParams;
import evogame.GameParameters;
import utilities.ElapsedTimer;

public class EvoAgentTest {

    // todo: fix an error where whne an Asteroid is hit, it all goes crazy
    // like it fails to remove the dead ones
    // which is actually exactly what does happen!!

    public static void main(String[] args) {
        ElapsedTimer timer = new ElapsedTimer();

        boolean visible = false;
        int nTicks = 20000;
        int startLevel = 1;
        int nLives = 3000;

        GameParameters params = new GameParameters().injectValues(new DefaultParams());
        GameState gameState = new GameState().setParams(params);
        gameState.initialLevel = 3;
        Game game = new Game(gameState, visible);
        gameState.forwardModel.nLives = nLives;

        Game.copyTest = false;

        ElapsedTimer t = new ElapsedTimer();
        game.run(nTicks);
        System.out.println(t);

        System.out.println(timer);

    }
}
