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

        int maxTicks = 1000;
        GameRunner runner = new GameRunner().setLength(maxTicks);
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = new RandomAgent();
        EvoAgentFactory evoAgentFactory = new EvoAgentFactory();
        evoAgentFactory.useShiftBuffer = true;
        EvoAgent evoAgent = evoAgentFactory.getAgent();
        evoAgent.setSequenceLength(100);
        runner.setPlayers(evoAgent, p2);
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.getRandom().setSeed(50);
        factory.params.maxTicks = maxTicks;
        runner.setGameFactory(factory);
        // runner.ga

        // todo: work out why this fails
        // runner.playGames(20);

        // todo: while this one works
        for (int i=0; i<3; i++) {
            EvoAgent evoAgent2 = evoAgentFactory.getAgent();
            evoAgent.setSequenceLength(100);
            runner.setPlayersWithoutReset(evoAgent2, p2);
            runner.playGame();
        }

        runner.plotGameScores();
        System.out.println(runner.scores);
    }
}
