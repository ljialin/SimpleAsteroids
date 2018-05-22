package spinbattle.core;

import logger.core.Trajectory;
import math.Vector2d;
import spinbattle.params.SpinBattleParams;
import spinbattle.util.MovableObject;

import java.awt.*;

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

    public String toString() {
        return String.format("%d -> %d (%.1f) >> %s", parent, target, payload, mo);
    }


    public Transporter next(VectorField vf) {
        if (mo != null) {
            mo.update(vf);
            if (trajectory != null)
                trajectory.addPoint(mo.s.copy());
            if (!params.inBounds(mo.s)) {
                outOfBoundsTermination();
            }
            payload -= params.transportTax;
        }
        return this;
    }

    public boolean inTransit() {
        return mo != null;
    }

    public Transporter terminateJourney() {
        payload = 0;
        mo = null;
        return this;
    }

    public Transporter outOfBoundsTermination() {
        // for now just terminate the journey, but other things are possible also ...
        // System.out.println("Out of bounds");
        terminateJourney();
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

    public Transporter setPayload(Planet source, double payload) {
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

    public Transporter incPayload(Planet source, double diff) {
        // payload may have already been set to a non-zero amount
        if (diff > source.shipCount) {
            diff = source.shipCount;
        }
        source.shipCount -= diff;
        this.payload += diff;
        return this;
    }

    Trajectory trajectory;

    static float alpha = 0.5f;
    static int lineWidth = 3;

    public Transporter launch(Vector2d start, Vector2d destination, int playerId, SpinGameState gameState) {
        // set up the mover
        mo = new MovableObject();
        mo.s = start.copy();
        mo.v = destination.copy().subtract(mo.s);
        mo.v.normalise();
        mo.v.mul(params.transitSpeed);
//        System.out.println("Launching with speed: " + mo.v.mag() + " : " + params.transitSpeed);
//        System.out.println(start + " ::: " + destination);
        this.ownedBy = playerId;
        // now add a trajectory, only if needed
        if (gameState.logger != null) {
            trajectory = gameState.logger.getTrajectoryLogger().addTrajectory();
            Color color = params.playerColors[playerId];
            float[] rgb = color.getRGBColorComponents(new float[3]);
            color = new Color(rgb[0], rgb[1], rgb[2], alpha);
            trajectory.setColor(color).setLineWidth(lineWidth);
        }
        return this;
    }

    public Transporter directionalLaunch(Vector2d start, double angle, int playerId, double transitSpeed) {
        // set up the mover
        mo = new MovableObject();
        mo.s = start.copy();
        mo.v = new Vector2d(Math.sin(angle), Math.cos(angle));
        mo.v.normalise();
        mo.v.mul(transitSpeed);
        this.ownedBy = playerId;
        return this;
    }

    public Transporter catapultLaunch(Vector2d start, Vector2d v, int playerId) {
        // set up the mover
        mo = new MovableObject();
        mo.s = start.copy();
        mo.v = v;
        // mo.v.normalise();
        // mo.v.mul(transitSpeed);
        this.ownedBy = playerId;
        return this;
    }
}
