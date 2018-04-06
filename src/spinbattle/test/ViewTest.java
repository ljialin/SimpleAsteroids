package spinbattle.test;

import spinbattle.core.SpinGameState;
import spinbattle.params.SpinBattleParams;
import spinbattle.view.SpinBattleView;
import utilities.JEasyFrame;

public class ViewTest {
    public static void main(String[] args) throws Exception {
        SpinBattleParams params = new SpinBattleParams();
        SpinGameState gameState = new SpinGameState().setParams(params);
        SpinBattleView view = new SpinBattleView().setParams(params);

        new JEasyFrame(view, "Spin Battle Game");

        for (int i=0; i<1000; i++) {
            view.repaint();
            Thread.sleep(20);
        }

    }
}
