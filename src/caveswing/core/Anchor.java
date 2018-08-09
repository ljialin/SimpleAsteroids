package caveswing.core;

import math.Vector2d;

public class Anchor {

    // at the moment all anchors may have the same characteristics
    // but they could potentially be different
    double hooke = 0.001;

    public Vector2d s;

    public Anchor setPosition(Vector2d s) {
        this.s = s.copy();
        return this;
    }

    public Anchor setHooke(double hooke) {
        this.hooke = hooke;
        return this;
    }

    public Vector2d getForce(Vector2d position) {
        // note that this is currently modelled as an elastic
        // with zero natural length; would be easy to update this
        // to model as a spring
        Vector2d tension = new Vector2d(s);
        tension.subtract(position);
        tension.mul(hooke);
        return tension;
    }

    public Vector2d getForce(Vector2d position, double hooke) {
        // note that this is currently modelled as an elastic
        // with zero natural length; would be easy to update this
        // to model as a spring
        Vector2d tension = new Vector2d(s);
        tension.subtract(position);
        tension.mul(hooke);
        return tension;
    }
}
