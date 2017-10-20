package gvglink;

import controllers.multiPlayer.ea.Agent;
import core.player.AbstractMultiPlayer;
import evodef.EvoAlg;
import evodef.GameActionSpaceAdapter;
import evodef.GameActionSpaceAdapterMulti;
import ga.SimpleRMHC;
import ntuple.CompactSlidingModelGA;
import ntuple.NTupleBanditEA;
import ntuple.SlidingMeanEDA;
import ontology.Types;
import planetwar.GameState;
import planetwar.KeyController;
import planetwar.PlanetWarView;
import tools.ElapsedCpuTimer;
import utilities.JEasyFrame;

import java.util.Random;

public class PlanetWarsLinkTest {

    // todo: show a graphic of the rollout predictions
    // split this in to two parts
    // for each action, we need to collect the data
    // then we need to display the data
    // each time plot the fitness of each sample versus the length
    // of the rollout - this will give a good idea of what we need to plot...

    // todo: implement a collision mechanism

    // todo: implement a nice graphic that shows transfer from planet to buffer
    // this is very easy to do - but question of whether we need a timed or instant movement...

    public static void main(String[] args) throws Exception {
        PlanetWarsLinkState state = new PlanetWarsLinkState();

        PlanetWarView view = null;
        // view = new PlanetWarView(state.state);
//        JEasyFrame frame = new JEasyFrame(view, "Test View");
//        KeyController controller = new KeyController();
//        frame.addKeyListener(controller);

        // now p;ay
        Random random = new Random();


        AbstractMultiPlayer player1, player2;
        GameActionSpaceAdapterMulti.visual = false;

//        controllers.singlePlayer.sampleOLMCTS.Agent olmcts =
//                new controllers.singlePlayer.sampleOLMCTS.Agent(linkState, timer);

        int idPlayer1 = 0;
        int idPlayer2 = 1;

        ElapsedCpuTimer timer = new ElapsedCpuTimer();

        player1 = new controllers.multiPlayer.discountOLMCTS.Agent(state.copy(), timer, idPlayer1);

        // try the evolutionary players

        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 200;
        evoAlg = new SlidingMeanEDA().setHistoryLength(20);


        Agent evoAgent = new controllers.multiPlayer.ea.Agent(state.copy(), timer, evoAlg, idPlayer1, nEvals);
        evoAgent.sequenceLength = 100;
        // player1 = evoAgent;

        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer2, nEvals);
        // player2 = new controllers.multiPlayer.ea.Agent(linkState, timer, new SimpleRMHC(nResamples), idPlayer2, nEvals);

        // player1 = new controllers.multiPlayer.smlrand.Agent();
        player2 = new controllers.multiPlayer.smlrand.Agent();
        // player2 = new controllers.multiPlayer.doNothing.Agent(state, timer, 1);

        // EvoAlg evoAlg2 = new SimpleRMHC(2);

        // player1 = new controllers.multiPlayer.ea.Agent(linkState, timer, evoAlg2, idPlayer1, nEvals);


        // player1 =
        int thinkingTime = 5; // in milliseconds
        int delay = 100;

        // player = new controllers.singlePlayer.sampleRandom.Agent(stateObs, timer);

        // check that we can play the game

        int nSteps = 10;

        for (int i = 0; i < nSteps; i++) {

            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);

            Types.ACTIONS action1 = player1.act(state.copy(), timer);

            timer = new ElapsedCpuTimer();
            timer.setMaxTimeMillis(thinkingTime);
            Types.ACTIONS action2 = player2.act(state.copy(), timer);
            // §action2 =

            state.advance(new Types.ACTIONS[]{action1, action2});

            if (view != null) {
                view.update(state.state);
                Thread.sleep(delay);
            }
            // System.out.println("Game tick: " + i);
        }
        System.out.println("Game Score: " + state.getGameScore());
    }


}

