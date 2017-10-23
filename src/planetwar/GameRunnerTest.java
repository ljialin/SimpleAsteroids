package planetwar;

import evodef.EvoAlg;
import ga.SimpleGA;
import ga.SimpleRMHC;
import ntuple.SlidingMeanEDA;

public class GameRunnerTest {
    public static void main(String[] args) {
        GameRunner gameRunner = new GameRunner().setLength(200);

        SimplePlayerInterface p1, p2;

        p1 = new RandomAgent();
        // p2 = new DoNothingAgent();

        EvoAlg evoAlg1 = new SimpleRMHC();

        int nEvals = 100;
        int seqLength = 20;

        SlidingMeanEDA evoAlg2 = new SlidingMeanEDA().setHistoryLength(40);

        SimpleGA simpleGA = new SimpleGA().setPopulationSize(20);

        EvoAgent evoAgent1 = new EvoAgent().setEvoAlg(evoAlg1, nEvals).setSequenceLength(seqLength);
        // evoAgent1.setUseShiftBuffer(false);
        // evoAgent1.u

        // evoAgent1.setOpponent(new RandomAgent()).setUseShiftBuffer(false);

        // evoAgent1.setOpponent(new RandomAgent());

        p1 = evoAgent1;

        SimplePlayerInterface opponentModel;
        opponentModel = new DoNothingAgent();
        // opponentModel = new RandomAgent();
        // p2 = new EvoAgent().setEvoAlg(simpleGA, nEvals).setSequenceLength(seqLength).setOpponent(opponentModel);

        // p2 = new EvoAgent().setEvoAlg(evoAlg1, nEvals/2).setSequenceLength(seqLength*2).setOpponent(opponentModel);
        EvoAgent evoAgent2 = new EvoAgent().setEvoAlg(evoAlg1, nEvals).setSequenceLength(seqLength).setOpponent(opponentModel);

        evoAgent2.setUseShiftBuffer(false);
        p2 = evoAgent2;



        // p2 = new RandomAgent();

        gameRunner.setPlayers(p1, p2);

        // now play a number of games and observe the outcomes
        // verbose is set to true by default so after the games have been played
        // it will report the outcomes


        int nGames = 50;
        gameRunner.playGames(nGames);

        // now access the game logs to plot the scores


        gameRunner.plotGameScores();

        // System.out.println(evoAlg2.pVec);

    }


}
