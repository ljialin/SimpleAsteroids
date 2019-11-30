package spinbattle.test;

import spinbattle.core.SpinGameState;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.HeuristicLauncher;
import spinbattle.players.TunablePriorityLauncher;
import spinbattle.ui.SourceTargetMouseController;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

public class HumanInterfaceTest {

    public static void main(String[] args) throws Exception {
        SpinBattleParams params = new SpinBattleParams();
        params.width = 1000;
        params.height = 700;
        params.gravitationalFieldConstant *= 2.0;
        params.nPlanets = 40;
        params.maxTicks = 5000;
        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        SpinBattleView view = new SpinBattleView().setParams(params).setGameState(gameState);
        HeuristicLauncher launcher = new HeuristicLauncher();
        // TunablePriorityLauncher launcher = new TunablePriorityLauncher();
        JEasyFrame frame = new JEasyFrame(view, "Spin Battle Game");
        SourceTargetMouseController mouseController = new SourceTargetMouseController();
        mouseController.setGameState(gameState).setPlayerId(Constants.playerOne);
        view.addMouseListener(mouseController);
        int launchPeriod = 400;
        for (int i=0; i<5000 && !gameState.isTerminal(); i++) {
            gameState.next(null);
            // launcher.makeTransits(gameState, Constants.playerOne);
            if (i % launchPeriod == 0)
                launcher.makeTransits(gameState, Constants.playerTwo);
            view.setGameState((SpinGameState) gameState.copy());
            view.repaint();
            Thread.sleep(20);
        }
        System.out.println(gameState.isTerminal());
        System.out.println(gameState.params.maxTicks);
    }

}
