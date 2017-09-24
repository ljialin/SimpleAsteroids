package agentopt;

import battle.BattleGameParameters;
import controllers.multiPlayer.ea.Agent;
import core.game.StateObservationMulti;
import core.player.AbstractMultiPlayer;
import evodef.*;
import ga.SimpleRMHC;
import gvglink.SpaceBattleLinkStateTwoPlayer;
import ntuple.CompactSlidingModelGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

public class AgentEvaluator implements NoisySolutionEvaluator {

    public static void main(String[] args) {
        int[] solution = new int[]{1, 1, 1, 1};

        AgentEvaluator eval = new AgentEvaluator();
        double result = eval.evaluate(solution);

        System.out.println(result);
    }

    @Override
    public Boolean isOptimal(int[] solution) {
        return false;
    }

    @Override
    public Double trueFitness(int[] solution) {
        return null;
    }

    int nEvals = 0;

    @Override
    public void reset() {
        logger.reset();
    }

    EvolutionLogger logger;
    AdaptiveEvoAgentSpace searchSpace;


    public AgentEvaluator() {
        logger = new EvolutionLogger();
        searchSpace = new AdaptiveEvoAgentSpace();

    }

    public String report(int[] solution) {
        return searchSpace.report(solution);
    }

    @Override
    public double evaluate(int[] solution) {

        // at thias point,

        System.out.println("Params are:");
        System.out.println(searchSpace.report(solution));

        // can also override parameters by setting them directly as follows:
        BattleGameParameters.loss = 0.996;
        BattleGameParameters.thrust = 3;
//        BattleGameParameters.shipSize *= 2;
//        BattleGameParameters.damageRadius *= 2;
        SpaceBattleLinkStateTwoPlayer linkState = new SpaceBattleLinkStateTwoPlayer();
        StateObservationMulti multi = linkState;

        GameActionSpaceAdapterMulti.useHeuristic = false;

        // Mutator.totalRandomChaosMutation = false;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        // AbstractMultiPlayer player2;



        int idPlayer1 = 0;
        int idPlayer2 = 1;

        // player2 = new controllers.multiPlayer.discountOLMCTS.Agent(linkState, timer, idPlayer2);

        // try the evolutionary players

        int nResamples = 2;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        double kExplore = searchSpace.getExplorationFactor(solution);
        int nNeighbours = 100;

        int nEvals = 100;
        evoAlg = new NTupleBanditEA(kExplore, nNeighbours);


        evoAlg = new SlidingMeanEDA().setHistoryLength(searchSpace.getHistoryLength(solution));

        Agent evoAgent = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg, idPlayer1, nEvals);
        evoAgent.setDiscountFactor(searchSpace.getDiscountFactor(solution));

        evoAgent.sequenceLength = searchSpace.getRolloutLength(solution);
        // evoAgent.di


        // EvoAlg evoAlg2 = new CompactSlidingModelGA().setHistoryLength(2);
        EvoAlg evoAlg2 = new SlidingMeanEDA().setHistoryLength(2);
        Agent player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer2, nEvals);
        player2.sequenceLength = 5;


        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, new SimpleRMHC(nResamples), idPlayer2, nEvals);

        // player1  = new controllers.multiPlayer.smlrand.Agent();

        // EvoAlg evoAlg2 = new SimpleRMHC(2);

        // player1 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer1, nEvals);


        int thinkingTime = 10; // in milliseconds
        int delay = 10;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        Random random = new Random();
        int nSteps = 500;

        ElapsedTimer t = new ElapsedTimer();

        StatSummary sst1 = new StatSummary("Player 1 Elapsed Time");
        StatSummary sst2 = new StatSummary("Player 2 Elapsed Time");

        StatSummary ssTicks1 = new StatSummary("Player 1 nTicks");
        StatSummary ssTicks2 = new StatSummary("Player 2 nTicks");

        for (int i = 0; i < nSteps && !linkState.isGameOver(); i++) {
            linkState.state = linkState.state.copyState();
            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);

            ElapsedTimer t1 = new ElapsedTimer();

            // keep track of the number of game ticks used by each algorithm
            int ticks;

            ticks = SpaceBattleLinkStateTwoPlayer.nTicks;
            Types.ACTIONS action1 = evoAgent.act(multi.copy(), timer);
            sst1.add(t1.elapsed());
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks - ticks;
            ssTicks1.add(ticks);
            // System.out.println("Player 1 Ticks = " + ticks);

            ElapsedTimer t2 = new ElapsedTimer();
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks;
            Types.ACTIONS action2 = player2.act(multi.copy(), timer);
            sst2.add(t2.elapsed());
            ticks = SpaceBattleLinkStateTwoPlayer.nTicks - ticks;
            ssTicks2.add(ticks);

            // System.out.println("Player 2 Ticks = " + ticks);
            multi.advance(new Types.ACTIONS[]{action1, action2});

        }

        System.out.println(multi.getGameScore());
        System.out.println(multi.isGameOver());

        // System.out.println(SingleTreeNode.rollOutScores);

        System.out.println(sst1);
        System.out.println(sst2);

        System.out.println(ssTicks1);
        System.out.println(ssTicks2);
        double score = multi.getGameScore(0);
        System.out.println("Game score: " + score);
        if (score > 0) return 1;
        if (score < 0) return -1;
        return 0;
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

    @Override
    public Double optimalIfKnown() {
        return null;
    }
}
