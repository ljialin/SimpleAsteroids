package planetwar;

import utilities.ElapsedTimer;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.Random;

/**
 * This is a simple Game State class for an abstract version of planet wars,
 * which stores only the growth rate, the number of ships, and the ownership for each
 * planet.
 * <p>
 * As a first effort the action space comprises at each time tick one of two things -
 * the source planet and the target planet - or an alternative would be to combine the
 * two.
 * <p>
 * Yet another alternative would be to have a buffer which ships are transferred in to,
 * then moved out of.  Imagine having a planet of focus, so the actions are to move between
 * them, and then to add or remove ships from it.
 * <p>
 * This has a rather nice feel to it - can be done with a small number of actions - let's say just 4.
 * <p>
 * Also has a nice feel for clever use of budget, and possible things to hide.
 * <p>
 * In this way the action space is fixed.
 * <p>
 * For visualisation, draw a tractor beam connecting the buffer of each one to the planet being
 * replenished or depleted.  The tractor beam could pulse every time a ship is moved.
 * <p>
 * So the first thing to do is to extend the game state to show this.
 * <p>
 * Or: could proceed with the simpler action space that just does the direct transfer model.
 * <p>
 * Problem is that we have a whole class of games here ...
 * <p>
 * Which one?
 * <p>
 * Easy approach is to implement and test a number of different nextState functions
 */


public class GameState implements AbstractGameState {

    public static Random random = new Random();

    public final static int incFocus = 0;
    public final static int decFocus = 1;
    public final static int incBuffer = 2;
    public final static int decBuffer = 3;
    public final static int doNothing = 4;

    final static int signPlayerOne = 1;
    final static int signPlayerTwo = -1;

    final static int[] signs = {signPlayerOne, signPlayerTwo};

    public static int nActions = 5;

    public static boolean includeBuffersInScore = true;
    public static boolean wrapAround = true;
    public static boolean invadeAll = true;
    public static boolean remove50perCent = true;


    int nTicks;

    // todo: check that the copy function works properly ...

    public GameState() {
        buffers = new double[2];
        focii = new int[2];
        nTicks = 0;
    }

    public static void main(String[] args) {

        // set a fixed random seed in order to check the outcome
        Random random = new Random(3);
        GameState.random = random;
        GameState gameState = new GameState().setNPlanets(10);
        gameState.setRandomGrowthRates().setRandomOwnerships();

        int nTicks = 20000000;

        ElapsedTimer timer = new ElapsedTimer();
        System.out.format("Initial score: %.2f\n", gameState.getScore());
        int terminals = 0;

        System.out.println(timer);

        for (int i = 0; i < nTicks; i++) {
            int a1 = random.nextInt(GameState.nActions);
            int a2 = random.nextInt(GameState.nActions);
//            a1 = AsteroidsGameState.doNothing;
//            a2 = AsteroidsGameState.doNothing;
            int[] actions = new int[]{a1, a2};
            gameState.next(actions);
            // gameState.update();
            // gameState = gameState.copy();
            if (gameState.isTerminal()) {
                terminals++;
            }
        }

        System.out.println(timer);
        System.out.println(Arrays.toString(gameState.planets));
        System.out.println(Arrays.toString(gameState.growthRates));
        System.out.println("Terminal states: " + terminals);
        System.out.println();
        System.out.format("Final score: %.2f\n", gameState.getScore());
    }

    public GameState setNPlanets(int nPlanets) {
        this.nPlanets = nPlanets;
        focii[0] = 0;
        focii[1] = nPlanets / 2;
        return this;
    }

    public GameState defaultState() {
        return this.setNPlanets(10).setAlternateOwnerships().setRandomGrowthRates();
    }

    public GameState defaultState(long seed) {
        GameState.random = new Random(seed);
        return this.setNPlanets(10).setAlternateOwnerships().setRandomGrowthRates();
    }

    @Override
    public GameState copy() {
        GameState gs = new GameState();
        gs.nPlanets = this.nPlanets;
        gs.growthRates = this.growthRates;
        gs.planets = new double[nPlanets];
        for (int i = 0; i < nPlanets; i++) {
            gs.planets[i] = planets[i];
        }
        gs.focii[0] = this.focii[0];
        gs.focii[1] = this.focii[1];

        gs.buffers[0] = this.buffers[0];
        gs.buffers[1] = this.buffers[1];

        return gs;
    }

    public GameState setRandomGrowthRates() {
        growthRates = new double[nPlanets];
        for (int i = 0; i < nPlanets; i++) {
            growthRates[i] = (random.nextDouble() + 0.5) / 10;
        }
        return this;
    }

    public double totalGrowthRate() {
        double tot = 0;
        for (int i = 0; i < nPlanets; i++) {
            tot += growthRates[i] * owner(i);
        }
        return tot;
    }

    public int owner(int i) {
        if (planets[i] == 0) return 0;
        return (planets[i] > 0) ? 1 : -1;
    }

    public GameState setRandomOwnerships() {
        planets = new double[nPlanets];
        for (int i = 0; i < nPlanets; i++) {
            planets[i] = random.nextInt(3) - 1;
            // System.out.println(planets[i]);
        }
        return this;
    }

    public GameState setAlternateOwnerships() {
        planets = new double[nPlanets];
        for (int i = 0; i < nPlanets; i++) {
            planets[i] = i % 2 == 0 ? 1 : -1; // random.nextInt(3) - 1;
            // at the same time, for now, also allocate a ship to each one

            // System.out.println(planets[i]);
        }
        return this;
    }

    int nPlanets = 10;
    double[] growthRates;

    double[] planets;

    // records the current planet of focus
    int[] focii;

    // buffer are one per player and store the number of ships in each case

    double[] buffers;

    public GameState update() {
        for (int i = 0; i < nPlanets; i++) {
            if (planets[i] > 0) {
                planets[i] += growthRates[i];
            }
            if (planets[i] < 0) {
                planets[i] -= growthRates[i];
            }
        }
        nTicks++;
        return this;
    }


    @Override
    public GameState next(int[] actions) {

        // need to make these for each player

        // very easy - just put in a switch statement for each one?

        next(actions[0], 0);
        next(actions[1], 1);

        // then update
        update();

        return this;
    }

    int limit(int x) {
        if (x < 0) return 0;
        if (x > nPlanets - 1) x = nPlanets - 1;
        return x;
    }

    public GameState next(int action, int playerId) {
        switch (action) {
            case incFocus: {
                focii[playerId] = focii[playerId] + 1;
                if (wrapAround) focii[playerId] %= nPlanets;
                else focii[playerId] = limit(focii[playerId]);
                break;
            }
            case decFocus: {
                focii[playerId] = focii[playerId] - 1;
                if (wrapAround) {
                    focii[playerId] = (focii[playerId] + nPlanets) % nPlanets;
                } else {
                    focii[playerId] = limit((focii[playerId]));
                }
                break;
            }
            case incBuffer: {
                // check ownership, then inc buffer if allowed
                // and decrement the ships of this sign on the planet
                int planetId = focii[playerId];


                if (planets[planetId] * signs[playerId] >= 0) {

                    if (remove50perCent) {
                        double tmp = planets[planetId] / 2;
                        planets[planetId] -= tmp;
                        buffers[playerId] += tmp * signs[playerId];
                    } else {
                        planets[planetId] -= signs[playerId];
                        buffers[playerId]++;
                    }
                }
                break;
            }
            case decBuffer: {
                // check that the buffer has some stuff in it
                if (buffers[playerId] > 0) {
                    int planetId = focii[playerId];
                    // then decrement the buffer
                    // and increment the planet by the sign of this player
                    // (so will become increasingly negative for a negative player)
                    // o
                    if (invadeAll) {
                        double temp = buffers[playerId];
                        buffers[playerId] = 0;
                        planets[planetId] += temp * signs[playerId];
                    } else {
                        buffers[playerId]--;
                        planets[planetId] += signs[playerId];
                    }
                }
                break;
            }
        }
        return this;
    }

    // public static double

    @Override
    public double getScore() {
        double score = 0;
        for (double x : planets) {
            score += x;
        }

        // now also factor in the buffers
        // or not, if we want the game to be more deceptive ...

        if (includeBuffersInScore) {
            score += buffers[0];
            score -= buffers[1];
        }


        return (int) (score * 10);
    }


    public static boolean terminateIfDominate = true;

    public boolean isTerminal() {
        // should do a better check really, but for now just return false

        if (terminateIfDominate) {
            StatSummary ss = new StatSummary();
            for (double x : planets) {
                ss.add(x);
            }
            return ss.min() > 0 || ss.max() < 0;
        } else {
            return false;
        }

        // return false;
    }

    public int getGameTick() {
        return nTicks;
    }

    public int nActions() {
        return 5;
    }
}
