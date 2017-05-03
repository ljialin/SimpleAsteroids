package battle;

import utilities.ElapsedTimer;
import utilities.JEasyFrame;
import utilities.StatSummary;

import javax.swing.*;
import java.util.Random;

/**
 * Created by Simon Mark Lucas on 04/10/2016.
 */
public class SimpleBattleTest {

    // delay between each frame when running visually
    static int delay = 40;


    public static void main(String[] args) {
        int nSteps = (int) 2e3;
        int nPlayers = 2;

        int[] actions = new int[nPlayers];

        Random rand = new Random();

        SimpleBattleState state = new SimpleBattleState();
        state = state.copyState();

        StatSummary[] scoreStats = new StatSummary[]{new StatSummary(), new StatSummary()};

        ElapsedTimer t = new ElapsedTimer();
        BattleView view = new BattleView(state);

        // set view to null to run fast with no visuals
        // view = null;

        if (view != null) {
            new JEasyFrame(view, "Simple Battle Game");
        }

        for (int i=0; i<nSteps; i++) {

            // for each step of the game choose a random action for each player

            for (int j=0; j<nPlayers; j++) {
                actions[j] = rand.nextInt(state.nActions());
            }

            // now advance the game to the next step

            state.next(actions);
            for (int j=0; j<state.score.length; j++) {
                scoreStats[j].add(state.score[j]);
            }

            if (view != null) {
                view.repaint();
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {}
            }

        }

        for (int i=0; i<scoreStats.length; i++) {
            System.out.println("Player: " + i);
            System.out.println(scoreStats[i]);
            System.out.println();
        }
        System.out.println(t);
        System.out.println();

        System.out.println(nSteps + " states written");
    }
}
