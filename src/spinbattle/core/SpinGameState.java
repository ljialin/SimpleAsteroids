package spinbattle.core;

import ggi.AbstractGameState;
import spinbattle.params.Constants;
import spinbattle.params.SpinBattleParams;

import java.util.ArrayList;

public class SpinGameState implements AbstractGameState {

    // this tracks all calls to the next method
    // useful for calculating overall stats

    public static int totalTicks;

    // number of ticks made by this instance
    int nTicks;

    // may set a limit on the game length
    // this will be used in the isTerminal() method
    SpinBattleParams params;

    public ArrayList<Planet> planets;


    @Override
    public AbstractGameState copy() {
        return null;
    }

    @Override
    public AbstractGameState next(int[] actions) {
        for (Planet p : planets) {
            p.update();
        }
        return this;
    }

    @Override
    public int nActions() {
        return 0;
    }

    @Override
    public double getScore() {
        return 0;
    }

    @Override
    public boolean isTerminal() {
        return nTicks > params.maxTicks;
    }

    public SpinGameState setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    static int maxTries = 200;

    public SpinGameState setPlanets() {
        planets = new ArrayList<>();
        int i=0;
        // set the neutral ones
        int nToAllocate = params.nPlanets - params.nNeutral;
        while (planets.size() < nToAllocate) {
            int owner = planets.size() == 0 ? Constants.playerOne : Constants.playerTwo;
            Planet planet = makePlanet(owner);
            planet.growthRate = params.maxGrowth;
            if (valid(planet)) planets.add(planet);
        }
        System.out.println("To allocate: " + nToAllocate + " : " + planets.size());

        while (planets.size() < params.nPlanets && i++ < maxTries) {
            Planet planet = makePlanet(Constants.neutralPlayer);
            if (valid(planet)) planets.add(planet);
        }
        System.out.println(planets);
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
            System.out.println("Failed border sep:" + minX +  " : " + minY);
            return false;
        }

        // now check proximity to each of the existing ones

        for (Planet x : planets) {
            double sep = x.position.dist(p.position);
            if (sep < Constants.radSep * (x.getRadius() + p.getRadius())) {
                System.out.println("Failed planet proximity: " + (int) sep);
                return false;
            }
        }
        return true;

    }

    // todo - set up the planets based on the params that have been passed


}
