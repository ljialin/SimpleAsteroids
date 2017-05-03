package gvglink;

import battle.BattleGameParameters;
import battle.BattleView;
import core.player.AbstractPlayer;
import evodef.*;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by simonmarklucas on 24/10/2016.
 */
public class EvalBattleGame implements SolutionEvaluator {

    EvolutionLogger logger;
    EvoVectorSet searchSpace;

    public EvalBattleGame() {
        logger = new EvolutionLogger();
        searchSpace = BattleGameSearchSpace.getSearchSpace();
    }

    @Override
    public void reset() {
        logger.reset();
    }

    @Override
    public double evaluate(int[] p) {

        // this is where we inject the parameters and run the game ...

        // unfortunately this is an ugly bit
        // where the selected parameters are injected one by one to make the game

        BattleGameParameters.inject(searchSpace, p);


        // BattleGameParameters.inject(searchSpace, SearchSpaceUtil.randomPoint(searchSpace));
        // now run the darn thing

        int nGames = 1;
        double score = runGames(nGames);
        // optimal probably has no meaning for this space
        boolean optimal = false;
        logger.log(score, p, false);

        return score;

    }

    private double runGames(int nGames) {
        StatSummary ss = new StatSummary();
        for (int i=0; i<nGames; i++) {
            ss.add(runGame());
        }
        return ss.mean();
    }

    public double runGame() {
        // for now just run the darn game


        SpaceBattleLinkState linkState = new SpaceBattleLinkState();
        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        AbstractPlayer player;

        player = new controllers.singlePlayer.discountOLMCTS.Agent(linkState, timer);

        int thinkingTime = 20; // in milliseconds
        int delay = 30;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);
        // check that we can play the game

        int nSteps = 300;

        for (int i=0; i<nSteps && !linkState.isGameOver(); i++) {

            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);
            Types.ACTIONS action = player.act(linkState.copy(), timer);

            // use this for a random action
            // action = actions.get(random.nextInt(actions.size()));
            // System.out.println("Selected: " + action); //  + "\t " + action.ordinal());
            linkState.advance(action);
            // System.out.println(linkState.getGameScore());
        }

        System.out.println("Game score = " + linkState.getGameScore());
        // System.out.println(linkState.isGameOver());


        return linkState.getGameScore();
    }


    @Override
    public Double optimalIfKnown() {
        return null;
    }


    @Override
    public boolean optimalFound() {
        return false;
    }

    @Override
    public SearchSpace searchSpace() {
        return searchSpace;
    }

    @Override
    public int nEvals() {
        return logger.nEvals();
    }

    @Override
    public EvolutionLogger logger() {
        return logger;
    }

}
