package spinbattle.view;

import math.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TransporterView {

    // define the shape of the ship
    static int[] xp = {-2, 0, 2, 0};
    static int[] yp = {2, -2, 2, 0};

    Vector2d s, v; //  d;
    double scale;

    public TransporterView setState(Vector2d s, Vector2d v, double scale) {
        this.s = s;
        this.v = v;
        // this.d = v.copy();
        this.scale = scale;
        // d.normalise();
        return this;
    }

    public String toString() {
        return s + "\t " + v;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(s.x, s.y);
        double rot = Math.atan2(v.y, v.x) + Math.PI / 2;
        g.rotate(rot);
        g.scale(scale, scale);
        g.fillPolygon(xp, yp, xp.length);
        g.setTransform(at);
    }
}
