package planetwar;

import evodef.EvoAlg;
import ga.SimpleRMHC;

public class GameRunnerTest {
    public static void main(String[] args) {
        GameRunner gameRunner = new GameRunner().setLength(200);

        SimplePlayerInterface p1, p2;

        p1 = new RandomAgent();
        p2 = new DoNothingAgent();

        EvoAlg evoAlg = new SimpleRMHC();
        int nEvals = 100;
        int seqLength = 100;
        p1 = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);

        gameRunner.setPlayers(p1, p2);

        // now play a number of games and observe the outcomes
        // verbose is set to true by default so after the games have been played
        // it will report the outcomes

        int nGames = 100;
        gameRunner.playGames(nGames);

    }
}
