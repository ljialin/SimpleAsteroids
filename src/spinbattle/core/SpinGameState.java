package spinbattle.core;

import ggi.AbstractGameState;
import spinbattle.event.LaunchEvent;
import spinbattle.event.SelectPlanetEvent;
import spinbattle.log.BasicLogger;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;

import java.util.ArrayList;

public class SpinGameState implements AbstractGameState {

    // this tracks all calls to the next method
    // useful for calculating overall stats

    public static int totalTicks = 0;
    public static int totalInstances = 0;

    public SpinGameState() {
        totalInstances++;
    }

    // number of ticks made by this instance
    public int nTicks;

    // may set a limit on the game length
    // this will be used in the isTerminal() method
    public SpinBattleParams params;

    public ArrayList<Planet> planets;
    public ProximityMap proximityMap;
    public VectorField vectorField;
    public BasicLogger logger;

    public SpinGameState setLogger(BasicLogger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    public AbstractGameState copy() {
        SpinGameState copy = new SpinGameState();
        // just shallow-copy the params
        copy.params = params;
        copy.nTicks = nTicks;
        // deep copy the planets
        copy.planets = new ArrayList<>();
        for (Planet p : planets) {
            copy.planets.add(p.copy());
        }
        // shallow copy the proximity map (which may even be null)
        copy.proximityMap = proximityMap;
        copy.vectorField = vectorField;

        // do NOT copy the logger - this is only used in the "real" game by default
        return copy;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        for (Planet p : planets) {
            p.update(this);
        }
        nTicks++;
        totalTicks++;
        return this;
    }

    @Override
    public int nActions() {
        return 0;
    }

    @Override
    public double getScore() {
        double score = 0;
        for (Planet p : planets) {
            score += p.getScore();
        }
        // but it the game is over, add in an early completion bonus
        if (!bothOwnPlanets()) {
            double tot = 0;
            for (Planet p : planets) tot += p.growthRate;
            double bonus = tot * (params.maxTicks - nTicks);
            // System.out.println("Awarding bonus: " + bonus);
            score += bonus;
        }
        return score;
    }

    @Override
    public boolean isTerminal() {
        return nTicks > params.maxTicks || !bothOwnPlanets();
    }

    // if only one player owns planets then the game is over
    public boolean bothOwnPlanets() {
        boolean playerOne = false;
        boolean playerTwo = false;

        for (Planet p : planets) {
            playerOne |= p.ownedBy == Constants.playerOne;
            playerTwo |= p.ownedBy == Constants.playerTwo;
            if (playerOne && playerTwo) return true;
        }

        // System.out.println(playerOne + " : " + playerTwo);
        return false;
    }

    public SpinGameState setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    static int maxTries = 200;

    public SpinGameState setPlanets() {
        planets = new ArrayList<>();
        int i=0;
        int whichEven = params.getRandom().nextInt(2);
        int nToAllocate = params.nPlanets - params.nNeutral;
        while (planets.size() < nToAllocate) {
            int owner = (planets.size() % 2 == whichEven ? Constants.playerOne : Constants.playerTwo);
            Planet planet = makePlanet(owner);
            // System.out.println("Made planet for: " + owner + " ... size: " + planets.size());
            planet.growthRate = params.maxGrowth;
            if (valid(planet)) {
                planet.setIndex(planets.size());
                planets.add(planet);
                // System.out.println("Added planet for: " + owner);
            } else {
                // System.out.println("Failed to add planet for: " + owner);
            }
            // System.out.println();
        }
        // System.out.println("To allocate: " + nToAllocate + " : " + planets.size());

        // set the neutral ones
        while (planets.size() < params.nPlanets && i++ < maxTries) {
            Planet planet = makePlanet(Constants.neutralPlayer);
            if (valid(planet)) {
                planet.setIndex(planets.size());
                planets.add(planet);
            }
        }
        // System.out.println(planets);
        if (params.useProximityMap) {
            proximityMap = new ProximityMap().setPlanets(this);
        }
        if (params.useVectorField) {
            vectorField = new VectorField().setParams(params).setField(this);
            // System.out.println("Set VF: " + vectorField);
        }

        return this;
    }

    Planet makePlanet(int owner) {
        Planet planet =new Planet().setParams(params).
                setRandomLocation(params).setOwnership(Constants.neutralPlayer);
        planet.setRandomGrowthRate();
        planet.setOwnership(owner);
        return planet;
    }

    boolean valid(Planet p) {
        double minX = Math.min(p.position.x, params.width - p.position.x);
        double minY = Math.min(p.position.y, params.height - p.position.y);
        // test whether planet is too close to border
        if (Math.min(minX, minY) < p.getRadius() * Constants.radSep) {
            // System.out.println("Failed border sep:" + minX +  " : " + minY);
            return false;
        }

        // now check proximity to each of the existing ones

        for (Planet x : planets) {
            double sep = x.position.dist(p.position);
            if (sep < Constants.radSep * (x.getRadius() + p.getRadius())) {
                // System.out.println("Failed planet proximity: " + (int) sep);
                return false;
            }
        }
        return true;

    }

    public void notifyLaunch(Transporter transit) {
        if (logger != null) {
            logger.logEvent(new LaunchEvent());
            // System.out.println(transit);
        }
    }

    public void notifySelection(Planet source) {
        if (logger != null) {
            logger.logEvent(new SelectPlanetEvent());
            // System.out.println(source);
        }
    }
    // todo - set up the planets based on the params that have been passed
}
