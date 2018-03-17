package gvglink;

import controllers.multiPlayer.ea.Agent;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapterMulti;
import ga.SimpleRMHC;
import ntuple.SlidingMeanEDA;
import tools.ElapsedCpuTimer;

public class GeneralGameRunnerTest {
    public static void main(String[] args) {
        PlanetWarsLinkState linkState = new PlanetWarsLinkState();
        GeneralGameRunner runner = new GeneralGameRunner().setGame(linkState).setLength(200);

        AbstractMultiPlayer player1, player2;
        GameActionSpaceAdapterMulti.visual = false;

        int idPlayer1 = 0;
        int idPlayer2 = 1;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        player1 = new controllers.multiPlayer.discountOLMCTS.Agent(linkState.copy(), timer, idPlayer1);

        // try the evolutionary players
        GameActionSpaceAdapterMulti.visual = true;

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 200;
        EvoAlg evoAlg2 = new SlidingMeanEDA().setHistoryLength(20);


        Agent evoAgent1 = new controllers.multiPlayer.ea.Agent(linkState.copy(), timer, evoAlg, idPlayer1, nEvals);
        evoAgent1.sequenceLength = 10;
        // player1 = evoAgent1;


        Agent evoAgent2 = new controllers.multiPlayer.ea.Agent(linkState.copy(), timer, evoAlg, idPlayer2, nEvals);
        evoAgent2.sequenceLength = 10;
        // evoAgent2.setUseShiftBuffer(false);

        player2 = evoAgent2;


        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer2, nEvals);
        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, new SimpleRMHC(nResamples), idPlayer2, nEvals);

        // player1 = new controllers.multiPlayer.smlrand.Agent();
        // player1 = new controllers.multiPlayer.smlrand.Agent();
        // player2 = new controllers.multiPlayer.doNothing.Agent(linkState.copy(), timer, 1);


        runner.setPlayers(player1, player2);

        int nGames = 50;
        runner.playGames(nGames);


    }

}
