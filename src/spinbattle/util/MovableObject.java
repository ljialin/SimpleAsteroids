package spinbattle.util;

import math.Vector2d;
import spinbattle.core.VectorField;

public class MovableObject {
    public Vector2d s;
    public Vector2d v;

    // may not need this...
    boolean isActive = false;

    // should really be moved to params, or in to VectoreField
    static double gConstant = 0.001;

    public MovableObject copy() {
        MovableObject mo = new MovableObject();
        mo.s = s.copy();
        mo.v = v.copy();
        return mo;
    }

    public MovableObject update(VectorField vf) {
        if (vf == null) {
            s.add(v);
        } else {
            v.add(vf.getForce(s), vf.getForceConstant());
            s.add(v);
        }
        return this;
    }

}
