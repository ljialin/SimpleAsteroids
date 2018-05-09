package logger.core;

import math.Vector2d;

import java.awt.*;
import java.util.ArrayList;

public class Trajectory {
    public ArrayList<Vector2d> points = new ArrayList<>();
    public Color fg = new Color(0, 255, 0, 64);
    public double lineWidth = 2;

    public Trajectory setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Trajectory setColor(Color fg) {
        this.fg = fg;
        return this;
    }

    public Trajectory addPoint(Vector2d x) {
        points.add(x);
        return this;
    }
}

