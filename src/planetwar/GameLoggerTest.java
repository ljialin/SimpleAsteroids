package planetwar;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ggi.SimplePlayerInterface;

public class GameLoggerTest {
    public static void main(String[] args) {

        System.out.println("n Actions: " + GameState.nActions);
        GameState.includeBuffersInScore = false;
        GameRunner gameRunner = new GameRunner().setLength(50);

        SimplePlayerInterface p1, p2;

        p1 = new RandomAgent();
        p2 = new DoNothingAgent();

        EvoAlg evoAlg1 = new SimpleRMHC();
        int nEvals1 = 500;
        int seqLength1 = 15;

        EvoAgent evoAgent1 = new EvoAgent().setEvoAlg(evoAlg1, nEvals1).setSequenceLength(seqLength1);
        evoAgent1.setUseShiftBuffer(false);

        EvoAgent evoAgentShadow1 = new EvoAgent().setEvoAlg(new SimpleRMHC(), nEvals1).setSequenceLength(seqLength1);
        evoAgentShadow1.setUseShiftBuffer(false);

        EvoAgent evoAgentShadow2 = new EvoAgent().setEvoAlg(new SimpleRMHC(), nEvals1).setSequenceLength(3);
        evoAgentShadow2.setUseShiftBuffer(false);

        AbstractGameLogger abstractGameLogger = new AbstractGameLogger().setAgent(evoAgent1);
        abstractGameLogger.addShadow(evoAgentShadow1);
        abstractGameLogger.addShadow(evoAgentShadow2);
        abstractGameLogger.addShadow(new RandomAgent());
        abstractGameLogger.addShadow(new RandomAgent());
        abstractGameLogger.addShadow(new DoNothingAgent());
        abstractGameLogger.addShadow(new DoNothingAgent());
        // p1 = new AbstractGameLogger().setAgent(new DoNothingAgent());
        // p1 = new AbstractGameLogger().setAgent(evoAgent1);

        p1 = abstractGameLogger;
        p2 = new AbstractGameLogger().setAgent(new RandomAgent());

        SimplePlayerInterface opponentModel;
        opponentModel = new RandomAgent();

        // p2 = getMCTSAgent(new GameState().defaultState(), 1);
        p2 = evoAgent1;
        gameRunner.setPlayers(p1, p2);
        int nGames = 20;
        gameRunner.playGames(nGames);
        gameRunner.plotGameScores();

        // System.out.println(p1);

        // System.out.println(p2);
    }

    // commented out for now, it's interesting but not necessary for illustration
//    static GVGAIWrapper getMCTSAgent(GameState gameState, int playerId) {
//        ElapsedCpuTimer timer = new ElapsedCpuTimer();
//        PlanetWarsLinkState linkState = new PlanetWarsLinkState(gameState);
//        AbstractMultiPlayer agent =
//                new controllers.multiPlayer.discountOLMCTS.Agent(linkState.copy(), timer, playerId);
//        GVGAIWrapper wrapper = new GVGAIWrapper().setAgent(agent);
//        return wrapper;
//
//    }
}
