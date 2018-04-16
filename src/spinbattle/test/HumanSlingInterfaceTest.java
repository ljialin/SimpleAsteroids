package spinbattle.test;

import spinbattle.core.SpinGameState;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;
import spinbattle.players.HeuristicLauncher;
import spinbattle.ui.MouseSlingController;
import spinbattle.ui.SourceTargetMouseController;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

import java.util.Random;

public class HumanSlingInterfaceTest {

    public static void main(String[] args) throws Exception {
        // to always get the same initial game
        SpinBattleParams.random = new Random(2);
        SpinBattleParams params = new SpinBattleParams();
        SpinGameState gameState = new SpinGameState().setParams(params).setPlanets();
        SpinBattleView view = new SpinBattleView().setParams(params).setGameState(gameState);
        HeuristicLauncher launcher = new HeuristicLauncher();
        String title = "Spin Battle Game" ;
        JEasyFrame frame = new JEasyFrame(view, title + ": Waiting for Graphics");
        MouseSlingController mouseSlingController = new MouseSlingController();
        mouseSlingController.setGameState(gameState).setPlayerId(Constants.playerOne);
        view.addMouseListener(mouseSlingController);
        int launchPeriod = params.releasePeriod;
        waitUntilReady(view);

        for (int i=0; i<=5000; i++) {
            gameState.next(null);
            mouseSlingController.update();
            // launcher.makeTransits(gameState, Constants.playerOne);
            if (i % launchPeriod == 0)
                launcher.makeTransits(gameState, Constants.playerTwo);
            view.setGameState((SpinGameState) gameState.copy());
            view.repaint();
            frame.setTitle(title + " : " + i + " : " + view.getTitle());
            Thread.sleep(20);
        }
    }

    static void waitUntilReady(SpinBattleView view) throws Exception {
        int i = 0;
        while (view.nPaints == 0) {
            // System.out.println(i++ + " : " + view.nPaints);
            Thread.sleep(50);
        }
    }

}
