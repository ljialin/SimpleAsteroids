package spinbattle.league;

import agents.dummy.RandomAgent;
import agents.evo.EvoAgent;
import ggi.agents.EvoAgentFactory;
import ggi.core.SimplePlayerInterface;

import java.util.ArrayList;

public class RoundRobinLeague {

    public static void main(String[] args) {
        // set up some players
        EvoAgentFactory f1 = new EvoAgentFactory();
        f1.useShiftBuffer = true;
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = f1.getAgent();

        ArrayList<SimplePlayerInterface> players = new ArrayList<>();

        RoundRobinLeague league = new RoundRobinLeague().setPlayers(players);

        int gamesPerMatch = 10;
        league.playGames(gamesPerMatch);

    }

    ArrayList<SimplePlayerInterface> players;
    int nPlayers;

    public RoundRobinLeague setPlayers(ArrayList<SimplePlayerInterface> players) {
        this.players = players;
        nPlayers = players.size();
        return this;
    }

    RoundRobinLeague playGames(int nGames) {
        int[][] results = new int[nPlayers][nPlayers];
        return this;
    }

}
