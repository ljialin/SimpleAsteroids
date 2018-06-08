package spinbattle.league;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import com.google.gson.Gson;
import ggi.agents.EvoAgentFactory;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import ggi.league.RoundRobinLeague;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.Constants;
import spinbattle.test.MCTSAgentTest;
import spinbattle.view.VisualSpinGameRunner;

import java.util.ArrayList;

/**
 *  Should move this later to the the ggi package ...
 */

public class RoundRobinLeagueTest {

    static int maxTicks = 2000;
    static boolean visual = true;

    public static void main(String[] args) {
        // set up some players
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;

        EvoAgentFactory f1 = new EvoAgentFactory();
        f1.mutationRate = 10;
        f1.seqLength = 100;
        f1.nEvals = 40;

        f1.useShiftBuffer = true;
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = f1.getAgent();
        SimplePlayerInterface p3 = new DoNothingAgent();
        SimplePlayerInterface p4 = MCTSAgentTest.getMCTSAgent(factory.newGame(), Constants.playerOne);

        ArrayList<SimplePlayerInterface> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        RoundRobinLeague league = new RoundRobinLeague().setPlayers(players);
        league.abstractVisualRunner = new VisualSpinGameRunner();

        GameRunnerTwoPlayer gameRunner = new GameRunnerTwoPlayer();
        gameRunner.nSteps = maxTicks;

        gameRunner.setGameFactory(factory);
        league.gameRunner = gameRunner;

        int gamesPerMatch = 5;
        league.playGames(gamesPerMatch);

        System.out.println(league);

        Gson gson = new Gson();
        System.out.println(gson.toJson(league.results));
    }
}
