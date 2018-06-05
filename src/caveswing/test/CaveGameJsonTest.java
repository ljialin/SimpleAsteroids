package caveswing.test;

import agents.dummy.RandomAgent;
import caveswing.core.CaveGameFactory;
import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;
import com.google.gson.Gson;
import ggi.tests.SpeedTest;

public class CaveGameJsonTest {
    public static void main(String[] args) {
        CaveSwingParams params = new CaveSwingParams();
        params.gravity.y = -1.0;
        params.gravity.x = 0.5;
        CaveGameState gameState = new CaveGameState().setParams(params).setup();
        Gson gson = new Gson();
        System.out.println("Serialising...");
        String json = gson.toJson(gameState);
        System.out.println(json);
        System.out.println();
        System.out.println(json.length());
    }
}
