package spinbattle;

import math.Vector2d;

public class MovableObject {
    Vector2d s, v;

    // may not need this...
    boolean isActive = false;

    public MovableObject copy() {
        MovableObject mo = new MovableObject();
        mo.s = s.copy();
        mo.v = v.copy();
        return mo;
    }

    public MovableObject update() {
        s.add(v);
        return this;
    }

}
