package caveswing.core;

import math.Vector2d;
import spinbattle.core.VectorField;

public class MovableObject {
    public Vector2d s;
    public Vector2d v;

    public MovableObject copy() {
        MovableObject mo = new MovableObject();
        mo.s = s.copy();
        mo.v = v.copy();
        return mo;
    }

    public MovableObject update(Vector2d resultantForce, double lossFactor) {
        v.add(resultantForce);
        s.add(v);
        v.mul(lossFactor);
        return this;
    }

    public String toString() {
        return s + " :: " + v;
    }
}
