package ggi.league;

import ggi.core.AbstractGameState;
import ggi.core.AbstractVisualRunner;
import ggi.core.GameRunnerTwoPlayer;
import ggi.core.SimplePlayerInterface;

import java.util.ArrayList;

/**
 *  Should move this later to the the ggi package ...
 */

public class RoundRobinLeague {

    public ArrayList<SimplePlayerInterface> players;
    public int nPlayers;
    public GameRunnerTwoPlayer gameRunner;
    public int[][] results;
    public AbstractVisualRunner abstractVisualRunner;

    public RoundRobinLeague setPlayers(ArrayList<SimplePlayerInterface> players) {
        this.players = players;
        nPlayers = players.size();
        results = new int[nPlayers][nPlayers];
        return this;
    }

    public RoundRobinLeague playGames(int nGames) {
        System.out.println(players);
        for (int i=0; i<nPlayers; i++) {
            for (int j=0; j<nPlayers; j++) {

                if (i != j) {
                    System.out.println(i + " : " + j);
                    if (abstractVisualRunner != null) {
                        SimplePlayerInterface p1 = players.get(i);
                        SimplePlayerInterface p2 = players.get(j);
                        SimplePlayerInterface[] players = new SimplePlayerInterface[]{p1, p2};
                        abstractVisualRunner.playVisualGame(gameRunner.gameFactory, players);
                    }

                    gameRunner.setPlayers(players.get(i), players.get(j));
                    gameRunner.verbose = true;
                    gameRunner.playGames(nGames);

                    results[i][j] += gameRunner.p1Wins;
                    results[j][i] += gameRunner.p2Wins;

                    System.out.println(players.get(i));
                    System.out.println("Versus");
                    System.out.println(players.get(j));
                    System.out.println(gameRunner.p1Wins + " : " + gameRunner.p2Wins);
                    gameRunner.plotGameScores();
                }
            }
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        // add the players
        int ix = 0;
        for (SimplePlayerInterface p : players) {
            sb.append(ix++ + "\t " + p + "\n");
        }
        sb.append("\n Results:\n");
        // now the results
        for (int i=0; i<nPlayers; i++) {
            StringBuilder row = new StringBuilder();
            row.append(i + " : \t");
            int rowTot = 0;
            for (int j=0; j<nPlayers; j++) {
                row.append(results[i][j] +"\t");
                rowTot += results[i][j];
            }
            row.append(" -> " + rowTot + "\n");
            sb.append(row.toString());
        }
        return sb.toString();
    }
}
