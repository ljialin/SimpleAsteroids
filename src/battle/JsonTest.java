package battle;

import com.google.gson.Gson;
import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Random;

/**
 * Created by simonmarklucas on 04/10/2016.
 */
public class JsonTest {

    public static void main(String[] args) {
        int nSteps = 10;
        int nPlayers = 2;

        int[] actions = new int[nPlayers];

        Gson gson = new Gson();

        Random rand = new Random();

        SimpleBattleState state = new SimpleBattleState();

        ElapsedTimer t = new ElapsedTimer();

        for (int i=0; i<nSteps; i++) {

            // for each step of the game choose a random action for each player

            for (int j=0; j<nPlayers; j++) {
                actions[j] = rand.nextInt(state.nActions());
            }

            // now advance the game to the next step
            state.next(actions);
            String out = gson.toJson(state);

            System.out.println(out);
        }
        System.out.println(t);
        System.out.println();

        System.out.println(nSteps + " states written");


    }
}
