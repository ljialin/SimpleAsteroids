package spinbattle.league;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import com.google.gson.Gson;
import ggi.agents.EvoAgentFactory;
import ggi.agents.SimpleEvoAgent;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import ggi.league.RoundRobinLeague;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.Constants;
import spinbattle.test.MCTSAgentTest;
import spinbattle.view.VisualSpinGameRunner;
import utilities.ElapsedTimer;

import java.util.ArrayList;

/**
 *  Should move this later to the the ggi package ...
 */

public class RoundRobinLeagueTest {

    static int maxTicks = 2000;
    static boolean visual = true;

    public static void main(String[] args) {
        int gamesPerMatch = 10;

        ElapsedTimer t = new ElapsedTimer();
        // set up some players
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;
        factory.params.nPlanets = 20;

        EvoAgentFactory f1 = new EvoAgentFactory();
        f1.mutationRate = 10;
        f1.seqLength = 100;
        f1.nEvals = 40;

        f1.useShiftBuffer = true;
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = f1.getAgent();
        SimplePlayerInterface p3 = new DoNothingAgent();
        SimplePlayerInterface p4 = MCTSAgentTest.getMCTSAgent(factory.newGame(), Constants.playerOne);
        SimplePlayerInterface p5 = new SimpleEvoAgent();

        ArrayList<SimplePlayerInterface> players = new ArrayList<>();
        // players.add(p1);
        players.add(p2);
        // players.add(p3);
        // players.add(p4);
        players.add(p5);

        RoundRobinLeague league = new RoundRobinLeague().setPlayers(players);
        league.abstractVisualRunner = new VisualSpinGameRunner();

        GameRunnerTwoPlayer gameRunner = new GameRunnerTwoPlayer();
        gameRunner.nSteps = maxTicks;

        gameRunner.setGameFactory(factory);
        league.gameRunner = gameRunner;

        league.playGames(gamesPerMatch);

        System.out.println(league);

        Gson gson = new Gson();
        System.out.println(gson.toJson(league.results));
        System.out.format("Elapsed time (s) = %.1f", t.elapsed() / 1000.0);

    }
}
