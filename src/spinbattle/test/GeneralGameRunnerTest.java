package spinbattle.test;

import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import ggi.agents.EvoAgentFactory;
import ggi.core.GameRunner;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.SpinGameState;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.SpinBattleParams;
import utilities.ElapsedTimer;

public class GeneralGameRunnerTest {
    public static void main(String[] args) {
        ElapsedTimer t = new ElapsedTimer();

        int maxTicks = 2000;
        GameRunner runner = new GameRunner().setLength(maxTicks);
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = new RandomAgent();
        EvoAgentFactory evoAgentFactory = new EvoAgentFactory();
        evoAgentFactory.useShiftBuffer = true;
        EvoAgent evoAgent = evoAgentFactory.getAgent();
        evoAgent.setSequenceLength(100);
        runner.setPlayers(evoAgent, p2);
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;
        runner.setGameFactory(factory);
        // runner.ga

        // todo: work out why this fails
        // runner.playGames(20);

        // todo: while this one works
        for (int i=0; i<100; i++) {
            factory.params.getRandom().setSeed(i);
            EvoAgent evoAgent2 = evoAgentFactory.getAgent();
            evoAgent2.setSequenceLength(50);
            EvoAgent evoAgent3 = evoAgentFactory.getAgent().setSequenceLength(200);
            runner.setPlayersWithoutReset(evoAgent2, evoAgent3);
            runner.playGame();
            System.out.println(runner.p1Wins + "\t " + runner.p2Wins);
            System.out.println();
        }

        runner.plotGameScores();
        System.out.println(runner.scores);
    }
}
