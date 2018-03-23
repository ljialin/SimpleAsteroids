package planetwar;

import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import gvglink.PlanetWarsLinkState;
import ntuple.SlidingMeanEDA;
import tools.ElapsedCpuTimer;

public class GameLoggerTest {
    public static void main(String[] args) {

        GameState.includeBuffersInScore = false;
        GameRunner gameRunner = new GameRunner().setLength(200);

        SimplePlayerInterface p1, p2;

        p1 = new RandomAgent();
        p2 = new DoNothingAgent();

        EvoAlg evoAlg1 = new SimpleRMHC();
        int nEvals1 = 200;
        int seqLength1 = 10;

        EvoAgent evoAgent1 = new EvoAgent().setEvoAlg(evoAlg1, nEvals1).setSequenceLength(seqLength1);
        evoAgent1.setUseShiftBuffer(true);

        p1 = new AbstractGameLogger().setAgent(new DoNothingAgent());
        // p1 = new AbstractGameLogger().setAgent(evoAgent1);
        p2 = new AbstractGameLogger().setAgent(new RandomAgent());

        SimplePlayerInterface opponentModel;
        opponentModel = new RandomAgent();

        // p2 = getMCTSAgent(new GameState().defaultState(), 1);
        gameRunner.setPlayers(p1, p2);
        int nGames = 20;
        gameRunner.playGames(nGames);
        gameRunner.plotGameScores();

        System.out.println(p1);
        System.out.println(p2);
    }

    static GVGAIWrapper getMCTSAgent(GameState gameState, int playerId) {
        ElapsedCpuTimer timer = new ElapsedCpuTimer();
        PlanetWarsLinkState linkState = new PlanetWarsLinkState(gameState);
        AbstractMultiPlayer agent =
                new controllers.multiPlayer.discountOLMCTS.Agent(linkState.copy(), timer, playerId);
        GVGAIWrapper wrapper = new GVGAIWrapper().setAgent(agent);
        return wrapper;

    }
}
