package asteroids;

import evodef.EvoAlg;
import evogame.GameParameters;
import ga.SimpleRMHC;
import planetwar.EvoAgent;
import planetwar.SimplePlayerInterface;
import utilities.ElapsedTimer;
import utilities.StatSummary;

public class GameRunner {
    // class to run multiple trials of an agent playing Asteroids


    public static void main(String[] args) {

        int nTicks = 1000;
        int nTrials = 10;


        SimplePlayerInterface agent;

        agent = getEvoAgent();

        // agent = getRandomAgent();


        ElapsedTimer t = new ElapsedTimer();
        System.out.println(runTrials(agent, nTicks, nTrials));
        System.out.println(t);

    }

    public static StatSummary runTrials(SimplePlayerInterface agent, int nTicks, int nTrials) {
        StatSummary ss = new StatSummary("Score stats: " + agent.getClass().getSimpleName());
        for (int i=0; i<nTrials; i++) {
            double score = runTrial(agent, nTicks);
            System.out.println(i + "\t " + score);
            ss.add(score);
        }
        return ss;
    }

    public static double runTrial(SimplePlayerInterface agent, int nTicks) {
        GameState gameState = new GameState();
        gameState.initialLevel = 5;
        gameState.setParams(new GameParameters()).initForwardModel();

        gameState.state = State.Playing;
        System.out.println("Level = " + gameState.forwardModel.level);
        // gameState.forwardModel

        while (nTicks-- > 0 && gameState.state != State.GameOver) {
            int action = agent.getAction(gameState.copy(), 0);
            int[] actions = {action};
            gameState.next(actions);
        }
        // System.out.println();
        return gameState.getScore();
    }

    public static EvoAgent getEvoAgent() {
        int nResamples = 1;
        EvoAlg evoAlg = new SimpleRMHC(nResamples);

        int nEvals = 50;
        int seqLength = 100;

        EvoAgent evoAgent = new EvoAgent().setEvoAlg(evoAlg, nEvals).setSequenceLength(seqLength);
        evoAgent.setUseShiftBuffer(true);
        return evoAgent;
    }

    public static SimplePlayerInterface getRandomAgent() {
        return new planetwar.RandomAgent();
    }



}
