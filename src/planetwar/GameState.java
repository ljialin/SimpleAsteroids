package planetwar;

import utilities.ElapsedTimer;

import java.util.Arrays;
import java.util.Random;

/**
 *  This is a simple Game State class for an abstract version of planet wars,
 *  which stores only the growth rate, the number of ships, and the ownership for each
 *  planet.
 *
 *  As a first effort the action space comprises at each time tick one of two things -
 *  the source planet and the target planet - or an alternative would be to combine the
 *  two.
 *
 *  Yet another alternative would be to have a buffer which ships are transferred in to,
 *  then moved out of.  Imagine having a planet of focus, so the actions are to move between
 *  them, and then to add or remove ships from it.
 *
 *  This has a rather noce feel to it - can be done with a small number of actions - let's say just 4.
 *
 *  Also has a nice feel for clever use of budget, and possible things to hide.
 *
 *  In this way the action space is fixed.
 *
 *  For visualisation, draw a tractor beam connecting the buffer of each one to the planet being
 *  replenished or depleted.  The tractor beam could pulse every time a ship is moved.
 *
 *  So the first thing to do is to extend the game state to show this.
 *
 *  Or: could proceed with the simpler action space that just does the direct transfer model.
 *
 *  Problem is that we have a whole class of games here ...
 *
 *  Which one?
 *
 *  Easy approach is to implement and test a number of different nextState functions
 *
 */


public class GameState {

    static Random random = new Random();

    static int incFocus = 0;
    static int decFocus = 1;
    static int incBuffer = 2;
    static int decBuffer = 3;
    static int doNothing = 4;

    static int nActions = 5;

    public static void main(String[] args) {
        GameState gameState = new GameState().setNPlanets(10);
        gameState.setRandomGrowthRates().setRandomOwnerships();

        int nReps = 1000000;

        ElapsedTimer timer = new ElapsedTimer();
        System.out.format("Initial score: %.2f\n", gameState.getScore());

        System.out.println(timer);
        for (int i=0; i<nReps; i++) {
            gameState.update();
            gameState = gameState.copy();
        }
        System.out.println(timer);
        System.out.println(Arrays.toString(gameState.planets));
        System.out.println(Arrays.toString(gameState.growthRates));
        System.out.println();
        System.out.format("Final score: %.2f\n", gameState.getScore());

    }

    public GameState setNPlanets(int nPlanets) {
        this.nPlanets = nPlanets;
        return this;
    }

    public GameState copy() {
        GameState gs = new GameState();
        gs.nPlanets = this.nPlanets;
        gs.growthRates = new double[nPlanets];
        gs.planets = new double[nPlanets];
        for (int i=0; i<nPlanets; i++) {
            gs.growthRates[i] = growthRates[i];
            gs.planets[i] = planets[i];
        }
        return this;
    }

    public GameState setRandomGrowthRates() {
        growthRates = new double[nPlanets];
        for (int i=0; i<nPlanets; i++) {
            growthRates[i] = random.nextDouble() + 0.5;
        }
        return this;
    }

    public GameState setRandomOwnerships() {
        planets = new double[nPlanets];
        for (int i=0; i<nPlanets; i++) {
            planets[i] = random.nextInt(3) - 1;
            System.out.println(planets[i]);
        }
        return this;
    }

    int nPlanets = 10;
    double[] growthRates;

    double[] planets;

    // records the current planet of focus
    int[] focii;

    // the buffers store floating point numbers to allow
    // for transfer rates of less than one per game tick
    // the idea is that the further away, the longer it takes
    // and hence we have a slower fractional transfer rate.

    // also, when switching focus, any partial amount could be
    // potentially lost - this is a detail that needs a bit of thought

    double[] buffers;

    public GameState update() {
        for (int i=0; i<nPlanets; i++) {
            if (planets[i] > 0) {
                planets[i] += growthRates[i];
            }
            if (planets[i] < 0) {
                planets[i] -= growthRates[i];
            }
        }
        return this;
    }

    // this version provides a next function
    // that does a transfer from source to destination for
    // each player
    // the source and destination planets have to be decoded
    // from the single int that encodes both of them
    public GameState next(int[] actions) {
        if (true)
            throw new RuntimeException("Not yet implemented.");
        return this;

    }

    public double getScore() {
        double score = 0;
        for (double x : planets) {
            score += x;
        }
        return score;
    }
}
