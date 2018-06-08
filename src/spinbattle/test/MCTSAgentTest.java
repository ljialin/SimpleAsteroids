package spinbattle.test;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import core.player.AbstractMultiPlayer;
import ggi.agents.EvoAgentFactory;
import ggi.core.AbstractGameState;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import gvglink.PlanetWarsLinkState;
import spinbattle.core.SpinGameState;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.GVGAIWrapper;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;


/**
 *  Interesting: against a uniform random player, MCTS does well for first 1,000 game ticks
 *  and then often falls apart!
 */

public class MCTSAgentTest {
    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        int maxTicks = 1000;
        GameRunnerTwoPlayer runner = new GameRunnerTwoPlayer().setLength(maxTicks);
        SimplePlayerInterface randomAgent = new RandomAgent();
        // SimplePlayerInterface p2 = new RandomAgent();

        SimplePlayerInterface doNothingAgent = new DoNothingAgent();
        EvoAgentFactory evoAgentFactory = new EvoAgentFactory();
        evoAgentFactory.useShiftBuffer = true;
        evoAgentFactory.mutationRate = 5;
        // evoAgentFactory.totalRandomMutation = true;
        EvoAgent evoAgent = evoAgentFactory.getAgent();
        // evoAgent.setVisual();
        evoAgent.setSequenceLength(200);
        // runner.setPlayers(evoAgent, doNothingAgent);

        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;
        runner.setGameFactory(factory);

        SimplePlayerInterface mctsAgent = getMCTSAgent(factory.newGame(), 1);

        // runner.ga
        // todo: work out why this fails
        // runner.playGames(20);

        // todo: while this one works
        for (int i=0; i<10; i++) {
            factory.params.getRandom().setSeed(i);
            // runner.setPlayersWithoutReset(randomAgent, evoAgent);
            runner.setPlayersWithoutReset(randomAgent, mctsAgent);
            runner.playGame();
            System.out.println(runner.p1Wins + "\t " + runner.p2Wins);
            System.out.println();
        }
        runner.plotGameScores();
        System.out.println(runner.scores);

    }

    public static GVGAIWrapper getMCTSAgent(AbstractGameState gameState, int playerId) {
        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        PlanetWarsLinkState linkState = new PlanetWarsLinkState(gameState);
        AbstractMultiPlayer agent =
                new controllers.multiPlayer.discountOLMCTS.Agent(linkState.copy(), timer, playerId);
        GVGAIWrapper wrapper = new GVGAIWrapper().setAgent(agent);
        return wrapper;

    }

}

