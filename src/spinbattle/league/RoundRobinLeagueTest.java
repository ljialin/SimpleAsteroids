package spinbattle.league;

import agents.dummy.DoNothingAgent;
import agents.dummy.RandomAgent;
import ggi.agents.EvoAgentFactory;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;
import ggi.league.RoundRobinLeague;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.view.VisualSpinGameRunner;

import java.util.ArrayList;

/**
 *  Should move this later to the the ggi package ...
 */

public class RoundRobinLeagueTest {

    static int maxTicks = 1000;
    static boolean visual = true;

    public static void main(String[] args) {
        // set up some players
        EvoAgentFactory f1 = new EvoAgentFactory();
        f1.useShiftBuffer = true;
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = f1.getAgent();
        SimplePlayerInterface p3 = new DoNothingAgent();

        ArrayList<SimplePlayerInterface> players = new ArrayList<>();
        players.add(p1); players.add(p2); players.add(p3);

        RoundRobinLeague league = new RoundRobinLeague().setPlayers(players);
        league.abstractVisualRunner = new VisualSpinGameRunner();

        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params.maxTicks = maxTicks;
        GameRunnerTwoPlayer gameRunner = new GameRunnerTwoPlayer();
        gameRunner.nSteps = maxTicks;

        gameRunner.setGameFactory(factory);
        league.gameRunner = gameRunner;

        int gamesPerMatch = 5;
        league.playGames(gamesPerMatch);

        System.out.println(league);
    }
}
