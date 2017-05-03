package battle;

import com.google.gson.Gson;
import utilities.ElapsedTimer;

import java.util.Random;

/**
 * Created by simonmarklucas on 04/10/2016.
 */
public class JsonTestBattleParams {

    public static void main(String[] args) {
        Gson gson = new Gson();

        SimpleBattleState state = new SimpleBattleState();


        String str = gson.toJson(BattleGameParameters.params);
        System.out.println(str);



    }
}
