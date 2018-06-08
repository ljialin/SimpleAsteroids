package spinbattle.view;

import agents.dummy.RandomAgent;
import ggi.agents.EvoAgentFactory;
import ggi.core.AbstractGameFactory;
import ggi.core.AbstractGameState;
import ggi.core.AbstractVisualRunner;
import ggi.core.SimplePlayerInterface;
import spinbattle.core.SpinGameState;
import spinbattle.core.SpinGameStateFactory;
import spinbattle.params.SpinBattleParams;
import utilities.JEasyFrame;

import javax.swing.*;

public class VisualSpinGameRunner extends Thread implements AbstractVisualRunner  {

    static int frameDelay = 20;

    public static void main(String[] args) {
        SpinBattleParams params = new SpinBattleParams();
        params.maxTicks = 2000;
        SpinGameStateFactory factory = new SpinGameStateFactory();
        factory.params = params;
        SimplePlayerInterface p1 = new RandomAgent();
        SimplePlayerInterface p2 = new EvoAgentFactory().getAgent();
        SimplePlayerInterface[] players = new SimplePlayerInterface[]{p1, p2};
        AbstractVisualRunner avs = new VisualSpinGameRunner();
        avs.playVisualGame(factory, players);
        System.out.println("Running in new thread...");
    }

    AbstractGameFactory gameFactory;
    SimplePlayerInterface[] players;

    public void run() {

        playVisualGameInternal(gameFactory, players);
    }


    boolean runInThread = false;
    @Override
    public AbstractVisualRunner playVisualGame(AbstractGameFactory gameFactory, SimplePlayerInterface[] players) {
        this.gameFactory = gameFactory;
        this.players = players;
        if (runInThread) {
            start();
        } else {
            playVisualGameInternal(gameFactory, players);
        }
        return this;
    }

    public AbstractVisualRunner playVisualGameInternal(AbstractGameFactory gameFactory, SimplePlayerInterface[] players) {
        try {
            SpinGameState spinGameState = (SpinGameState) gameFactory.newGame();
            SpinBattleParams params = spinGameState.params;
            SpinBattleView view = new SpinBattleView().setParams(params).setGameState(spinGameState);
            String title = players[0] + " versus " + players[1];
            JEasyFrame frame = new JEasyFrame(view, title + ": Waiting for Graphics");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            spinbattle.util.ViewUtil.waitUntilReady(view);
            int[] actions = new int[2];

            int nTicks = 50000;
            for (int i=0; i<nTicks && !spinGameState.isTerminal(); i++) {
                actions[0] = players[0].getAction(spinGameState.copy(), 0);
                actions[1] = players[1].getAction(spinGameState.copy(), 1);
                spinGameState.next(actions);
                // System.out.println(Arrays.toString(actions));
                SpinGameState viewCopy = (SpinGameState) spinGameState.copy();
                view.setGameState(viewCopy);
                view.repaint();
                frame.setTitle(title + " : " + i); //  + " : " + CaveView.getTitle());
                Thread.sleep(frameDelay);
                nTicks++;
            }
            // System.out.println(spinGameState.isTerminal());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
