package planetwar;

import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import gvglink.PlanetWarsLinkState;
import ntuple.SlidingMeanEDA;
import tools.ElapsedCpuTimer;

public class GameRunnerTest {
    public static void main(String[] args) {

        GameState.includeBuffersInScore = false;
        GameRunner gameRunner = new GameRunner().setLength(200);

        SimplePlayerInterface p1, p2;

        p1 = new RandomAgent();
        // p2 = new DoNothingAgent();

        EvoAlg evoAlg1 = new SimpleRMHC();
        // evoAlg1.mu

        int nEvals1 = 200;
        int seqLength1 = 10;

        int nEvals2 = 400;
        int seqLength2 = 5;

        SlidingMeanEDA evoAlg2 = new SlidingMeanEDA().setHistoryLength(40);

        SimpleGA simpleGA = new SimpleGA().setPopulationSize(20);

        EvoAgent evoAgent1 = new EvoAgent().setEvoAlg(evoAlg1, nEvals1).setSequenceLength(seqLength1);
        evoAgent1.setUseShiftBuffer(true);
        // evoAgent1.mu
        // evoAgent1.u

        // evoAgent1.setOpponent(new RandomAgent()).setUseShiftBuffer(false);

        // evoAgent1.setOpponent(new RandomAgent());

        p1 = evoAgent1;

        SimplePlayerInterface opponentModel;
        opponentModel = new DoNothingAgent();
        opponentModel = new RandomAgent();
        // p2 = new EvoAgent().setEvoAlg(simpleGA, nEvals).setSequenceLength(seqLength).setOpponent(opponentModel);

        // p2 = new EvoAgent().setEvoAlg(evoAlg1, nEvals/2).setSequenceLength(seqLength*2).setOpponent(opponentModel);
        EvoAgent evoAgent2 = new EvoAgent().setEvoAlg(evoAlg1, nEvals2).setSequenceLength(seqLength2).setOpponent(opponentModel);

        evoAgent2.setUseShiftBuffer(true);
        p2 = evoAgent2;


        p2 = getMCTSAgent(new GameState().defaultState(), 1);

        // p2 = new RandomAgent();

        gameRunner.setPlayers(p1, p2);

        // now play a number of games and observe the outcomes
        // verbose is set to true by default so after the games have been played
        // it will report the outcomes


        int nGames = 100;
        gameRunner.playGames(nGames);

        // now access the game logs to plot the scores

        gameRunner.plotGameScores();

        // System.out.println(evoAlg2.pVec);

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
