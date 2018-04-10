package spinbattle.core;

import math.Vector2d;
import utilities.ElapsedTimer;
import utilities.Picker;

import java.util.List;

public class ProximityMap {
    // 2d array showing which planet is in range of current point
    Integer[][] inRange;

    public ProximityMap setPlanets(SpinGameState gameState) {
        ElapsedTimer t = new ElapsedTimer();
        // loop over each planet
        // and for the square enclosing it, add each index that is
        // within its circular radius
        inRange = new Integer[gameState.params.width][gameState.params.height];
        for (Planet p : gameState.planets) {
            int rad = p.getRadius();
            for (int x = (int) p.position.x -rad; x <= (int) p.position.x + rad; x++) {
                for (int y = (int) p.position.y - rad; y <= (int) p.position.y + rad; y++) {
                    if (new Vector2d(x, y).dist(p.position) <= rad) {
                        inRange[x][y] = p.index;
                    }
                }
            }
        }
        // System.out.println("Proximity map set in: " + t);
        return this;
    }

    public ProximityMap setPlanetsOld(SpinGameState gameState) {
        // this was a simpler but inefficient method
        ElapsedTimer t = new ElapsedTimer();
        inRange = new Integer[gameState.params.width][gameState.params.height];
        for (int i=0; i<gameState.params.width; i++) {
            for (int j=0; j<gameState.params.height; j++) {
                inRange[i][j] = getClosestInRange(gameState.planets, i, j);
            }
        }
        System.out.println("Proximity map set in: " + t);
        return this;
    }

    public Planet getPlanet(SpinGameState gameState, Vector2d s) {
        Integer ix = getPlanetIndex(s);
        if (ix != null) {
            return gameState.planets.get(ix);
        } else {
            return null;
        }
    }

    public Integer getPlanetIndex(Vector2d s) {
        try {
            return inRange[(int) s.x][(int) s.y];
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getClosestInRange(List<Planet> planets, int x, int y) {
        // will return null if nothing in range
        Vector2d probe = new Vector2d(x, y);
        Picker<Integer> picker = new Picker<>(Picker.MIN_FIRST);
        for (Planet p : planets) {
            double dist = p.position.dist(probe);
            if (dist < p.getRadius()) {
                picker.add(dist, p.index);
            }
        }
        return picker.getBest();
    }
}
