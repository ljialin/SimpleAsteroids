package spinbattle.core;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import utilities.ElapsedTimer;

import java.util.List;

public class VectorField {
    public Vector2d[][] vf;
    public int cellSize = 10;
    public int w;
    public int h;
    SpinBattleParams params;
    SpinGameState gameState;

    public VectorField setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    public VectorField setField(SpinGameState gameState) {
        ElapsedTimer t = new ElapsedTimer();
        this.gameState = gameState;

        // calculate gravity field at each point

        w = params.width / cellSize;
        h = params.height / cellSize;

        vf = new Vector2d[w][h];

        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                vf[x][y] = forceAt((x+0.5) * cellSize, (y+0.5) * cellSize, gameState.planets);
            }
        }

        // System.out.println("Made vector field: " + t);
        return this;
    }

    private Vector2d forceAt(double x, double y, List<Planet> planets) {
        Vector2d f = new Vector2d();
        // add in all the forces weighted by the mass of each planet and the inverse square of the distance to it
        Vector2d q = new Vector2d(x, y);
        for (Planet p : planets) {
            double d = q.dist(p.position);
            // experiment with having zero gravity within a planet's radius
            if (d < p.getRadius()) return new Vector2d();
            // experiment with an inverse law
            // double inv2 = .005 / (d * 1);
            double inv2 = params.gravitationalFieldConstant / (d * d);
            double m = p.mass();
            Vector2d pull = new Vector2d(p.position).subtract(q);
            f.add(pull, inv2 * m);
        }
        return f;
    }

    public Vector2d getForce(Vector2d s) {
        try {
            int x = (int) (s.x/cellSize), y = (int) (s.y/cellSize);
            Vector2d vec = vf[x][y];
            if (vec == null) {
                // throw new RuntimeException("Null Vector in Vector Field");
                System.out.println("Warning: null Vector in VF");
                return new Vector2d();
            } else {
                return vec;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return new Vector2d();
        }
    }

    public double getForceConstant() {
        return params.gravitationalForceConstant;
    }



}
