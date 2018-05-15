package planetwar;

import agents.dummy.RandomAgent;
import asteroids.AsteroidsGameState;
import evogame.DefaultParams;
import evogame.GameParameters;
import ggi.core.SimplePlayerInterface;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class RandomTestAsteroids {
    public static void main(String[] args) {

        int nTrials = 100;

        SimplePlayerInterface player = new RandomAgent();

        StatSummary ss = new StatSummary("Random Player on Asteroids");
        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<nTrials; i++) {
            ss.add(evaluate(player));
        }

        System.out.println(ss);
        System.out.println();
        System.out.println(t);

    }

    static GameParameters params = new GameParameters().injectValues(new DefaultParams());

    static int maxTick = 1000;

    public static double evaluate(SimplePlayerInterface player) {

        // create a problem to evaluate this one on ...
        // this should really be set externally, but just doing it this way for now

        AsteroidsGameState gameState = new AsteroidsGameState().setParams(params).initForwardModel();


        for (int i=0; i<maxTick; i++) {
            int action = player.getAction(gameState, 0);
            gameState.next(new int[]{action});
        }
        double fitness = gameState.getScore();

        return fitness;
    }


}
