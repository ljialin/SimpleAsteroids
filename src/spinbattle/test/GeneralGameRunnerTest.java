package spinbattle.test;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import ggi.agents.EvoAgentFactory;
import ggi.core.GameRunner;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.FalseModelAdapter;
import spinbattle.core.SpinGameState;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.SpinBattleParams;
import utilities.ElapsedTimer;

public class GeneralGameRunnerTest {
    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        int maxTicks = 2000;
        GameRunner runner = new GameRunner().setLength(maxTicks);
        SimplePlayerInterface randomAgent = new RandomAgent();
        // SimplePlayerInterface p2 = new RandomAgent();

        SimplePlayerInterface doNothingAgent = new DoNothingAgent();
        EvoAgentFactory evoAgentFactory = new EvoAgentFactory();
        evoAgentFactory.useShiftBuffer = true;
        evoAgentFactory.mutationRate = 5;
        evoAgentFactory.totalRandomMutation = true;
        // evoAgentFactory.totalRandomMutation = true
        EvoAgent evoAgent = evoAgentFactory.getAgent();
        // evoAgent.setVisual();
        evoAgent.setSequenceLength(100);
        runner.setPlayers(evoAgent, doNothingAgent);
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;
        factory.params.transitSpeed = 0;

        runner.setGameFactory(factory);

        // runner.ga
        // todo: work out why this fails
        // runner.playGames(20);

        // todo: while this one works
        for (int i=0; i<1; i++) {
            factory.params.getRandom().setSeed(i);
//            evoAgent2.setSequenceLength(50);
            EvoAgent evoAgent3 = evoAgentFactory.getAgent().setSequenceLength(100);
            evoAgent3.nEvals = 10;

            // evoAgentFactory.totalRandomMutation = false;
            EvoAgent evoAgent1 = evoAgentFactory.getAgent().setSequenceLength(100);
            // evoAgent1.setVisual();
            // evoAgent1.useShiftBuffer = false;
            evoAgent1.nEvals = 20;

            SpinBattleParams params = new SpinBattleParams();
            // params.gravitationalFieldConstant *= 0;
            params.transitSpeed = 3;

            FalseModelAdapter falsePlayer = new FalseModelAdapter().setPlayer(evoAgent1).setParams(params);

            // runner.delay = 100;
            runner.setPlayersWithoutReset(doNothingAgent, falsePlayer);
            runner.playGame();
            System.out.println(runner.p1Wins + "\t " + runner.p2Wins);
            System.out.println();
        }

        runner.plotGameScores();
        System.out.println(runner.scores);
    }
}
