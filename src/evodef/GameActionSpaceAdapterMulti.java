package evodef;

import core.game.StateObservation;
import core.game.StateObservationMulti;
import gvglink.SpaceBattleLinkState;
import gvglink.SpaceBattleLinkStateTwoPlayer;
import ontology.Types;
import planetwar.GameState;
import plot.LineChart;
import plot.LineChartAxis;
import plot.LinePlot;
import utilities.JEasyFrame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sml on 20/01/2017.
 */
public class GameActionSpaceAdapterMulti implements FitnessSpace {
    StateObservationMulti stateObservation;
    int sequenceLength;
    EvolutionLogger logger;
    int nEvals;
    public static boolean useDiscountFactor = true;
    public static boolean useHeuristic = true;
    static Random random = new Random();
    static double noiseLevel = 0;

    public static int actionRepeat = 1;

    public int numActions;
    public Types.ACTIONS[] gvgaiActions;

    // this is used to value future rewards less
    // than immediate ones via an exponential decay
    public double discountFactor = 0.99;

    int playerID;
    int opponentID;


    /**
     * For now assume that the number of actions available at each game tick is always
     * the same and may be found with a call to stateObservation
     *
     * @param stateObservation
     * @param sequenceLength
     */
    public GameActionSpaceAdapterMulti(StateObservationMulti stateObservation, int sequenceLength, int playerID, int opponentID) {
        this.stateObservation = stateObservation;
        this.sequenceLength = sequenceLength;

        ArrayList<Types.ACTIONS> act = stateObservation.getAvailableActions();
        gvgaiActions = new Types.ACTIONS[act.size()];
        for(int i = 0; i < gvgaiActions.length; ++i)
        {
            gvgaiActions[i] = act.get(i);
        }
        numActions = gvgaiActions.length;
        logger = new EvolutionLogger();
        nEvals = 0;
        this.playerID = playerID;
        this.opponentID = opponentID;

        // also make the plot list if we're running visually
        if (visual) {
            linePlots = new ArrayList<>();
        }

    }

    public static boolean visual = false;
    List<LinePlot> linePlots;



    @Override
    public int nDims() {
        return sequenceLength;
    }

    @Override
    public int nValues(int i) {
        // we assume that the nummber of actions available (and hence number of possible
        // values at each point in the search space
        // is always the same
        return numActions;
    }

    @Override
    public void reset() {
        // no action is needed apart from resetting the count;
        // the state is defined by the stateObservation that is passed to this
        logger.reset();
        nEvals = 0;
    }

    @Override
    public double evaluate(int[] actions) {
        // take a copy of the current game state and accumulate the score as we go along

        StateObservationMulti obs = stateObservation.copy();
        // note the score now - for normalisation reasons
        // we wish to track the change in score, not the absolute score
        double initScore = obs.getGameScore(playerID);
        double discount = 1.0;
        double denom = 0;
        double discountedTot = 0;

        // need to do the visual stuff here ...
        LinePlot linePlot = null;
        if (visual) {
            float grey = (nEvals % 100) / 50.0f;
            linePlot = new LinePlot().setColor(new Color(grey, grey, grey));
            // linePlot = new LinePlot().setColor(Color.red);

        }


        for (int i=0; i<sequenceLength; i++) {

            // Note here that we need to look at the advance method which takes multiple players
            // hence an array of actions
            // the idea is that we'll pad out the
            int myAction = actions[i];
            int opAction = random.nextInt(obs.getAvailableActions(opponentID).size());
            opAction = GameState.doNothing;
            Types.ACTIONS[] acts = new Types.ACTIONS[2];
            acts[playerID] = gvgaiActions[myAction];
            acts[opponentID] = gvgaiActions[opAction];

            for (int k=0; k< actionRepeat; k++) {
                obs.advance(acts);
            }

            discountedTot += discount * (obs.getGameScore(playerID) - initScore);

            if (useHeuristic && obs instanceof SpaceBattleLinkStateTwoPlayer) {
                SpaceBattleLinkStateTwoPlayer state = (SpaceBattleLinkStateTwoPlayer) obs;
                discountedTot += state.getHeuristicScore();
            }
            denom += discount;
            discount *= discountFactor;

            if (linePlot != null) {
                linePlot.add(discountedTot);
            }

        }
        if (visual) {
            linePlots.add(linePlot);
        }

        nEvals++;
        double delta;
        if (useDiscountFactor) {
            delta = discountedTot / denom;
        } else {
            delta = obs.getGameScore() - initScore;
        }
        delta += noiseLevel * random.nextGaussian();
        logger.log(delta, actions, false);

        return delta;
    }

    public void plot() {
        System.out.println("in plot? "  + linePlots == null);
        if (linePlots!=null) {
            LineChart lineChart = new LineChart().setBG(Color.blue);
            lineChart.xAxis = new LineChartAxis(new double[]{0, 10, 20, 30});
            lineChart.yAxis = new LineChartAxis(new double[]{-200, -100, 0, 100, 200});

            lineChart.addLines(linePlots);
            // lineChart.
            // lineChart.
            new JEasyFrame(lineChart, "Evo Plots");
            for (LinePlot linePlot : linePlots) {
                System.out.println(linePlot);
                // System.out.println();
            }
            System.out.println();
        }
    }





    @Override
    public boolean optimalFound() {
        return false;
    }

    @Override
    public SearchSpace searchSpace() {
        return this;
    }

    @Override
    public int nEvals() {
        return nEvals;
    }

    @Override
    public EvolutionLogger logger() {
        return logger;
    }

    @Override
    public Double optimalIfKnown() {
        return null;
    }
}
