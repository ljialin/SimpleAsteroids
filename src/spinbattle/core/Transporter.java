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
    // use ints to index the parent and the target planets
    public int parent;
    // use Integer to allow it to be null
    public Integer target;
    SpinBattleParams params;
    public int ownedBy;
    public double payload = 0;

    public MovableObject mo;

    public Transporter copy() {
        Transporter transporter = new Transporter();
        // just shallow copy; the planets themselves have already been deep copied?
        transporter.parent = parent;
        transporter.target = target;
        transporter.params = params;
        transporter.ownedBy = ownedBy;
        transporter.payload = payload;
        if (mo != null)
            transporter.mo = mo.copy();
        return transporter;
    }


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

    public Transporter setParent(int parent) {
        this.parent = parent;
        return this;
    }

    public Transporter setParams(SpinBattleParams params) {
        this.params = params;
        return this;
    }

    public Transporter setTarget(int target) {
        this.target = target;
        return this;
    }

    public Transporter incPayload(Planet source, double payload) {
        // payload may have already been set to a non-zero amount
        double diff = payload - this.payload;
        // subtract the difference of
        if (diff > source.shipCount) {
            diff = source.shipCount;
        }
        source.shipCount -= diff;
        this.payload += diff;
        // this.payload = payload;
        return this;
    }

    public Transporter launch(Vector2d start, Vector2d destination, int playerId) {
        // set up the mover
        mo = new MovableObject();
        mo.s = start.copy();
        mo.v = destination.copy().subtract(mo.s);
        mo.v.normalise();
        mo.v.mul(params.transitSpeed);
        this.ownedBy = playerId;
        return this;
    }
}
