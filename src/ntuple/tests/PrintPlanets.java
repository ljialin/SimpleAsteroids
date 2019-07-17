package ntuple.tests;

import spinbattle.core.Planet;
import spinbattle.core.SpinGameState;
import spinbattle.params.SpinBattleParams;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

public class PrintPlanets {
    public static void main(String[] args) {
        SpinBattleParams params = new SpinBattleParams();
        // params.transitSpeed *= 2;
        params.width = 800;
        params.height = 500;
        params.nPlanets = 8;
        params.maxGrowth *= 2;
        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        System.out.format("Map dimension: %d x %d\n", params.width, params.height);
        System.out.format("Generated %d planets\n", gameState.planets.size());
        for (Planet p : gameState.planets) {

            System.out.format("Position: (%d,%d), \t radius = %d, owner = %d, ships = %d\n",
                    (int) p.position.x, (int) p.position.y, p.getRadius(), p.ownedBy, (int) p.shipCount   );
        }
        new JEasyFrame(new SpinBattleView().setGameState(gameState).setParams(params),
                "Map View");
    }
}
