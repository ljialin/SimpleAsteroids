package spinbattle.core;

import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.MovableObject;

/**
 *  Interesting class: we model both the ownedBy of the ships,
 *  and the parent planet of the ship.
 *
 *  This allows the possbility to assign ownership to the invasion
 *  based either on the ownedBy, or the owner of the planet which
 *  may have changed since launching the Transporter
 */

public class Transporter {
    public Planet parent;
    SpinBattleParams params;
    public int ownedBy;
    public double payload = 0;

    public MovableObject mo;

    public Transporter next() {
        if (mo != null) {
            mo.update();
        }
        return this;
    }

    public boolean inTransit() {
        return mo != null;
    }

    public Transporter terminateJourney() {
        mo = null;
        return this;
    }

    public Transporter setParent(Planet parent) {
        this.parent = parent;
        return this;
    }

    public Transporter incPayload(double payload) {
        // payload may have already been set to a non-zero amount
        double diff = payload - this.payload;
        // subtract the difference of
        if (diff > parent.shipCount) {
            diff = parent.shipCount;
        }
        parent.shipCount -= diff;
        this.payload += diff;
        // this.payload = payload;
        return this;
    }

    public Transporter launch(Vector2d destination, int playerId) {
        // set up the mover
        mo = new MovableObject();
        mo.s = parent.position.copy();
        mo.v = destination.copy().subtract(mo.s);
        mo.v.normalise();

        this.ownedBy = playerId;

        return this;
    }

}
