package asteroids;

import math.Vector2d;

import java.awt.*;

public abstract class GameObject {
    public Vector2d s,v;
    boolean isTarget;
    public boolean dead;
    double r;

    protected GameObject(Vector2d s, Vector2d v) {
        this.s = new Vector2d(s);
        this.v = new Vector2d(v);
    }

    public abstract void update(GameState gameState);
    public abstract void draw(Graphics2D g);

    public abstract boolean dead();

    public void hit() {
        dead  = true;
    }

    public double r() {
        return r;
    }

    public boolean wrappable() {
        // wrap objects by default
        return true;
    }

    // public abstract GameObject copy();



}
