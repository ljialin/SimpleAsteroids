package spinbattle.core;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.MovableObject;

/**
 *  Interesting class: we model both the playerId of the ships,
 *  and the parent planet of the ship.
 *
 *  This allows the possbility to assign ownership to the invasion
 *  based either on the playerId, or the owner of the planet which
 *  may have changed since launching the Transporter
 */

public class Transporter {
    public Planet parent;
    SpinBattleParams params;
    public int playerId;
    public double payload;

    MovableObject mo;

    public Transporter next() {
        if (mo != null) {
            mo.update();
        }
        return this;
    }

    public Transporter setParent(Planet parent) {
        this.parent = parent;
        return this;
    }

    public Transporter setPayload(double payload) {
        this.payload = payload;
        return this;
    }

    public Transporter launch(Vector2d destination, int playerId) {
        // set up the mover
        mo = new MovableObject();
        mo.s = parent.position;
        mo.v = destination.copy().subtract(mo.s);
        mo.v.normalise();

        return this;
    }

}
