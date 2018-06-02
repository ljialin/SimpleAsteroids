package caveswing.core;

import math.Vector2d;
import utilities.Picker;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Map {
    public ArrayList<Anchor> anchors;

    public Anchor getAnchor(int index) {
        if (index < anchors.size()) {
            return anchors.get(index);
        } else {
            return null;
        }
    }

    public Anchor getClosestAnchor(Vector2d s) {
        Picker<Anchor> picker = new Picker<>(Picker.MIN_FIRST);
        for (Anchor a : anchors) {
            picker.add(a.s.dist(s), a);
        }
        return picker.getBest();

    }

    // the map specifies the dimensions and the set of anchors
    Rectangle2D bounds;

    public Map setBounds(double width, double height) {
        bounds = new Rectangle2D.Double(0, 0, width, height);
        return this;
    }

    public Map setAnchors(int nAnchors, double meanHeight, double hooke) {
        anchors = new ArrayList<>();
        double gap = bounds.getWidth() / (nAnchors + 1);
        double x = gap/2;
        for (int i=0; i<nAnchors; i++) {
            Vector2d s = new Vector2d(x, meanHeight);
            anchors.add(new Anchor().setHooke(hooke).setPosition(s));
            x += gap;
        }
        return this;
    }

    public Map setup(CaveSwingParams params) {
        setBounds(params.width, params.height);
        setAnchors(params.nAnchors, params.meanAnchorHeight, params.hooke);
        return this;
    }
}
