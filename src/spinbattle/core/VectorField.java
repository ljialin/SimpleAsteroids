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

        System.out.println("Made vector field: " + t);
        return this;
    }

    private Vector2d forceAt(double x, double y, List<Planet> planets) {
        Vector2d f = new Vector2d();
        // add in all the forces weighted by the mass of each planet and the inverse square of the distance to it
        Vector2d q = new Vector2d(x, y);
        for (Planet p : planets) {
            double d = q.dist(p.position);
            double inv2 = 1 / (d * d);
            double m = p.mass();
            Vector2d pull = new Vector2d(p.position).subtract(q);
            f.add(pull, inv2 * m);
        }
        return f;
    }

    public Vector2d getForce(Vector2d s) {
        try {
            int x = (int) (s.x/cellSize), y = (int) (s.y/cellSize);
            return vf[x][y];
        } catch (Exception e) {
            e.printStackTrace();
            return new Vector2d();
        }
    }



}
