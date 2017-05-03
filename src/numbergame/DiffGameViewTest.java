package numbergame;

import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import ga.SimpleRMHC;
import ontology.Types;
import tools.ElapsedCpuTimer;
import utilities.ElapsedTimer;
import utilities.JEasyFrame;

/**
 * Created by simonmarklucas on 30/03/2017.
 */
public class DiffGameViewTest {

    public static void main(String[] args) throws Exception {

        // stop the game ending
        DiffGame.maxTick = 1000000;
        DiffGame dg = new DiffGame();

        AbstractMultiPlayer player1, player2;
        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        player1 = new controllers.multiPlayer.sampleRandom.Agent(dg, timer, 0);
        player2 = new controllers.multiPlayer.sampleRandom.Agent(dg, timer, 1);


        int idPlayer1 = 0;
        int idPlayer2 = 1;


        // try the evolutionary players

        int nResamples = 3;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);
        EvoAlg evoAlg2 = new SimpleRMHC(nResamples);



        double kExplore = 2;
        int nNeighbours = 50;

        int nEvals = 15000;
        // evoAlg = new NTupleBanditEA(kExplore, nNeighbours);

        controllers.multiPlayer.ea.Agent agentEAShift =
                new controllers.multiPlayer.ea.Agent(dg, timer, evoAlg, idPlayer1, nEvals);
        agentEAShift.useShiftBuffer = true;
        agentEAShift.sequenceLength = 10;

        player1 = agentEAShift;

        // player2 = new controllers.multiPlayer.discountOLMCTS.Agent(dg, timer, idPlayer2);
        // player2 = new controllers.multiPlayer.sampleOLMCTS.Agent(dg, timer, idPlayer2);

        controllers.multiPlayer.ea.Agent agentEANoShift =
                new controllers.multiPlayer.ea.Agent(dg, timer, evoAlg2, idPlayer1, nEvals);
        agentEANoShift.useShiftBuffer = true;
        agentEANoShift.sequenceLength = 10;
        player1 = agentEANoShift;





        int nTicks = 250;
        int delay = 100;
        int updateTick = 1;

        DiffGameView view = new DiffGameView(dg);
        JEasyFrame frame = new JEasyFrame(view, "Diff Game");



        KeyPlayer keyPlayer = new KeyPlayer(view);
        frame.addKeyListener(keyPlayer.controller);
        // player1 = keyPlayer;

        player2 = new ConstantPlayer(2);

        Types.ACTIONS[] actions = new Types.ACTIONS[2];

        for (int i = 0; i < nTicks; i++) {

            // update the chosen actions every so often
            if (i % updateTick == 0) {
                actions[0] = player1.act(dg.copy(), timer);
                actions[1] = player2.act(dg.copy(), timer);
            }
            // advance anyway
            dg.advance(actions);
            view.update(dg);
            Thread.sleep(delay);
        }

        System.out.println(dg.getGameScore());
    }
}
